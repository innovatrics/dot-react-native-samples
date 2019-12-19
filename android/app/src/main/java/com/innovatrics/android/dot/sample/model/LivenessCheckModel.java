package com.innovatrics.android.dot.sample.model;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.innovatrics.android.dot.dto.LivenessCheckArguments;
import com.innovatrics.android.dot.dto.Photo;
import com.innovatrics.android.dot.exception.FaceImageVerifierException;
import com.innovatrics.android.dot.face.FaceImage;
import com.innovatrics.android.dot.livenesscheck.liveness.SegmentPhoto;
import com.innovatrics.android.dot.sample.utils.SingleLiveEvent;
import com.innovatrics.android.dot.utils.Utils;
import com.innovatrics.android.dot.verification.FaceImageVerifier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LivenessCheckModel extends AndroidViewModel {

    private static final String TAG = LivenessCheckModel.class.getSimpleName();

    private static final float LIVENESS_CHECK_THRESHOLD = 0.9999781847f;

    private final LivenessCheckArguments arguments;
    private final byte[] referenceTemplate;
    private final SingleLiveEvent<Void> livenessCheckSuccessEvent;
    private final SingleLiveEvent<Void> livenessCheckFailEvent;
    private final FaceImageVerifier faceImageVerifier;

    public LivenessCheckModel(@NonNull final Application application, final LivenessCheckArguments arguments, final byte[] referenceTemplate) {
        super(application);

        this.arguments = arguments;
        this.referenceTemplate = referenceTemplate;

        this.livenessCheckSuccessEvent = new SingleLiveEvent<>();
        this.livenessCheckFailEvent = new SingleLiveEvent<>();
        this.faceImageVerifier = new FaceImageVerifier();
    }

    public LiveData<Void> getLivenessCheckSuccessEvent() {
        return livenessCheckSuccessEvent;
    }

    public LiveData<Void> getLivenessCheckFailEvent() {
        return livenessCheckFailEvent;
    }

    public void livenessCheckDone(final float score, final List<SegmentPhoto> segmentPhotoList) {
        final long timestamp = System.currentTimeMillis();

        for (int i = 0; i < segmentPhotoList.size(); i++) {
            final Photo photo = segmentPhotoList.get(i).getPhoto();

            if (photo != null) {
                final Bitmap bitmap = photo.toBitmap();
                final File file = new File(getApplication().getFilesDir(), "photo_" + timestamp + "_" + i);
                final Uri uri = Uri.fromFile(file);
                Utils.saveBitmapAsJpeg(bitmap, uri);
            }
        }

        if (score < LIVENESS_CHECK_THRESHOLD) {
            livenessCheckFailEvent.postValue(null);
            return;
        }

        if (referenceTemplate != null) {
            final List<FaceImage> faceImageList = new ArrayList<>();

            for (final SegmentPhoto segmentPhoto : segmentPhotoList) {
                final Photo photo = segmentPhoto.getPhoto();

                if (photo != null) {
                    final Bitmap bitmap = photo.toBitmap();
                    final FaceImage faceImage = FaceImage.create(bitmap, arguments.getMinFaceSizeRatio(), arguments.getMaxFaceSizeRatio());
                    faceImageList.add(faceImage);
                }
            }

            try {
                final List<Float> scoreList = faceImageVerifier.match(referenceTemplate, faceImageList);
                Log.i(TAG, "Matching scores are: " + scoreList);
            } catch (final FaceImageVerifierException e) {
                Log.e(TAG, "Exception while verification: " + e.getError(), e);
                livenessCheckFailEvent.postValue(null);
                return;
            }
        }

        livenessCheckSuccessEvent.postValue(null);
    }

}
