package com.innovatrics.android.dot.sample.fragment;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.innovatrics.android.dot.sample.activity.OnboardingActivity;
import com.innovatrics.android.dot.sample.documentcapture.DocumentSide;
import com.innovatrics.android.dot.sample.model.OnboardingModel;
import com.innovatrics.dot.document.autocapture.DocumentAutoCaptureDetection;
import com.innovatrics.dot.document.autocapture.DocumentAutoCaptureFragment;
import com.innovatrics.dot.document.autocapture.DocumentAutoCaptureResult;
import com.innovatrics.dot.document.image.BitmapFactory;

public class OnboardingDocumentCaptureFragment extends DocumentAutoCaptureFragment {

    private static final float DOCUMENT_WIDTH_RATIO_PORTRAIT = 0.9f;
    private static final float DOCUMENT_WIDTH_RATIO_LANDSCAPE = 0.6f;
    private static final float DOCUMENT_ASPECT_RATIO = 1.58577f;

    private OnboardingModel onboardingModel;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start();
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        onboardingModel = ViewModelProviders.of(getActivity()).get(OnboardingModel.class);

        onboardingModel.getDocumentSideLiveData().observe(this, new Observer<DocumentSide>() {

            @Override
            public void onChanged(@Nullable final DocumentSide documentSide) {
                restart();
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
    }

    @Override
    protected void onCandidateSelectionStarted() {

    }

    @Override
    protected void onCaptured(DocumentAutoCaptureResult documentAutoCaptureResult) {
        final Bitmap bitmap = BitmapFactory.create(documentAutoCaptureResult.getBgraRawImage());

        onboardingModel.processCapturedDocumentPageInBackground(bitmap);
    }

    @Override
    protected void onDetected(DocumentAutoCaptureDetection documentAutoCaptureDetection) {

    }

    @Override
    protected void onNoCameraPermission() {
        getActivity().setResult(OnboardingActivity.RESULT_NO_CAMERA_PERMISSION);
        getActivity().finish();
    }

}
