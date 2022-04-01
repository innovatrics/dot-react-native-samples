package com.innovatrics.android.dot.sample.model;

import android.graphics.Bitmap;
import android.net.Uri;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.innovatrics.android.dot.sample.utils.Bitmaps;
import com.innovatrics.android.dot.sample.utils.SingleLiveEvent;
import com.innovatrics.dot.face.detection.DetectedFace;
import com.innovatrics.dot.face.image.BitmapFactory;

public class InstructedFaceCaptureModel extends ViewModel {

    private final Uri photoUri;
    private final SingleLiveEvent<Void> prepareToCaptureEvent;
    private final SingleLiveEvent<Integer> faceCaptureEvent;
    private final SingleLiveEvent<Void> submitPhotoEvent;

    public InstructedFaceCaptureModel(final Uri photoUri) {
        this.photoUri = photoUri;

        prepareToCaptureEvent = new SingleLiveEvent<>();
        faceCaptureEvent = new SingleLiveEvent<>();
        submitPhotoEvent = new SingleLiveEvent<>();
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public LiveData<Void> getPrepareToCaptureEvent() {
        return prepareToCaptureEvent;
    }

    public LiveData<Integer> getFaceCaptureEvent() {
        return faceCaptureEvent;
    }

    public LiveData<Void> getSubmitPhotoEvent() {
        return submitPhotoEvent;
    }

    public void prepareAndStartCapture() {
        prepareToCaptureEvent.call();
    }

    public void callFaceCaptureEvent(final int result, final DetectedFace detectedFace) {
        if (detectedFace != null) {
            final Bitmap bitmap = BitmapFactory.create(detectedFace.createFullFrontalImage());
            Bitmaps.saveBitmapAsJpeg(bitmap, photoUri);
        }

        faceCaptureEvent.setValue(result);
    }

    public void submitPhoto() {
        submitPhotoEvent.call();
    }

}
