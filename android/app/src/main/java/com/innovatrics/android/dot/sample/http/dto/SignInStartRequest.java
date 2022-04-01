package com.innovatrics.android.dot.sample.http.dto;

import java.io.Serializable;

public class SignInStartRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

}
