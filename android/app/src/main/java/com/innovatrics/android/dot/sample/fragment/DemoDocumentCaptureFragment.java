package com.innovatrics.android.dot.sample.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.innovatrics.android.dot.sample.R;
import com.innovatrics.android.dot.sample.activity.DocumentCaptureActivity;
import com.innovatrics.android.dot.sample.documentcapture.DocumentSide;
import com.innovatrics.android.dot.utils.Bitmaps;
import com.innovatrics.android.dot.utils.Utils;

public class DemoDocumentCaptureFragment extends DocumentCaptureFragment {

    private final float DOCUMENT_WIDTH_RATIO = 0.8f;
    private final float DOCUMENT_WIDTH_TOLERANCE = 0.1f;
    private final float DOCUMENT_ASPECT_RATIO = 1.58577f;

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        changeDocumentSide(DocumentSide.FRONT);
        setDocumentPlaceholderAspect(DOCUMENT_ASPECT_RATIO);
        setDocumentPlaceholderWidthRatio(DOCUMENT_WIDTH_RATIO);
    }

    @Override
    protected void onCameraInitFailed() {
        getActivity().setResult(DocumentCaptureActivity.RESULT_CAMERA_INIT_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onPictureTaken(final byte[] bytes) {
        Bitmap bitmap = crop(Bitmaps.fromJpeg(bytes));
        Toast.makeText(
                getContext(),
                String.format(getString(R.string.document_capture_photo_captured_dimensions), bitmap.getWidth(), bitmap.getHeight()),
                Toast.LENGTH_SHORT)
                .show();

        final Uri imageUri = Uri.fromFile(com.innovatrics.android.dot.sample.utils.Utils.getTempFile(getContext()));
        Utils.saveBitmapAsJpeg(bitmap, imageUri);
        final Intent intent = new Intent();
        intent.setData(imageUri);

        getActivity().setResult(DocumentCaptureActivity.RESULT_SUCCESS, intent);
        getActivity().finish();
    }

    private Bitmap crop(final Bitmap bitmap) {
        final float activeWidthRatioExtended = Math.min(1.0f, DOCUMENT_WIDTH_RATIO + DOCUMENT_WIDTH_TOLERANCE);
        final int width = (int) (bitmap.getWidth() * activeWidthRatioExtended);
        final int height = (int) (width / DOCUMENT_ASPECT_RATIO);
        final int offsetX = (int) (bitmap.getWidth() * (1 - activeWidthRatioExtended)) / 2;
        final int offsetY = (bitmap.getHeight() - height) / 2;

        return Bitmap.createBitmap(bitmap, offsetX, offsetY, width, height);
    }
}
