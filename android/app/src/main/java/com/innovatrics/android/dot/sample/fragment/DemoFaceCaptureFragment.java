package com.innovatrics.android.dot.sample.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.innovatrics.android.dot.sample.activity.FaceCaptureActivity;
import com.innovatrics.android.dot.sample.utils.Bitmaps;
import com.innovatrics.android.dot.sample.utils.Utils;
import com.innovatrics.dot.face.autocapture.FaceAutoCaptureFragment;
import com.innovatrics.dot.face.autocapture.steps.CaptureStepId;
import com.innovatrics.dot.face.detection.DetectedFace;
import com.innovatrics.dot.face.image.BitmapFactory;

public class DemoFaceCaptureFragment extends FaceAutoCaptureFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        start();
    }

    @Override
    protected void onNoCameraPermission() {
        getActivity().setResult(FaceCaptureActivity.RESULT_NO_CAMERA_PERMISSION);
        getActivity().finish();
    }

    @Override
    protected void onStepChanged(CaptureStepId captureStepId, DetectedFace detectedFace) {

    }

    @Override
    protected void onCaptured(final DetectedFace detectedFace) {
        final Bitmap bitmap = BitmapFactory.create(detectedFace.createFullFrontalImage());
        final Uri imageUri = Uri.fromFile(Utils.getTempFile(getContext()));
        Bitmaps.saveBitmapAsJpeg(bitmap, imageUri);
        final byte[] template = detectedFace.createTemplate().getBytes();

        final Intent intent = new Intent();
        intent.setData(imageUri);
        intent.putExtra(FaceCaptureActivity.OUT_TEMPLATE, template);

        getActivity().setResult(FaceCaptureActivity.RESULT_SUCCESS, intent);
        getActivity().finish();
    }

    @Override
    protected void onStopped() {

    }
}
