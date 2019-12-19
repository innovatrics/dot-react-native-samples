package com.innovatrics.android.dot.sample.camera;

import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import com.innovatrics.android.dot.camera.CameraController;

public class SurfaceHolderCallback implements SurfaceHolder.Callback {

    private static final String TAG = SurfaceHolderCallback.class.getSimpleName();

    private final SurfaceHolder surfaceHolder;
    private final int displayOrientation;

    public SurfaceHolderCallback(final SurfaceHolder surfaceHolder, final int displayOrientation) {
        this.surfaceHolder = surfaceHolder;
        this.displayOrientation = displayOrientation;
    }

    @Override
    public void surfaceCreated(final SurfaceHolder surfaceHolder) {
    }

    @Override
    public void surfaceChanged(final SurfaceHolder surfaceHolder, final int i, final int width, final int height) {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        final Camera camera = CameraController.getInstance().getCamera();
        camera.setDisplayOrientation(displayOrientation);

        final Camera.Parameters parameters = camera.getParameters();
        parameters.setRotation(displayOrientation);
        camera.setParameters(parameters);

        startPreview();
    }

    @Override
    public void surfaceDestroyed(final SurfaceHolder surfaceHolder) {
        stopPreview();
    }

    private void startPreview() {
        Log.i(TAG, "Start preview.");

        try {
            CameraController.getInstance().getCamera().setPreviewDisplay(surfaceHolder);
            CameraController.getInstance().getCamera().startPreview();
        } catch (final Exception e) {
            Log.e(TAG, "Unable to start preview.", e);
        }
    }

    private void stopPreview() {
        Log.i(TAG, "Stop preview.");

        try {
            CameraController.getInstance().getCamera().stopPreview();
            CameraController.getInstance().getCamera().setPreviewCallback(null);
            CameraController.getInstance().releaseInBackground(null);
        } catch (final Exception e) {
            Log.e(TAG, "Unable to stop preview.", e);
        }
    }
}
