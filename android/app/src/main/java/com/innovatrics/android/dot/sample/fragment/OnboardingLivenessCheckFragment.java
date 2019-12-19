package com.innovatrics.android.dot.sample.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.innovatrics.android.dot.fragment.LivenessCheckFragment;
import com.innovatrics.android.dot.livenesscheck.controller.FaceLivenessState;
import com.innovatrics.android.dot.livenesscheck.liveness.SegmentPhoto;
import com.innovatrics.android.dot.sample.activity.OnboardingActivity;
import com.innovatrics.android.dot.sample.model.OnboardingModel;

import java.util.List;

public class OnboardingLivenessCheckFragment extends LivenessCheckFragment {

    private OnboardingModel onboardingModel;

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onboardingModel = ViewModelProviders.of(getActivity()).get(OnboardingModel.class);
    }

    @Override
    protected void onCameraReady() {
        startLivenessCheck();
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
    protected void onLivenessStateChange(final FaceLivenessState faceLivenessState) {
        if (faceLivenessState == FaceLivenessState.LOST) {
            restartTransitionView();
            startLivenessCheck();
        }
    }

    @Override
    protected void onLivenessCheckDone(final float score, final List<SegmentPhoto> segmentPhotoList) {
        onboardingModel.livenessCheckDone(score, segmentPhotoList);
    }

    @Override
    protected void onLivenessCheckFailedNoMoreSegments() {
        new OnboardingLivenessCheckFailDialogFragment().show(getChildFragmentManager(), OnboardingLivenessCheckFailDialogFragment.TAG);
    }

    @Override
    protected void onLivenessCheckFailedEyesNotDetected() {
        new OnboardingLivenessCheckFailDialogFragment().show(getChildFragmentManager(), OnboardingLivenessCheckFailDialogFragment.TAG);
    }

    @Override
    protected void onNoCameraPermission() {
        getActivity().setResult(OnboardingActivity.RESULT_NO_CAMERA_PERMISSION);
        getActivity().finish();
    }

    @Override
    protected void onNoFrontCamera() {
        getActivity().setResult(OnboardingActivity.RESULT_NO_FRONT_CAMERA);
        getActivity().finish();
    }

}
