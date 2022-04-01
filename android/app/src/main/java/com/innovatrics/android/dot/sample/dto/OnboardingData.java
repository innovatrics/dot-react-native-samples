package com.innovatrics.android.dot.sample.dto;

import com.innovatrics.android.dot.sample.documentcapture.DocumentSide;
import com.innovatrics.android.dot.sample.documentreview.DocumentReviewedItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class OnboardingData implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<DocumentSide, String> uriStringMap;
    private final String documentFaceUriString;
    private final String faceCaptureUriString;
    private final List<DocumentReviewedItem> list;

    public OnboardingData(final Map<DocumentSide, String> uriStringMap, final String documentFaceUriString, final String faceCaptureUriString, final List<DocumentReviewedItem> list) {
        this.uriStringMap = uriStringMap;
        this.documentFaceUriString = documentFaceUriString;
        this.faceCaptureUriString = faceCaptureUriString;
        this.list = list;
    }

    public Map<DocumentSide, String> getUriStringMap() {
        return uriStringMap;
    }

    public String getDocumentFaceUriString() {
        return documentFaceUriString;
    }

    public String getFaceCaptureUriString() {
        return faceCaptureUriString;
    }

    public List<DocumentReviewedItem> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "OnboardingData{" +
                "uriStringMap='" + uriStringMap + '\'' +
                ", documentFaceUriString='" + documentFaceUriString + '\'' +
                ", faceCaptureUriString='" + faceCaptureUriString + '\'' +
                ", list=" + list +
                '}';
    }

}
