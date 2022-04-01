package com.innovatrics.android.dot.sample.dto;

import com.innovatrics.android.dot.sample.documentcapture.DocumentSide;
import com.innovatrics.android.dot.sample.documentreview.DocumentReviewedItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class OnboardingDocumentReviewedData implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<DocumentSide, String> uriStringMap;
    private List<DocumentReviewedItem> list;

    public Map<DocumentSide, String> getUriStringMap() {
        return uriStringMap;
    }

    public void setUriStringMap(final Map<DocumentSide, String> uriStringMap) {
        this.uriStringMap = uriStringMap;
    }

    public List<DocumentReviewedItem> getList() {
        return list;
    }

    public void setList(final List<DocumentReviewedItem> list) {
        this.list = list;
    }

}
