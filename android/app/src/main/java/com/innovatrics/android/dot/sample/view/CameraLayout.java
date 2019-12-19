package com.innovatrics.android.dot.sample.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.innovatrics.android.dot.camera.CameraController;
import com.innovatrics.android.dot.sample.camera.SurfaceHolderCallback;
import com.innovatrics.android.dot.utils.CameraUtils;

@SuppressLint("ViewConstructor")
public class CameraLayout extends ViewGroup {

    private final Camera.Size previewSize;
    private final SurfaceView surfaceView;

    public CameraLayout(final Context context, final int id, final int cameraFacing) {
        super(context);
        setId(id);

        previewSize = CameraController.getInstance().getCamera().getParameters().getPreviewSize();
        surfaceView = new SurfaceView(context);

        final int previewRotationCompensation = CameraUtils.calculatePreviewRotationCompensation(getContext(), cameraFacing);
        final SurfaceHolderCallback callback = new SurfaceHolderCallback(surfaceView.getHolder(), previewRotationCompensation);
        surfaceView.getHolder().addCallback(callback);
        addView(surfaceView);
    }

    @Override
    protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
        final float previewAspectRatio = (float) previewSize.width / (float) previewSize.height;

        int surfaceViewLeft;
        int surfaceViewTop;
        int surfaceViewRight;
        int surfaceViewBottom;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            final int surfaceViewHeight = (int) (getWidth() * previewAspectRatio);

            surfaceViewLeft = left;
            surfaceViewTop = top + (getHeight() - surfaceViewHeight) / 2;
            surfaceViewRight = right;
            surfaceViewBottom = surfaceViewTop + surfaceViewHeight;
        } else {
            final int surfaceViewWidth = (int) (getHeight() * previewAspectRatio);

            surfaceViewLeft = left + (getWidth() - surfaceViewWidth) / 2;
            surfaceViewTop = top;
            surfaceViewRight = surfaceViewLeft + surfaceViewWidth;
            surfaceViewBottom = bottom;
        }

        surfaceView.layout(surfaceViewLeft, surfaceViewTop, surfaceViewRight, surfaceViewBottom);
    }

}
