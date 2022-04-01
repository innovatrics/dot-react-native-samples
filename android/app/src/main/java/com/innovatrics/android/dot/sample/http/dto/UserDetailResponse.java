package com.innovatrics.android.dot.sample.http.dto;

import java.io.Serializable;

public class UserDetailResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;
    private String givenNames;
    private String surname;

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getGivenNames() {
        return givenNames;
    }

    public void setGivenNames(final String givenNames) {
        this.givenNames = givenNames;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }

}
