package com.innovatrics.android.dot.sample.model;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.innovatrics.android.dot.sample.documentreview.DocumentItem;

import java.util.List;

public class DocumentReviewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final float dataConfidenceThreshold;
    private final Uri frontImageUri;
    private final Uri backImageUri;
    private final List<DocumentItem> visualInspectionZoneItemList;
    private final List<DocumentItem> machineReadableZoneItemList;

    public DocumentReviewModelFactory(
            final Application application,
            final float dataConfidenceThreshold,
            final Uri frontImageUri,
            final Uri backImageUri,
            final List<DocumentItem> visualInspectionZoneItemList,
            final List<DocumentItem> machineReadableZoneItemList) {
        this.application = application;
        this.dataConfidenceThreshold = dataConfidenceThreshold;
        this.frontImageUri = frontImageUri;
        this.backImageUri = backImageUri;
        this.visualInspectionZoneItemList = visualInspectionZoneItemList;
        this.machineReadableZoneItemList = machineReadableZoneItemList;
    }

    @NonNull
    @Override
    public DocumentReviewModel create(@NonNull final Class modelClass) {
        return new DocumentReviewModel(application, dataConfidenceThreshold, frontImageUri, backImageUri, visualInspectionZoneItemList, machineReadableZoneItemList);
    }

}
