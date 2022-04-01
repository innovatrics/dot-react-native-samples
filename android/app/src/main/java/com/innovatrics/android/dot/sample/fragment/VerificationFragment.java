package com.innovatrics.android.dot.sample.fragment;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import com.innovatrics.android.dot.sample.activity.VerificationActivity;
import com.innovatrics.android.dot.sample.model.VerificationModel;
import com.innovatrics.dot.face.detection.DetectedFace;
import com.innovatrics.dot.face.simplecapture.FaceSimpleCaptureFragment;

public class VerificationFragment extends FaceSimpleCaptureFragment {

    private VerificationModel verificationModel;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        start();
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        verificationModel = ViewModelProviders.of(getActivity()).get(VerificationModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestCapture();
    }

    @Override
    protected void onNoCameraPermission() {
        getActivity().setResult(VerificationActivity.RESULT_NO_CAMERA_PERMISSION);
        getActivity().finish();
    }

    @Override
    protected void onCaptured(final DetectedFace detectedFace) {
        verificationModel.verify(detectedFace);
    }

    @Override
    protected void onStopped() {

    }

}
