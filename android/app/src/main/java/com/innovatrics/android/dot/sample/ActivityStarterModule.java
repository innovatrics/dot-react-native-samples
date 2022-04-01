package com.innovatrics.android.dot.sample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import com.facebook.react.bridge.*;
import com.innovatrics.android.commons.io.RawResourceReader;
import com.innovatrics.android.dot.sample.activity.DocumentCaptureActivity;
import com.innovatrics.android.dot.sample.activity.EyeGazeLivenessCheckActivity;
import com.innovatrics.android.dot.sample.activity.FaceCaptureActivity;
import com.innovatrics.dot.face.DotFace;
import com.innovatrics.dot.face.DotFaceConfiguration;
import com.innovatrics.dot.face.detection.fast.DotFaceDetectionFastModule;
import com.innovatrics.dot.face.eyegazeliveness.DotFaceEyeGazeLivenessModule;
import com.innovatrics.dot.face.liveness.eyegaze.EyeGazeLivenessConfiguration;
import com.innovatrics.dot.face.liveness.eyegaze.RandomSegmentsGenerator;
import com.innovatrics.dot.face.liveness.eyegaze.Segment;
import com.innovatrics.dot.face.liveness.eyegaze.SegmentsGenerator;
import com.innovatrics.dot.face.modules.DotFaceModule;
import com.innovatrics.dot.face.passiveliveness.DotFacePassiveLivenessModule;
import com.innovatrics.dot.face.verification.DotFaceVerificationModule;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// A native module is a Java class that usually extends the ReactContextBaseJavaModule class and implements the functionality required by the JavaScript.
final class ActivityStarterModule extends ReactContextBaseJavaModule {
  private static final int REQUEST_FACE_CAPTURE = 0;
  private static final int REQUEST_DOCUMENT_CAPTURE = 7;
  private static final int REQUEST_EYE_GAZE_LIVENESS_CHECK = 6;
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

    if (!DotFace.getInstance().isInitialized()) {
      ReactApplicationContext context = getReactApplicationContext();
      final byte[] license = new RawResourceReader(context.getResources()).read(R.raw.iengine);
      final DotFace.InitializationListener listener = new DotFace.InitializationListener() {
        @Override
        public void onFinished(DotFace.Result result) {
          if (result.getCode() == DotFace.Result.Code.OK) {
            mPickerPromise.resolve(true);
          } else {
            mPickerPromise.reject(new Error(result.getException().getMessage()));
          }
        }
      };
      final List<DotFaceModule> modulesList = new ArrayList<DotFaceModule>() {
        {
          add(DotFaceDetectionFastModule.of());
          add(DotFaceVerificationModule.of());
          add(DotFaceEyeGazeLivenessModule.of());
          add(DotFacePassiveLivenessModule.of());
        }
      };
      DotFaceConfiguration configuration = new DotFaceConfiguration.Builder(context, license, modulesList).build();
      DotFace.getInstance().initializeAsync(configuration, listener);
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
      final List<Segment> segmentList = createSegmentList();
      final EyeGazeLivenessConfiguration eyeGazeLivenessConfiguration = new EyeGazeLivenessConfiguration.Builder(segmentList)
              .transitionType(EyeGazeLivenessConfiguration.TransitionType.MOVE)
              .build();
      mPickerPromise = promise;
      Intent intent = new Intent(activity, EyeGazeLivenessCheckActivity.class);
      intent.putExtra(EyeGazeLivenessCheckActivity.ARGUMENTS, eyeGazeLivenessConfiguration);

      activity.startActivityForResult(intent, REQUEST_EYE_GAZE_LIVENESS_CHECK);
    }
  }

  @ReactMethod
  public void startFaceAutoCaptureActivity(Promise promise) {
    Activity activity = getCurrentActivity();
    if (activity != null) {
      mPickerPromise = promise;
      Intent intent = new Intent(activity, FaceCaptureActivity.class);
      activity.startActivityForResult(intent, REQUEST_FACE_CAPTURE);
    }
  }

  private List<Segment> createSegmentList() {
    SegmentsGenerator segmentsGenerator = new RandomSegmentsGenerator();
    int segmentCount = 5;
    int segmentDurationMillis = 1000;
    return segmentsGenerator.generate(segmentCount, segmentDurationMillis);
  }

  private String getBase64fromUri(Uri imageUri) {
    try {
      Bitmap bitmap = MediaStore.Images.Media.getBitmap(getReactApplicationContext().getContentResolver(), imageUri);
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
      byte[] byteArray = outputStream.toByteArray();

      String encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);
      return encodedString;
    } catch (IOException e) {
      return null;
    }
  }

  // we use `BaseActivityEventListener` to asynchronously get data from `activity.startActivityForResult`
  // see https://facebook.github.io/react-native/docs/native-modules-android#promises
  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

    @Override
    public void onActivityResult(Activity activity, final int requestCode, final int resultCode, final Intent intent) {
      switch (requestCode) {
        case REQUEST_FACE_CAPTURE:
          if (resultCode == FaceCaptureActivity.RESULT_SUCCESS) {
            Uri photoUri = intent.getData();
            if (photoUri != null) {
              String encodedString = getBase64fromUri(photoUri);
              if (encodedString != null) {
                mPickerPromise.resolve(encodedString);
              } else {
                mPickerPromise.reject(new Error("no image found!"));
              }
            } else {
              mPickerPromise.reject(new Error("no image found!"));
            }
          } else {
            mPickerPromise.reject(new Error(Integer.toString(resultCode)));
          }
          break;
        case REQUEST_DOCUMENT_CAPTURE:
          if (resultCode == DocumentCaptureActivity.RESULT_SUCCESS) {
            Uri photoUri = intent.getData();
            if (photoUri != null) {
              String encodedString = getBase64fromUri(photoUri);
              if (encodedString != null) {
                mPickerPromise.resolve(encodedString);
              } else {
                mPickerPromise.reject(new Error("no image found!"));
              }
            } else {
              mPickerPromise.reject(new Error("no image found!"));
            }
          } else {
            mPickerPromise.reject(new Error(Integer.toString(resultCode)));
          }
          break;
        case REQUEST_EYE_GAZE_LIVENESS_CHECK:
          if (resultCode == EyeGazeLivenessCheckActivity.RESULT_DONE) {
            Uri photoUri = intent.getData();
            float score = intent.getFloatExtra(EyeGazeLivenessCheckActivity.OUT_SCORE, 0);
            if (photoUri != null) {
              String encodedString = getBase64fromUri(photoUri);
              if (encodedString != null) {
                WritableMap map = Arguments.createMap();
                map.putString("photoUri", encodedString);
                map.putDouble("score", score);
                mPickerPromise.resolve(map);
              } else {
                mPickerPromise.reject(new Error("no image found!"));
              }
            }
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