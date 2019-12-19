package com.innovatrics.android.dot.sample.http.dto;

import java.io.Serializable;

public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private byte[] data;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(final byte[] data) {
        this.data = data;
    }

}
