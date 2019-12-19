package com.innovatrics.android.dot.sample.model;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.innovatrics.android.dot.face.DetectedFace;
import com.innovatrics.android.dot.sample.enums.ErrorCode;
import com.innovatrics.android.dot.sample.http.CallbackImpl;
import com.innovatrics.android.dot.sample.http.Webservice;
import com.innovatrics.android.dot.sample.http.WebserviceCallback;
import com.innovatrics.android.dot.sample.http.WebserviceFactory;
import com.innovatrics.android.dot.sample.http.dto.SignInStartRequest;
import com.innovatrics.android.dot.sample.http.dto.SignInStartResponse;
import com.innovatrics.android.dot.sample.http.dto.SignInUploadFaceImageRequest;
import com.innovatrics.android.dot.sample.http.dto.SignInVerifyResponse;
import com.innovatrics.android.dot.sample.http.dto.UserDetailResponse;
import com.innovatrics.android.dot.sample.utils.SingleLiveEvent;
import com.innovatrics.android.dot.sample.utils.Utils;

import java.io.IOException;

public class SignInModel extends AndroidViewModel {

    public static final int SCREEN_FACE_CAPTURE_SIMPLE = 0;

    private static final String TAG = OnboardingModel.class.getSimpleName();

    private final String userId;
    private final MutableLiveData<Integer> screenLiveData;
    private final SingleLiveEvent<ErrorCode> errorEvent;
    private final SingleLiveEvent<Void> cancelEvent;
    private final SingleLiveEvent<String> successEvent;
    private final SingleLiveEvent<Void> failEvent;
    private final Webservice webservice;

    private String bearerToken;

    public SignInModel(final Application application, final String appServerUrl, final String userId) {
        super(application);

        this.userId = userId;

        screenLiveData = new MutableLiveData<>();
        errorEvent = new SingleLiveEvent<>();
        cancelEvent = new SingleLiveEvent<>();
        successEvent = new SingleLiveEvent<>();
        failEvent = new SingleLiveEvent<>();
        webservice = WebserviceFactory.create(application, appServerUrl);

        final SignInStartRequest signInStartRequest = new SignInStartRequest();
        signInStartRequest.setUserId(userId);

        webservice.signInStart(signInStartRequest).enqueue(new CallbackImpl<>(new WebserviceCallback<SignInStartResponse>() {

            @Override
            public void onResponse(final SignInStartResponse response) {
                bearerToken = response.getBearerToken();
                screenLiveData.postValue(SCREEN_FACE_CAPTURE_SIMPLE);
            }

            @Override
            public void onError(final ErrorCode errorCode) {
                errorEvent.postValue(errorCode);
            }

        }));
    }

    public LiveData<Integer> getScreenLiveData() {
        return screenLiveData;
    }

    public LiveData<ErrorCode> getErrorEvent() {
        return errorEvent;
    }

    public LiveData<Void> getCancelEvent() {
        return cancelEvent;
    }

    public LiveData<String> getSuccessEvent() {
        return successEvent;
    }

    public LiveData<Void> getFailEvent() {
        return failEvent;
    }

    private java.io.File createCacheFile() {
        try {
            return java.io.File.createTempFile("image_", ".jpg", getApplication().getCacheDir());
        } catch (final IOException e) {
            throw new RuntimeException("Unable to create file.");
        }
    }

    public void faceCaptureDone(final DetectedFace detectedFace) {
        final Bitmap bitmap = detectedFace.createFullImage();
        final Uri uri = Uri.fromFile(createCacheFile());
        com.innovatrics.android.dot.utils.Utils.saveBitmapAsJpeg(bitmap, uri);

        final byte[] faceCapture;

        try {
            faceCapture = Utils.getBytes(getApplication(), uri);
        } catch (final IOException e) {
            Log.e(TAG, "Unable to read image.", e);
            errorEvent.postValue(ErrorCode.UNEXPECTED_ERROR);
            return;
        }

        final SignInUploadFaceImageRequest request = new SignInUploadFaceImageRequest();
        request.setImage(faceCapture);

        webservice.signInUploadFaceImage("Bearer " + bearerToken, request).enqueue(new CallbackImpl<>(new WebserviceCallback<Void>() {

            @Override
            public void onResponse(final Void response) {
                verify();
            }

            @Override
            public void onError(final ErrorCode errorCode) {
                errorEvent.postValue(errorCode);
            }

        }));
    }

    private void verify() {
        webservice.signInVerify("Bearer " + bearerToken).enqueue(new CallbackImpl<>(new WebserviceCallback<SignInVerifyResponse>() {

            @Override
            public void onResponse(final SignInVerifyResponse response) {
                bearerToken = response.getBearerToken();
                readUserDetail();
            }

            @Override
            public void onError(final ErrorCode errorCode) {
                if (errorCode == ErrorCode.NOT_SAME_PERSON) {
                    failEvent.postValue(null);
                } else {
                    errorEvent.postValue(errorCode);
                }
            }

        }));
    }

    private void readUserDetail() {
        webservice.userDetail("Bearer " + bearerToken).enqueue(new CallbackImpl<>(new WebserviceCallback<UserDetailResponse>() {

            @Override
            public void onResponse(final UserDetailResponse response) {
                final String username = response.getGivenNames() + " " + response.getSurname();
                successEvent.postValue(username);
            }

            @Override
            public void onError(final ErrorCode errorCode) {
                errorEvent.postValue(errorCode);
            }

        }));
    }

    public void cancel() {
        cancelEvent.postValue(null);
    }

}
