package com.innovatrics.android.dot.sample.http.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OnboardingUploadDocumentImagesRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("images")
    private List<Image> imageList;

    @SerializedName("modelMetadataIds")
    private List<String> modelMetadataIdList;

    private String mrzType;

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(final List<Image> imageList) {
        this.imageList = imageList;
    }

    public List<String> getModelMetadataIdList() {
        return modelMetadataIdList;
    }

    public void setModelMetadataIdList(final List<String> modelMetadataIdList) {
        this.modelMetadataIdList = modelMetadataIdList;
    }

    public String getMrzType() {
        return mrzType;
    }

    public void setMrzType(final String mrzType) {
        this.mrzType = mrzType;
    }

}
