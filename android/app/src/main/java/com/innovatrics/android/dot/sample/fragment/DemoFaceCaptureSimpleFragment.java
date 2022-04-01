package com.innovatrics.android.dot.sample.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.innovatrics.android.dot.sample.activity.FaceCaptureSimpleActivity;
import com.innovatrics.android.dot.sample.utils.Bitmaps;
import com.innovatrics.dot.face.detection.DetectedFace;
import com.innovatrics.dot.face.image.BitmapFactory;
import com.innovatrics.dot.face.simplecapture.FaceSimpleCaptureFragment;

import java.io.File;
import java.io.IOException;

public class DemoFaceCaptureSimpleFragment extends FaceSimpleCaptureFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        start();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestCapture();
    }

    @Override
    protected void onNoCameraPermission() {
        getActivity().setResult(FaceCaptureSimpleActivity.RESULT_NO_CAMERA_PERMISSION);
        getActivity().finish();
    }

    @Override
    protected void onCaptured(final DetectedFace detectedFace) {
        final Bitmap bitmap = BitmapFactory.create(detectedFace.createFullFrontalImage());
        final Uri imageUri = Uri.fromFile(getTempFile());
        Bitmaps.saveBitmapAsJpeg(bitmap, imageUri);

        final Intent intent = new Intent();
        intent.setData(imageUri);

        getActivity().setResult(FaceCaptureSimpleActivity.RESULT_SUCCESS, intent);
        getActivity().finish();
    }

    @Override
    protected void onStopped() {

    }

    private File getTempFile() {
        try {
            return File.createTempFile("photo_", ".jpg", getContext().getCacheDir());
        } catch (final IOException e) {
            throw new RuntimeException("Unable to create photo file.");
        }
    }

}
