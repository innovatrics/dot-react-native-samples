package com.innovatrics.android.dot.sample.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.innovatrics.android.dot.dto.Photo;
import com.innovatrics.android.dot.face.DetectedFace;
import com.innovatrics.android.dot.facecapture.steps.CaptureState;
import com.innovatrics.android.dot.fragment.FaceCaptureFragment;
import com.innovatrics.android.dot.sample.activity.InstructedFaceCaptureActivity;
import com.innovatrics.android.dot.sample.model.InstructedFaceCaptureModel;

public class InstructedDemoFaceCaptureFragment extends FaceCaptureFragment {

    private InstructedFaceCaptureModel instructedFaceCaptureModel;

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        instructedFaceCaptureModel = ViewModelProviders.of(getActivity()).get(InstructedFaceCaptureModel.class);
    }

    @Override
    protected void onCameraInitFailed() {
        getActivity().setResult(InstructedFaceCaptureActivity.RESULT_CAMERA_INIT_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onCameraAccessFailed() {
        getActivity().setResult(InstructedFaceCaptureActivity.RESULT_CAMERA_ACCESS_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onNoCameraPermission() {
        instructedFaceCaptureModel.callFaceCaptureEvent(InstructedFaceCaptureActivity.RESULT_NO_CAMERA_PERMISSION, null);
    }

    @Override
    protected void onCaptureStateChange(CaptureState captureState, Photo photo) {

    }

    @Override
    protected void onCaptureSuccess(final DetectedFace detectedFace) {
        instructedFaceCaptureModel.callFaceCaptureEvent(InstructedFaceCaptureActivity.RESULT_SUCCESS, detectedFace);
    }

    @Override
    protected void onCaptureFail() {
        instructedFaceCaptureModel.callFaceCaptureEvent(InstructedFaceCaptureActivity.RESULT_FAIL, null);
    }

}
