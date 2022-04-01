package com.innovatrics.android.dot.sample.model;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.innovatrics.android.dot.sample.utils.Bitmaps;
import com.innovatrics.android.dot.sample.utils.SingleLiveEvent;
import com.innovatrics.dot.face.image.BgrRawImage;
import com.innovatrics.dot.face.image.BitmapFactory;
import com.innovatrics.dot.face.image.FaceImage;
import com.innovatrics.dot.face.image.FaceImageFactory;
import com.innovatrics.dot.face.liveness.eyegaze.EyeGazeLivenessConfiguration;
import com.innovatrics.dot.face.liveness.eyegaze.SegmentImage;
import com.innovatrics.dot.face.similarity.FaceMatcher;
import com.innovatrics.dot.face.similarity.FaceMatcherFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LivenessCheckModel extends AndroidViewModel {

    private static final String TAG = LivenessCheckModel.class.getSimpleName();

    private static final float LIVENESS_CHECK_THRESHOLD = 0.9999781847f;

    private final EyeGazeLivenessConfiguration configuration;
    private final byte[] referenceTemplate;
    private final SingleLiveEvent<Void> livenessCheckSuccessEvent;
    private final SingleLiveEvent<Void> livenessCheckFailEvent;
    private final FaceMatcher faceMatcher;

    public LivenessCheckModel(@NonNull final Application application, final EyeGazeLivenessConfiguration configuration, final byte[] referenceTemplate) {
        super(application);

        this.configuration = configuration;
        this.referenceTemplate = referenceTemplate;

        this.livenessCheckSuccessEvent = new SingleLiveEvent<>();
        this.livenessCheckFailEvent = new SingleLiveEvent<>();
        this.faceMatcher = FaceMatcherFactory.create();
    }

    public LiveData<Void> getLivenessCheckSuccessEvent() {
        return livenessCheckSuccessEvent;
    }

    public LiveData<Void> getLivenessCheckFailEvent() {
        return livenessCheckFailEvent;
    }

    public void livenessCheckDone(final float score, final List<SegmentImage> segmentImageList) {
        final long timestamp = System.currentTimeMillis();

        for (int i = 0; i < segmentImageList.size(); i++) {
            final BgrRawImage photo = segmentImageList.get(i).getImage();

            if (photo != null) {
                final Bitmap bitmap = BitmapFactory.create(photo);
                final File file = new File(getApplication().getFilesDir(), "photo_" + timestamp + "_" + i);
                final Uri uri = Uri.fromFile(file);
                Bitmaps.saveBitmapAsJpeg(bitmap, uri);
            }
        }

        if (score < LIVENESS_CHECK_THRESHOLD) {
            livenessCheckFailEvent.postValue(null);
            return;
        }

        if (referenceTemplate != null) {
            final List<FaceImage> faceImageList = new ArrayList<>();

            for (final SegmentImage segmentImage : segmentImageList) {
                final BgrRawImage photo = segmentImage.getImage();

                if (photo != null) {
                    final FaceImage faceImage = FaceImageFactory.create(photo, 0.03,0.5);
                    faceImageList.add(faceImage);
                }
            }

            try {
                final List<Double> scoreList = new ArrayList<>();

                for (final FaceImage faceImage : faceImageList) {
                    final FaceMatcher.Result result = faceMatcher.match(referenceTemplate, faceImage);
                    scoreList.add(result.getScore());
                }
                Log.i(TAG, "Matching scores are: " + scoreList);
            } catch (final FaceMatcher.MatchException e) {
                Log.e(TAG, "Exception while verification: " + e.getError(), e);
                livenessCheckFailEvent.postValue(null);
                return;
            }
        }

        livenessCheckSuccessEvent.postValue(null);
    }

}
