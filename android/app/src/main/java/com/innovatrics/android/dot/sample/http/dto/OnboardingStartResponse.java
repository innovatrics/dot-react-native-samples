package com.innovatrics.android.dot.sample.http.dto;

import java.io.Serializable;

public class OnboardingStartResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String bearerToken;

    public String getBearerToken() {
        return bearerToken;
    }

    public void setBearerToken(final String bearerToken) {
        this.bearerToken = bearerToken;
    }

}
