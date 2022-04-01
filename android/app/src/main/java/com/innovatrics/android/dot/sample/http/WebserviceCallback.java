package com.innovatrics.android.dot.sample.http;

import com.innovatrics.android.dot.sample.enums.ErrorCode;

public interface WebserviceCallback<RESPONSE> {

    void onResponse(RESPONSE response);

    void onError(ErrorCode errorCode);

}
