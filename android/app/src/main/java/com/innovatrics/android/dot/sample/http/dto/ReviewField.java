package com.innovatrics.android.dot.sample.http.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ReviewField implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @SerializedName("lines")
    private List<String> lineList;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public List<String> getLineList() {
        return lineList;
    }

    public void setLineList(final List<String> lineList) {
        this.lineList = lineList;
    }

}
