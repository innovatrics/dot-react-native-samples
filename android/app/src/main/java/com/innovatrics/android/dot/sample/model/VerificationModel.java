package com.innovatrics.android.dot.sample.model;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.innovatrics.android.dot.sample.dto.VerificationResult;
import com.innovatrics.android.dot.sample.utils.SingleLiveEvent;
import com.innovatrics.dot.face.detection.DetectedFace;
import com.innovatrics.dot.face.similarity.TemplateMatcher;
import com.innovatrics.dot.face.similarity.TemplateMatcherFactory;

public class VerificationModel extends ViewModel {

    private static final String TAG = VerificationModel.class.getSimpleName();

    private static final float THRESHOLD = 0.40f;

    private final byte[] referenceTemplate;
    private final SingleLiveEvent<VerificationResult> verificationDoneEvent;
    private final TemplateMatcher templateMatcher;

    public VerificationModel(final byte[] referenceTemplate) {
        this.referenceTemplate = referenceTemplate;
        verificationDoneEvent = new SingleLiveEvent<>();
        templateMatcher = TemplateMatcherFactory.create();
    }

    public LiveData<VerificationResult> getVerificationDoneEvent() {
        return verificationDoneEvent;
    }

    public void verify(final DetectedFace detectedFace) {
        try {
            final byte[] probeTemplate = detectedFace.createTemplate().getBytes();
            final TemplateMatcher.Result result = templateMatcher.match(probeTemplate, referenceTemplate);
            final double score = result.getScore();
            Log.i(TAG, "Verifying score is: " + score);

            if (score >= THRESHOLD) {
                verificationDoneEvent.postValue(new VerificationResult(VerificationResult.Event.VERIFIED, score));
            } else {
                verificationDoneEvent.postValue(new VerificationResult(VerificationResult.Event.NOT_VERIFIED, score));
            }
        } catch (final TemplateMatcher.MatchException e) {
            Log.e(TAG, "Exception while verification: " + e.getError(), e);
            verificationDoneEvent.postValue(new VerificationResult(VerificationResult.Event.NOT_VERIFIED));
        }
    }

}
