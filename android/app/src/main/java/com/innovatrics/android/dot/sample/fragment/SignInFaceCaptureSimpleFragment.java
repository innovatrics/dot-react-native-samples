package com.innovatrics.android.dot.sample.fragment;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import com.innovatrics.android.dot.sample.activity.SignInActivity;
import com.innovatrics.android.dot.sample.model.SignInModel;
import com.innovatrics.dot.face.detection.DetectedFace;
import com.innovatrics.dot.face.simplecapture.FaceSimpleCaptureFragment;

public class SignInFaceCaptureSimpleFragment extends FaceSimpleCaptureFragment {

    private SignInModel signInModel;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        start();
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        signInModel = ViewModelProviders.of(getActivity()).get(SignInModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestCapture();
    }

    @Override
    protected void onNoCameraPermission() {
        getActivity().setResult(SignInActivity.RESULT_NO_CAMERA_PERMISSION);
        getActivity().finish();
    }

    @Override
    protected void onCaptured(final DetectedFace detectedFace) {
        signInModel.faceCaptureDone(detectedFace);
    }

    @Override
    protected void onStopped() {

    }

}
