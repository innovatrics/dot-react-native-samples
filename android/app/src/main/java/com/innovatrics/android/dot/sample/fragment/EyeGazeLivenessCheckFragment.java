package com.innovatrics.android.dot.sample.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import com.innovatrics.android.dot.sample.activity.EyeGazeLivenessCheckActivity;
import com.innovatrics.android.dot.sample.utils.Bitmaps;
import com.innovatrics.android.dot.sample.utils.Utils;
import com.innovatrics.dot.face.image.BitmapFactory;
import com.innovatrics.dot.face.liveness.eyegaze.EyeGazeLivenessFragment;
import com.innovatrics.dot.face.liveness.eyegaze.EyeGazeLivenessState;
import com.innovatrics.dot.face.liveness.eyegaze.SegmentImage;

import java.io.File;
import java.util.List;

public class EyeGazeLivenessCheckFragment extends EyeGazeLivenessFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start();
    }

    @Override
    protected void onNoCameraPermission() {
        getActivity().setResult(EyeGazeLivenessCheckActivity.RESULT_NO_CAMERA_PERMISSION);
        getActivity().finish();
    }

    @Override
    protected void onStateChanged(EyeGazeLivenessState eyeGazeLivenessState) {
        if (eyeGazeLivenessState == EyeGazeLivenessState.LOST_FACE) {
            start();
        }
    }

    @Override
    protected void onFinished(float score, List<SegmentImage> segmentImageList) {
        if (segmentImageList.size() == 0) {
            getActivity().setResult(EyeGazeLivenessCheckActivity.RESULT_FACE_CAPTURE_FAIL);
            getActivity().finish();
        }

        final Bitmap bitmap = BitmapFactory.create(segmentImageList.get(0).getImage());
        final File file = Utils.getTempFile(getContext());
        final Uri uri = Uri.fromFile(file);
        Bitmaps.saveBitmapAsJpeg(bitmap, uri);

        final Intent intent = new Intent();
        intent.setData(uri);
        intent.putExtra(EyeGazeLivenessCheckActivity.OUT_SCORE, score);

        getActivity().setResult(EyeGazeLivenessCheckActivity.RESULT_DONE, intent);
        getActivity().finish();
    }

    @Override
    protected void onNoMoreSegments() {
        getActivity().setResult(EyeGazeLivenessCheckActivity.RESULT_NO_MORE_SEGMENTS);
        getActivity().finish();
    }

    @Override
    protected void onEyesNotDetected() {
        getActivity().setResult(EyeGazeLivenessCheckActivity.RESULT_EYES_NOT_DETECTED);
        getActivity().finish();
    }

    @Override
    protected void onFaceTrackingFailed() {
        getActivity().setResult(EyeGazeLivenessCheckActivity.RESULT_FACE_TRACKING_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onStopped() {

    }
}
