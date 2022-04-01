package com.innovatrics.android.dot.sample.http.dto;

import java.io.Serializable;

public class SignInUploadFaceImageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(final byte[] image) {
        this.image = image;
    }

}
