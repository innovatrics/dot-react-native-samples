package com.innovatrics.android.dot.sample.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

public class VerificationModelFactory implements ViewModelProvider.Factory {

    private final byte[] referenceTemplate;

    public VerificationModelFactory(final byte[] referenceTemplate) {
        this.referenceTemplate = referenceTemplate;
    }

    @NonNull
    @Override
    public VerificationModel create(@NonNull final Class modelClass) {
        return new VerificationModel(referenceTemplate);
    }

}