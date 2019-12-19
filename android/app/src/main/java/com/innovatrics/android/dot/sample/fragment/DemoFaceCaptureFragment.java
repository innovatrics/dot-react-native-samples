package com.innovatrics.android.dot.sample.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.innovatrics.android.dot.dto.Photo;
import com.innovatrics.android.dot.face.DetectedFace;
import com.innovatrics.android.dot.facecapture.steps.CaptureState;
import com.innovatrics.android.dot.fragment.FaceCaptureFragment;
import com.innovatrics.android.dot.sample.activity.FaceCaptureActivity;
import com.innovatrics.android.dot.sample.utils.Utils;

public class DemoFaceCaptureFragment extends FaceCaptureFragment {

    @Override
    protected void onCameraInitFailed() {
        getActivity().setResult(FaceCaptureActivity.RESULT_CAMERA_INIT_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onCameraAccessFailed() {
        getActivity().setResult(FaceCaptureActivity.RESULT_CAMERA_ACCESS_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onNoCameraPermission() {
        getActivity().setResult(FaceCaptureActivity.RESULT_NO_CAMERA_PERMISSION);
        getActivity().finish();
    }

    @Override
    protected void onCaptureStateChange(CaptureState captureState, Photo photo) {

    }

    @Override
    protected void onCaptureSuccess(final DetectedFace detectedFace) {
        final Bitmap bitmap = detectedFace.createCroppedImage();
        final Uri imageUri = Uri.fromFile(Utils.getTempFile(getContext()));
        com.innovatrics.android.dot.utils.Utils.saveBitmapAsJpeg(bitmap, imageUri);
        final byte[] template = detectedFace.createTemplate().getTemplate();

        final Intent intent = new Intent();
        intent.setData(imageUri);
        intent.putExtra(FaceCaptureActivity.OUT_TEMPLATE, template);

        getActivity().setResult(FaceCaptureActivity.RESULT_SUCCESS, intent);
        getActivity().finish();
    }

    @Override
    protected void onCaptureFail() {
        getActivity().setResult(FaceCaptureActivity.RESULT_FAIL);
        getActivity().finish();
    }

}
