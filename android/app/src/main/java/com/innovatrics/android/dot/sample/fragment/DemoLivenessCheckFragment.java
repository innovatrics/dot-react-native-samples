package com.innovatrics.android.dot.sample.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import com.innovatrics.android.dot.sample.activity.LivenessCheckActivity;
import com.innovatrics.android.dot.sample.model.LivenessCheckModel;
import com.innovatrics.dot.face.liveness.eyegaze.EyeGazeLivenessFragment;
import com.innovatrics.dot.face.liveness.eyegaze.EyeGazeLivenessState;
import com.innovatrics.dot.face.liveness.eyegaze.SegmentImage;

import java.util.List;

public class DemoLivenessCheckFragment extends EyeGazeLivenessFragment {

    private LivenessCheckModel livenessCheckModel;

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        livenessCheckModel = ViewModelProviders.of(getActivity()).get(LivenessCheckModel.class);
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
        livenessCheckModel.livenessCheckDone(score, segmentImageList);
    }

    @Override
    protected void onNoMoreSegments() {
        getActivity().setResult(LivenessCheckActivity.RESULT_NO_MORE_SEGMENTS);
        getActivity().finish();
    }

    @Override
    protected void onEyesNotDetected() {
        getActivity().setResult(LivenessCheckActivity.RESULT_EYES_NOT_DETECTED);
        getActivity().finish();
    }

    @Override
    protected void onFaceTrackingFailed() {
        getActivity().setResult(LivenessCheckActivity.RESULT_FACE_TRACKING_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onStopped() {

    }

    @Override
    protected void onNoCameraPermission() {
        getActivity().setResult(LivenessCheckActivity.RESULT_NO_CAMERA_PERMISSION);
        getActivity().finish();
    }

}
