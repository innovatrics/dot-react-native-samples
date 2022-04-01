package com.innovatrics.android.dot.sample.fragment;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import com.innovatrics.android.dot.sample.activity.OnboardingActivity;
import com.innovatrics.android.dot.sample.model.OnboardingModel;
import com.innovatrics.dot.face.autocapture.FaceAutoCaptureFragment;
import com.innovatrics.dot.face.autocapture.steps.CaptureStepId;
import com.innovatrics.dot.face.detection.DetectedFace;

public class OnboardingFaceCaptureFragment extends FaceAutoCaptureFragment {

    private OnboardingModel onboardingModel;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        start();
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onboardingModel = ViewModelProviders.of(getActivity()).get(OnboardingModel.class);
    }

    @Override
    protected void onNoCameraPermission() {
        getActivity().setResult(OnboardingActivity.RESULT_NO_CAMERA_PERMISSION);
        getActivity().finish();
    }

    @Override
    protected void onStepChanged(CaptureStepId captureStepId, DetectedFace detectedFace) {

    }

    @Override
    protected void onCaptured(final DetectedFace detectedFace) {
        onboardingModel.faceCaptureDone(detectedFace);
    }

    @Override
    protected void onStopped() {

    }

}