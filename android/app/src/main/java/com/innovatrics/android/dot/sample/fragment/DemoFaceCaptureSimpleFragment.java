package com.innovatrics.android.dot.sample.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.innovatrics.android.dot.face.DetectedFace;
import com.innovatrics.android.dot.fragment.FaceCaptureSimpleFragment;
import com.innovatrics.android.dot.sample.activity.FaceCaptureSimpleActivity;
import com.innovatrics.android.dot.utils.Utils;

import java.io.File;
import java.io.IOException;

public class DemoFaceCaptureSimpleFragment extends FaceCaptureSimpleFragment {

    @Override
    public void onResume() {
        super.onResume();
        requestPhoto();
    }

    @Override
    protected void onCameraInitFailed() {
        getActivity().setResult(FaceCaptureSimpleActivity.RESULT_CAMERA_INIT_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onCameraAccessFailed() {
        getActivity().setResult(FaceCaptureSimpleActivity.RESULT_CAMERA_ACCESS_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onNoCameraPermission() {
        getActivity().setResult(FaceCaptureSimpleActivity.RESULT_NO_CAMERA_PERMISSION);
        getActivity().finish();
    }

    @Override
    protected void onCapture(final DetectedFace detectedFace) {
        final Bitmap bitmap = detectedFace.createFullImage();
        final Uri imageUri = Uri.fromFile(getTempFile());
        Utils.saveBitmapAsJpeg(bitmap, imageUri);

        final Intent intent = new Intent();
        intent.setData(imageUri);

        getActivity().setResult(FaceCaptureSimpleActivity.RESULT_SUCCESS, intent);
        getActivity().finish();
    }

    private File getTempFile() {
        try {
            return File.createTempFile("photo_", ".jpg", getContext().getCacheDir());
        } catch (final IOException e) {
            throw new RuntimeException("Unable to create photo file.");
        }
    }

}
