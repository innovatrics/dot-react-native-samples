package com.innovatrics.android.dot.sample.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.innovatrics.android.dot.exception.TemplateVerifierException;
import com.innovatrics.android.dot.face.DetectedFace;
import com.innovatrics.android.dot.sample.dto.VerificationResult;
import com.innovatrics.android.dot.sample.utils.SingleLiveEvent;
import com.innovatrics.android.dot.verification.TemplateVerifier;

public class VerificationModel extends ViewModel {

    private static final String TAG = VerificationModel.class.getSimpleName();

    private static final float THRESHOLD = 40f;

    private final byte[] referenceTemplate;
    private final SingleLiveEvent<VerificationResult> verificationDoneEvent;
    private final TemplateVerifier templateVerifier;

    public VerificationModel(final byte[] referenceTemplate) {
        this.referenceTemplate = referenceTemplate;
        verificationDoneEvent = new SingleLiveEvent<>();
        templateVerifier = new TemplateVerifier();
    }

    public LiveData<VerificationResult> getVerificationDoneEvent() {
        return verificationDoneEvent;
    }

    public void verify(final DetectedFace detectedFace) {
        try {
            final byte[] probeTemplate = detectedFace.createTemplate().getTemplate();
            final float score = templateVerifier.match(probeTemplate, referenceTemplate);
            Log.i(TAG, "Verifying score is: " + score);

            if (score >= THRESHOLD) {
                verificationDoneEvent.postValue(new VerificationResult(VerificationResult.Event.VERIFIED, score));
            } else {
                verificationDoneEvent.postValue(new VerificationResult(VerificationResult.Event.NOT_VERIFIED, score));
            }
        } catch (final TemplateVerifierException e) {
            Log.e(TAG, "Exception while verification: " + e.getError(), e);
            verificationDoneEvent.postValue(new VerificationResult(VerificationResult.Event.NOT_VERIFIED));
        }
    }

}
