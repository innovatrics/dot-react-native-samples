package com.innovatrics.android.dot.sample;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.innovatrics.android.dot.Dot;
import com.innovatrics.android.dot.dto.LivenessCheck2Arguments;
import com.innovatrics.android.dot.livenesscheck.liveness.DotPosition;
import com.innovatrics.android.dot.livenesscheck.model.SegmentConfiguration;
import com.innovatrics.android.dot.sample.activity.DocumentCaptureActivity;
import com.innovatrics.android.dot.sample.activity.LivenessCheck2Activity;
import com.innovatrics.android.dot.utils.LicenseUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// A native module is a Java class that usually extends the ReactContextBaseJavaModule class and implements the functionality required by the JavaScript.
final class ActivityStarterModule extends ReactContextBaseJavaModule {
  private static final int REQUEST_DOCUMENT_CAPTURE = 7;
  private static final int REQUEST_LIVENESS_CHECK_2 = 6;
  private Promise mPickerPromise;

  // return value represent class in javascript
  // `NativeModules.ActivityStarter`
  @Override
  public String getName() {
    return "ActivityStarter";
  }

  // @ReactMethod decorator tells react-native to expose this method in javascript to use
  // react-native automatically provides javascript `Promise` as an argument
  @ReactMethod
  public void initDot(Promise promise) {
    mPickerPromise = promise;

    if (Dot.getInstance().isInitialized()) {
      return;
    } else {
      ReactApplicationContext context = getReactApplicationContext();
      if (context != null) {
        final byte[] license = LicenseUtils.loadRawLicense(context, R.raw.sample_license);
        final Dot.Listener dotListener = new Dot.Listener() {
          @Override
          public void onInitSuccess() {
            mPickerPromise.resolve(true);
          }

          @Override
          public void onInitFail(final String message) {
            mPickerPromise.reject(new Error(message));
          }

          @Override
          public void onClosed() {
          }
        };

        Dot.getInstance().initAsync(license, dotListener);
      }
    }
  }

  @ReactMethod
  public void startDocumentCaptureActivity(Promise promise) {
    Activity activity = getCurrentActivity();
    if (activity != null) {
      mPickerPromise = promise;
      Intent intent = new Intent(activity, DocumentCaptureActivity.class);
      activity.startActivityForResult(intent, REQUEST_DOCUMENT_CAPTURE);
    }
  }

  @ReactMethod
  public void startLivenessCheckActivity(Promise promise) {
    Activity activity = getCurrentActivity();
    if (activity != null) {
      final List<SegmentConfiguration> segmentList = createSegmentConfigurationList();
      final LivenessCheck2Arguments livenessCheck2Arguments = new LivenessCheck2Arguments.Builder()
              .segmentList(segmentList)
              .transitionType(LivenessCheck2Arguments.TransitionType.MOVING)
              .dotColorResId(R.color.colorPrimary)
              .build();
      mPickerPromise = promise;
      Intent intent = new Intent(activity, LivenessCheck2Activity.class);
      intent.putExtra(LivenessCheck2Activity.ARGUMENTS, livenessCheck2Arguments);

      activity.startActivityForResult(intent, REQUEST_LIVENESS_CHECK_2);
    }
  }

  private List<SegmentConfiguration> createSegmentConfigurationList() {
    final List<SegmentConfiguration> list = new ArrayList<>();

    for (int i = 0; i < 8; i++) {
      final DotPosition position = (DotPosition.getRandomPositionExclude(Arrays.asList(
              i > 0 ? list.get(i - 1).getTargetPosition() : null,
              i > 1 ? list.get(i - 2).getTargetPosition() : null)));
      list.add(new SegmentConfiguration(position.name(), 1000));
    }

    return list;
  }

  // we use `BaseActivityEventListener` to asynchronously get data from `activity.startActivityForResult`
  // see https://facebook.github.io/react-native/docs/native-modules-android#promises
  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

    @Override
    public void onActivityResult(Activity activity, final int requestCode, final int resultCode, final Intent intent) {
      switch (requestCode) {
        case REQUEST_DOCUMENT_CAPTURE:
          if (resultCode == DocumentCaptureActivity.RESULT_SUCCESS) {
            Uri photoUri = intent.getData();
            if (photoUri != null) {
              ImageDecoder.Source source = ImageDecoder.createSource(getReactApplicationContext().getContentResolver(), photoUri);
              try {
                Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] byteArray = outputStream.toByteArray();

                String encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);
                mPickerPromise.resolve(encodedString);
              } catch (IOException exception) {
                mPickerPromise.reject(exception);
              }
            } else {
              mPickerPromise.reject(new Error("no image found!"));
            }
          } else {
            mPickerPromise.reject(new Error(Integer.toString(resultCode)));
          }
          break;
        case REQUEST_LIVENESS_CHECK_2:
          if (resultCode == LivenessCheck2Activity.RESULT_DONE) {
            Uri photoUri = intent.getData();
            float score = intent.getFloatExtra(LivenessCheck2Activity.OUT_SCORE, 0);
            // `WritableMap` is translated into javascript object
            WritableMap map = Arguments.createMap();
            map.putString("photoUri", photoUri.toString());
            map.putDouble("score", score);
            mPickerPromise.resolve(map);
          } else {
            mPickerPromise.reject(new Error(Integer.toString(resultCode)));
          }
          break;
      }
    }
  };

  ActivityStarterModule(ReactApplicationContext reactContext) {
    super(reactContext);
    // we add event listener to `ReactApplicationContext`
    reactContext.addActivityEventListener(mActivityEventListener);
  }

}