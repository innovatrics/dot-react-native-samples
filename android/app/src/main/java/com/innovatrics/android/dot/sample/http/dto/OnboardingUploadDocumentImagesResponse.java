package com.innovatrics.android.dot.sample.http.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OnboardingUploadDocumentImagesResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("pages")
    private List<DocumentPage> documentPageList;

    public List<DocumentPage> getDocumentPageList() {
        return documentPageList;
    }

    public void setDocumentPageList(final List<DocumentPage> documentPageList) {
        this.documentPageList = documentPageList;
    }

}
