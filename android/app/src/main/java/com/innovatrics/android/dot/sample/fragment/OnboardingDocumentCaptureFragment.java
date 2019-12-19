package com.innovatrics.android.dot.sample.fragment;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.innovatrics.android.dot.sample.activity.OnboardingActivity;
import com.innovatrics.android.dot.sample.documentcapture.DocumentSide;
import com.innovatrics.android.dot.sample.model.OnboardingModel;

public class OnboardingDocumentCaptureFragment extends DocumentCaptureFragment {

    private static final float DOCUMENT_WIDTH_RATIO_PORTRAIT = 0.9f;
    private static final float DOCUMENT_WIDTH_RATIO_LANDSCAPE = 0.6f;
    private static final float DOCUMENT_ASPECT_RATIO = 1.58577f;

    private OnboardingModel onboardingModel;

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        onboardingModel = ViewModelProviders.of(getActivity()).get(OnboardingModel.class);

        onboardingModel.getDocumentSideLiveData().observe(this, new Observer<DocumentSide>() {

            @Override
            public void onChanged(@Nullable final DocumentSide documentSide) {
                changeDocumentSide(documentSide);
            }

        });

        float documentWidthRatio;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            documentWidthRatio = DOCUMENT_WIDTH_RATIO_LANDSCAPE;
        } else {
            documentWidthRatio = DOCUMENT_WIDTH_RATIO_PORTRAIT;
        }

        onboardingModel.setDocumentWidthRatio(documentWidthRatio);
        onboardingModel.setDocumentAspectRatio(DOCUMENT_ASPECT_RATIO);

        setDocumentPlaceholderWidthRatio(documentWidthRatio);
        setDocumentPlaceholderAspect(DOCUMENT_ASPECT_RATIO);
    }

    @Override
    protected void onCameraInitFailed() {
        getActivity().setResult(OnboardingActivity.RESULT_CAMERA_INIT_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onPictureTaken(final byte[] bytes) {
        onboardingModel.processCapturedDocumentPageInBackground(bytes);
    }

}
