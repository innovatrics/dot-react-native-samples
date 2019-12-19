package com.innovatrics.android.dot.sample.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.innovatrics.android.dot.dto.Photo;
import com.innovatrics.android.dot.face.DetectedFace;
import com.innovatrics.android.dot.facecapture.steps.CaptureState;
import com.innovatrics.android.dot.fragment.FaceCaptureFragment;
import com.innovatrics.android.dot.sample.activity.OnboardingActivity;
import com.innovatrics.android.dot.sample.model.OnboardingModel;

public class OnboardingFaceCaptureFragment extends FaceCaptureFragment {

    private OnboardingModel onboardingModel;

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onboardingModel = ViewModelProviders.of(getActivity()).get(OnboardingModel.class);
    }

    @Override
    protected void onCameraInitFailed() {
        getActivity().setResult(OnboardingActivity.RESULT_CAMERA_INIT_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onCameraAccessFailed() {
        getActivity().setResult(OnboardingActivity.RESULT_CAMERA_ACCESS_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onNoCameraPermission() {
        getActivity().setResult(OnboardingActivity.RESULT_NO_CAMERA_PERMISSION);
        getActivity().finish();
    }

    @Override
    protected void onCaptureStateChange(final CaptureState captureState, final Photo photo) {
    }

    @Override
    protected void onCaptureSuccess(final DetectedFace detectedFace) {
        onboardingModel.faceCaptureDone(detectedFace);
    }

    @Override
    protected void onCaptureFail() {
    }

}
