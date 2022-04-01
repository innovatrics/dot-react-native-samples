package com.innovatrics.android.dot.sample.http.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DocumentPage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String modelMetadataId;
    private String prediction;

    @SerializedName("textFields")
    private List<TextField> textFieldList;

    @SerializedName("mrzFields")
    private List<MrzField> mrzFieldList;

    private String errorName;

    public String getModelMetadataId() {
        return modelMetadataId;
    }

    public void setModelMetadataId(final String modelMetadataId) {
        this.modelMetadataId = modelMetadataId;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(final String prediction) {
        this.prediction = prediction;
    }

    public List<TextField> getTextFieldList() {
        return textFieldList;
    }

    public void setTextFieldList(final List<TextField> textFieldList) {
        this.textFieldList = textFieldList;
    }

    public List<MrzField> getMrzFieldList() {
        return mrzFieldList;
    }

    public void setMrzFieldList(final List<MrzField> mrzFieldList) {
        this.mrzFieldList = mrzFieldList;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(final String errorName) {
        this.errorName = errorName;
    }

}
