package com.innovatrics.android.dot.sample.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.innovatrics.android.dot.dto.Photo;
import com.innovatrics.android.dot.face.DetectedFace;
import com.innovatrics.android.dot.facecapture.steps.CaptureState;
import com.innovatrics.android.dot.fragment.LivenessCheck2Fragment;
import com.innovatrics.android.dot.livenesscheck.controller.FaceLivenessState;
import com.innovatrics.android.dot.livenesscheck.liveness.SegmentPhoto;
import com.innovatrics.android.dot.sample.activity.LivenessCheck2Activity;
import com.innovatrics.android.dot.sample.utils.Utils;

import java.io.File;
import java.util.List;

public class DemoLivenessCheck2Fragment extends LivenessCheck2Fragment {

    private static final String TAG = DemoLivenessCheck2Fragment.class.getSimpleName();

    private DetectedFace detectedFace;

    @Override
    protected void onCameraInitFail() {
        getActivity().setResult(LivenessCheck2Activity.RESULT_CAMERA_INIT_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onCameraAccessFailed() {
        getActivity().setResult(LivenessCheck2Activity.RESULT_CAMERA_ACCESS_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onNoCameraPermission() {
        getActivity().setResult(LivenessCheck2Activity.RESULT_NO_CAMERA_PERMISSION);
        getActivity().finish();
    }

    @Override
    protected void onCaptureStateChange(CaptureState captureState, Photo photo) {
        Log.i(TAG, "CaptureState: " + captureState);
    }

    @Override
    protected void onCaptureSuccess(final DetectedFace detectedFace) {
        this.detectedFace = detectedFace;
    }

    @Override
    protected void onFaceCaptureFail() {
        getActivity().setResult(LivenessCheck2Activity.RESULT_FACE_CAPTURE_FAIL);
        getActivity().finish();
    }

    @Override
    protected void onLivenessStateChange(final FaceLivenessState faceLivenessState) {
        if (faceLivenessState == FaceLivenessState.LOST) {
            startLivenessCheck();
        }
    }

    @Override
    protected void onLivenessCheckDone(final float score, final List<SegmentPhoto> keyFrameList) {
        final Bitmap bitmap = detectedFace.createCroppedImage();
        final File file = Utils.getTempFile(getContext());
        final Uri uri = Uri.fromFile(file);
        com.innovatrics.android.dot.utils.Utils.saveBitmapAsJpeg(bitmap, uri);

        final Intent intent = new Intent();
        intent.setData(uri);
        intent.putExtra(LivenessCheck2Activity.OUT_SCORE, score);

        getActivity().setResult(LivenessCheck2Activity.RESULT_DONE, intent);
        getActivity().finish();
    }

    @Override
    protected void onLivenessCheckFailNoMoreSegments() {
        getActivity().setResult(LivenessCheck2Activity.RESULT_NO_MORE_SEGMENTS);
        getActivity().finish();
    }

    @Override
    protected void onLivenessCheckFailEyesNotDetected() {
        getActivity().setResult(LivenessCheck2Activity.RESULT_EYES_NOT_DETECTED);
        getActivity().finish();
    }

}
