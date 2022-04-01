package com.innovatrics.android.dot.sample.model;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import com.innovatrics.dot.face.liveness.eyegaze.EyeGazeLivenessConfiguration;

public class LivenessCheckModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final EyeGazeLivenessConfiguration configuration;
    private final byte[] referenceTemplate;

    public LivenessCheckModelFactory(final Application application, final EyeGazeLivenessConfiguration configuration, final byte[] referenceTemplate) {
        this.application = application;
        this.configuration = configuration;
        this.referenceTemplate = referenceTemplate;
    }

    @NonNull
    @Override
    public LivenessCheckModel create(@NonNull final Class modelClass) {
        return new LivenessCheckModel(application, configuration, referenceTemplate);
    }

}