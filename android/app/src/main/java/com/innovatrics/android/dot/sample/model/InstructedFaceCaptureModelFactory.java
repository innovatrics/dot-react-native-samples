package com.innovatrics.android.dot.sample.model;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

public class InstructedFaceCaptureModelFactory implements ViewModelProvider.Factory {

    private final Uri photoUri;

    public InstructedFaceCaptureModelFactory(final Uri photoUri) {
        this.photoUri = photoUri;
    }

    @NonNull
    @Override
    public InstructedFaceCaptureModel create(@NonNull final Class modelClass) {
        return new InstructedFaceCaptureModel(photoUri);
    }

}
