package com.innovatrics.android.dot.sample.fragment;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import com.innovatrics.android.dot.sample.activity.InstructedFaceCaptureActivity;
import com.innovatrics.android.dot.sample.model.InstructedFaceCaptureModel;
import com.innovatrics.dot.face.autocapture.FaceAutoCaptureFragment;
import com.innovatrics.dot.face.autocapture.steps.CaptureStepId;
import com.innovatrics.dot.face.detection.DetectedFace;

public class InstructedDemoFaceCaptureFragment extends FaceAutoCaptureFragment {

    private InstructedFaceCaptureModel instructedFaceCaptureModel;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        start();
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        instructedFaceCaptureModel = ViewModelProviders.of(getActivity()).get(InstructedFaceCaptureModel.class);
    }

    @Override
    protected void onNoCameraPermission() {
        instructedFaceCaptureModel.callFaceCaptureEvent(InstructedFaceCaptureActivity.RESULT_NO_CAMERA_PERMISSION, null);
    }

    @Override
    protected void onStepChanged(CaptureStepId captureStepId, DetectedFace detectedFace) {

    }

    @Override
    protected void onCaptured(final DetectedFace detectedFace) {
        instructedFaceCaptureModel.callFaceCaptureEvent(InstructedFaceCaptureActivity.RESULT_SUCCESS, detectedFace);
    }

    @Override
    protected void onStopped() {

    }

}
