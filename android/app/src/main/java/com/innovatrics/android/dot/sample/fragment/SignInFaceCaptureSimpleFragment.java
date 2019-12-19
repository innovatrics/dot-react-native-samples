package com.innovatrics.android.dot.sample.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.innovatrics.android.dot.face.DetectedFace;
import com.innovatrics.android.dot.fragment.FaceCaptureSimpleFragment;
import com.innovatrics.android.dot.sample.activity.SignInActivity;
import com.innovatrics.android.dot.sample.model.SignInModel;

public class SignInFaceCaptureSimpleFragment extends FaceCaptureSimpleFragment {

    private SignInModel signInModel;

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        signInModel = ViewModelProviders.of(getActivity()).get(SignInModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestPhoto();
    }

    @Override
    protected void onCameraInitFailed() {
        getActivity().setResult(SignInActivity.RESULT_CAMERA_INIT_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onCameraAccessFailed() {
        getActivity().setResult(SignInActivity.RESULT_CAMERA_ACCESS_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onNoCameraPermission() {
        getActivity().setResult(SignInActivity.RESULT_NO_CAMERA_PERMISSION);
        getActivity().finish();
    }

    @Override
    protected void onCapture(final DetectedFace detectedFace) {
        signInModel.faceCaptureDone(detectedFace);
    }

}
