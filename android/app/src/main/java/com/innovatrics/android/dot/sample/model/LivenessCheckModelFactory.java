package com.innovatrics.android.dot.sample.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.innovatrics.android.dot.dto.LivenessCheckArguments;

public class LivenessCheckModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final LivenessCheckArguments arguments;
    private final byte[] referenceTemplate;

    public LivenessCheckModelFactory(final Application application, final LivenessCheckArguments arguments, final byte[] referenceTemplate) {
        this.application = application;
        this.arguments = arguments;
        this.referenceTemplate = referenceTemplate;
    }

    @NonNull
    @Override
    public LivenessCheckModel create(@NonNull final Class modelClass) {
        return new LivenessCheckModel(application, arguments, referenceTemplate);
    }

}