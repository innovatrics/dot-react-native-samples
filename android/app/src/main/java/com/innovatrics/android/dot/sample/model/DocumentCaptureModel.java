package com.innovatrics.android.dot.sample.model;

import android.hardware.Camera;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.innovatrics.android.dot.camera.CameraController;
import com.innovatrics.android.dot.camera.FlashModes;
import com.innovatrics.android.dot.sample.documentcapture.DocumentSide;
import com.innovatrics.android.dot.sample.utils.SingleLiveEvent;

public class DocumentCaptureModel extends ViewModel {

    public static final int CAMERA_STATE_EVENT_OPEN_SUCCESS = 0;
    public static final int CAMERA_STATE_EVENT_OPEN_FAIL = 1;
    public static final int CAMERA_STATE_EVENT_RELEASE_SUCCESS = 2;
    public static final int CAMERA_STATE_EVENT_RELEASE_FAIL = 3;

    private final SingleLiveEvent<Integer> cameraStateEvent;
    private final MutableLiveData<FlashModes> flashModes;
    private final SingleLiveEvent<Void> shutterEvent;
    private final SingleLiveEvent<byte[]> pictureTakenEvent;
    private final MediatorLiveData<Boolean> captureAvailable;
    private final MutableLiveData<Boolean> cameraAvailable;
    private final MutableLiveData<DocumentSide> documentSide;
    private final CameraController.Listener cameraControllerListener;

    public DocumentCaptureModel() {
        cameraStateEvent = new SingleLiveEvent<>();
        flashModes = new MutableLiveData<>();
        shutterEvent = new SingleLiveEvent<>();
        pictureTakenEvent = new SingleLiveEvent<>();
        captureAvailable = new MediatorLiveData<>();
        cameraAvailable = new MutableLiveData<>();
        documentSide = new MutableLiveData<>();
        cameraAvailable.setValue(false);
        captureAvailable.addSource(cameraAvailable, new Observer<Boolean>() {

            @Override
            public void onChanged(@Nullable final Boolean aBoolean) {
                setCaptureAvailable();
            }

        });
        captureAvailable.addSource(documentSide, new Observer<DocumentSide>() {

            @Override
            public void onChanged(@Nullable final DocumentSide documentSide) {
                setCaptureAvailable();
            }

        });
        cameraControllerListener = new CameraController.Listener() {

            @Override
            public void onOpenSuccess() {
                cameraStateEvent.postValue(CAMERA_STATE_EVENT_OPEN_SUCCESS);
            }

            @Override
            public void onOpenFail() {
                cameraStateEvent.postValue(CAMERA_STATE_EVENT_OPEN_FAIL);
            }

            @Override
            public void onReleaseSuccess() {
                cameraStateEvent.postValue(CAMERA_STATE_EVENT_RELEASE_SUCCESS);
            }

            @Override
            public void onReleaseFail() {
                cameraStateEvent.postValue(CAMERA_STATE_EVENT_RELEASE_FAIL);
            }

            @Override
            public void onFlashModeSet(final FlashModes flashModes) {
                DocumentCaptureModel.this.flashModes.postValue(flashModes);
            }

            @Override
            public void onShutter() {
                shutterEvent.postValue(null);
            }

            @Override
            public void onPictureTaken(final byte[] data) {
                pictureTakenEvent.postValue(data);
            }

        };
    }

    public LiveData<Integer> getCameraStateEvent() {
        return cameraStateEvent;
    }

    public LiveData<FlashModes> getFlashModes() {
        return flashModes;
    }

    public LiveData<Void> getShutterEvent() {
        return shutterEvent;
    }

    public LiveData<byte[]> getPictureTakenEvent() {
        return pictureTakenEvent;
    }

    public void setCameraAvailable(boolean cameraAvailable) {
        this.cameraAvailable.setValue(cameraAvailable);
    }

    private void setCaptureAvailable() {
        if (cameraAvailable.getValue() != null && cameraAvailable.getValue() && documentSide.getValue() != null) {
            captureAvailable.setValue(true);
        } else {
            captureAvailable.setValue(false);
        }
    }

    public LiveData<Boolean> getCaptureAvailable() {
        return captureAvailable;
    }

    public void setDocumentSide(final DocumentSide documentSide) {
        this.documentSide.setValue(documentSide);
    }

    public void startCamera() {
        String flashMode = null;

        if (flashModes.getValue() != null) {
            flashMode = flashModes.getValue().getActive();
        }

        CameraController.getInstance().openInBackground(
                Camera.CameraInfo.CAMERA_FACING_BACK,
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE,
                flashMode,
                null,
                cameraControllerListener);
    }

    public void takePicture() {
        CameraController.getInstance().takePicture(cameraControllerListener);
    }

    public String nextCameraFlashMode() {
        final String flashMode = flashModes.getValue().next();
        CameraController.getInstance().setFlashMode(flashMode);
        return flashMode;
    }

}
