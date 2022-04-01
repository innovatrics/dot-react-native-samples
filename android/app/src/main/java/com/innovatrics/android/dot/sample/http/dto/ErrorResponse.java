package com.innovatrics.android.dot.sample.http.dto;

import com.innovatrics.android.dot.sample.enums.ServerErrorCode;

import java.io.Serializable;

public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private ServerErrorCode errorCode;

    public ServerErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(final ServerErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
