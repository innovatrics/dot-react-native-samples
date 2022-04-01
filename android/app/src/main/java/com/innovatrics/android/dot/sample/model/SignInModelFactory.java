package com.innovatrics.android.dot.sample.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

public class SignInModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final String appServerUrl;
    private final String userId;

    public SignInModelFactory(final Application application, final String appServerUrl, final String userId) {
        this.application = application;
        this.appServerUrl = appServerUrl;
        this.userId = userId;
    }

    @NonNull
    @Override
    public SignInModel create(@NonNull final Class modelClass) {
        return new SignInModel(application, appServerUrl, userId);
    }

}
