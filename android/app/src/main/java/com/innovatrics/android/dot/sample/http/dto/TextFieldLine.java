package com.innovatrics.android.dot.sample.http.dto;

import java.io.Serializable;

public class TextFieldLine implements Serializable {

    private static final long serialVersionUID = 1L;

    private String value;
    private float confidence;
    private Roi roi;

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(final float confidence) {
        this.confidence = confidence;
    }

    public Roi getRoi() {
        return roi;
    }

    public void setRoi(final Roi roi) {
        this.roi = roi;
    }

}
