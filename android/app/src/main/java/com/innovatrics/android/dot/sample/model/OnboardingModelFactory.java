package com.innovatrics.android.dot.sample.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

public class OnboardingModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final String appServerUrl;
    private final String userId;

    public OnboardingModelFactory(final Application application, final String appServerUrl, final String userId) {
        this.application = application;
        this.appServerUrl = appServerUrl;
        this.userId = userId;
    }

    @NonNull
    @Override
    public OnboardingModel create(@NonNull final Class modelClass) {
        return new OnboardingModel(application, appServerUrl, userId);
    }

}
