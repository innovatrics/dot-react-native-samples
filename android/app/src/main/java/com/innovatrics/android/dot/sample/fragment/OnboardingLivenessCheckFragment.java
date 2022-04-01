package com.innovatrics.android.dot.sample.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import com.innovatrics.android.dot.sample.activity.OnboardingActivity;
import com.innovatrics.android.dot.sample.model.OnboardingModel;
import com.innovatrics.dot.face.liveness.eyegaze.EyeGazeLivenessFragment;
import com.innovatrics.dot.face.liveness.eyegaze.EyeGazeLivenessState;
import com.innovatrics.dot.face.liveness.eyegaze.SegmentImage;

import java.util.List;

public class OnboardingLivenessCheckFragment extends EyeGazeLivenessFragment {

    private OnboardingModel onboardingModel;

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onboardingModel = ViewModelProviders.of(getActivity()).get(OnboardingModel.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start();
    }

    @Override
    protected void onStateChanged(EyeGazeLivenessState eyeGazeLivenessState) {
        if (eyeGazeLivenessState == EyeGazeLivenessState.LOST_FACE) {
            start();
        }
    }

    @Override
    protected void onFinished(float score, List<SegmentImage> segmentImageList) {
        onboardingModel.livenessCheckDone(score, segmentImageList);
    }

    @Override
    protected void onNoMoreSegments() {
        new OnboardingLivenessCheckFailDialogFragment().show(getChildFragmentManager(), OnboardingLivenessCheckFailDialogFragment.TAG);
    }

    @Override
    protected void onEyesNotDetected() {
        new OnboardingLivenessCheckFailDialogFragment().show(getChildFragmentManager(), OnboardingLivenessCheckFailDialogFragment.TAG);
    }

    @Override
    protected void onFaceTrackingFailed() {

    }

    @Override
    protected void onStopped() {

    }

    @Override
    protected void onNoCameraPermission() {
        getActivity().setResult(OnboardingActivity.RESULT_NO_CAMERA_PERMISSION);
        getActivity().finish();
    }
}
