package com.innovatrics.android.dot.sample.http.dto;

import java.io.Serializable;

public class MrzField implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String value;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
