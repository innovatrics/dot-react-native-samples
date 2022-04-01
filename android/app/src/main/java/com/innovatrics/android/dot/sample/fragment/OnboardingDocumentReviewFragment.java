package com.innovatrics.android.dot.sample.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.innovatrics.android.dot.sample.documentreview.DocumentReviewedItem;
import com.innovatrics.android.dot.sample.model.OnboardingModel;

import java.util.List;

public class OnboardingDocumentReviewFragment extends DocumentReviewFragment {

    private OnboardingModel onboardingModel;

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onboardingModel = ViewModelProviders.of(getActivity()).get(OnboardingModel.class);
    }

    @Override
    protected void onReviewed(final List<DocumentReviewedItem> list) {
        onboardingModel.documentCaptureDone(list);
    }

}
