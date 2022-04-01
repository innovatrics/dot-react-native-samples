package com.innovatrics.android.dot.sample.http.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TextField implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @SerializedName("lines")
    private List<TextFieldLine> textFieldLineList;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public List<TextFieldLine> getTextFieldLineList() {
        return textFieldLineList;
    }

    public void setTextFieldLineList(final List<TextFieldLine> textFieldLineList) {
        this.textFieldLineList = textFieldLineList;
    }

}
