package com.innovatrics.android.dot.sample.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.innovatrics.android.dot.face.DetectedFace;
import com.innovatrics.android.dot.fragment.FaceCaptureSimpleFragment;
import com.innovatrics.android.dot.sample.activity.VerificationActivity;
import com.innovatrics.android.dot.sample.model.VerificationModel;

public class VerificationFragment extends FaceCaptureSimpleFragment {

    private VerificationModel verificationModel;

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        verificationModel = ViewModelProviders.of(getActivity()).get(VerificationModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestPhoto();
    }

    @Override
    protected void onCameraInitFailed() {
        getActivity().setResult(VerificationActivity.RESULT_CAMERA_INIT_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onCameraAccessFailed() {
        getActivity().setResult(VerificationActivity.RESULT_CAMERA_ACCESS_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onNoCameraPermission() {
        getActivity().setResult(VerificationActivity.RESULT_NO_CAMERA_PERMISSION);
        getActivity().finish();
    }

    @Override
    protected void onCapture(final DetectedFace detectedFace) {
        verificationModel.verify(detectedFace);
    }

}
