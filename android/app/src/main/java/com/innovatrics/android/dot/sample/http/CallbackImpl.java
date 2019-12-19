package com.innovatrics.android.dot.sample.http;

import android.util.Log;

import com.google.gson.Gson;
import com.innovatrics.android.dot.sample.enums.ErrorCode;
import com.innovatrics.android.dot.sample.http.dto.ErrorResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallbackImpl<RESPONSE> implements Callback<RESPONSE> {

    private static final String TAG = CallbackImpl.class.getSimpleName();

    private final WebserviceCallback<RESPONSE> webserviceCallback;
    private final Gson gson;

    public CallbackImpl(final WebserviceCallback<RESPONSE> webserviceCallback) {
        this.webserviceCallback = webserviceCallback;
        this.gson = new Gson();
    }

    @Override
    public void onResponse(final Call<RESPONSE> call, final Response<RESPONSE> response) {
        if (!response.isSuccessful()) {
            Log.e(TAG, "Response error: " + response.toString());
            final ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), ErrorResponse.class);
            Log.e(TAG, "Response error code: " + errorResponse.getErrorCode().name());
            final ErrorCode errorCode = ErrorCode.valueOf(errorResponse.getErrorCode().name());
            webserviceCallback.onError(errorCode);
            return;
        }

        webserviceCallback.onResponse(response.body());
    }

    @Override
    public void onFailure(final Call<RESPONSE> call, final Throwable t) {
        Log.e(TAG, "Webservice failed.", t);
        webserviceCallback.onError(ErrorCode.SERVER_ERROR);
    }

}
