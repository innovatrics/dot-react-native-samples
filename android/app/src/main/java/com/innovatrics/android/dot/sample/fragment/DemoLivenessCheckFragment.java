package com.innovatrics.android.dot.sample.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.innovatrics.android.dot.fragment.LivenessCheckFragment;
import com.innovatrics.android.dot.livenesscheck.controller.FaceLivenessState;
import com.innovatrics.android.dot.livenesscheck.liveness.SegmentPhoto;
import com.innovatrics.android.dot.sample.activity.LivenessCheckActivity;
import com.innovatrics.android.dot.sample.model.LivenessCheckModel;

import java.util.List;

public class DemoLivenessCheckFragment extends LivenessCheckFragment {

    private LivenessCheckModel livenessCheckModel;

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        livenessCheckModel = ViewModelProviders.of(getActivity()).get(LivenessCheckModel.class);
    }

    @Override
    protected void onCameraReady() {
        startLivenessCheck();
    }

    @Override
    protected void onCameraInitFailed() {
        getActivity().setResult(LivenessCheckActivity.RESULT_CAMERA_INIT_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onCameraAccessFailed() {
        getActivity().setResult(LivenessCheckActivity.RESULT_CAMERA_ACCESS_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onLivenessStateChange(final FaceLivenessState faceLivenessState) {
        if (faceLivenessState == FaceLivenessState.LOST) {
            restartTransitionView();
            startLivenessCheck();
        }
    }

    @Override
    protected void onLivenessCheckDone(final float score, final List<SegmentPhoto> segmentPhotoList) {
        livenessCheckModel.livenessCheckDone(score, segmentPhotoList);
    }

    @Override
    protected void onLivenessCheckFailedNoMoreSegments() {
        getActivity().setResult(LivenessCheckActivity.RESULT_NO_MORE_SEGMENTS);
        getActivity().finish();
    }

    @Override
    protected void onLivenessCheckFailedEyesNotDetected() {
        getActivity().setResult(LivenessCheckActivity.RESULT_EYES_NOT_DETECTED);
        getActivity().finish();
    }

    @Override
    protected void onNoCameraPermission() {
        getActivity().setResult(LivenessCheckActivity.RESULT_NO_CAMERA_PERMISSION);
        getActivity().finish();
    }

    @Override
    protected void onNoFrontCamera() {
        getActivity().setResult(LivenessCheckActivity.RESULT_NO_FRONT_CAMERA);
        getActivity().finish();
    }

}
