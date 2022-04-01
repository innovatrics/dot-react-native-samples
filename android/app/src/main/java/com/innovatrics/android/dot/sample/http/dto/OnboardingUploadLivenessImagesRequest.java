package com.innovatrics.android.dot.sample.http.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OnboardingUploadLivenessImagesRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("images")
    private List<byte[]> imageList;

    public List<byte[]> getImageList() {
        return imageList;
    }

    public void setImageList(final List<byte[]> imageList) {
        this.imageList = imageList;
    }

}
