package com.innovatrics.android.dot.sample.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.innovatrics.android.dot.dto.FaceCaptureSimpleArguments;
import com.innovatrics.android.dot.fragment.FaceCaptureSimpleFragment;
import com.innovatrics.android.dot.sample.R;
import com.innovatrics.android.dot.sample.enums.ErrorCode;
import com.innovatrics.android.dot.sample.fragment.SignInErrorDialogFragment;
import com.innovatrics.android.dot.sample.fragment.SignInFaceCaptureSimpleFragment;
import com.innovatrics.android.dot.sample.model.SignInModel;
import com.innovatrics.android.dot.sample.model.SignInModelFactory;

public class SignInActivity extends AppCompatActivity {

    public static final String ARG_APP_SERVER_URL = "app_server_url";
    public static final String ARG_USER_ID = "user_id";
    public static final int RESULT_INTERRUPTED = 0;
    public static final int RESULT_CAMERA_INIT_FAILED = 1;
    public static final int RESULT_CAMERA_ACCESS_FAILED = 2;
    public static final int RESULT_NO_CAMERA_PERMISSION = 3;
    public static final int RESULT_SUCCESS = 4;
    public static final int RESULT_FAIL = 5;
    public static final String OUT_USERNAME = "username";

    private SignInModel signInModel;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_INTERRUPTED);

        String appServerUrl = null;
        String userId = null;

        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra(ARG_APP_SERVER_URL)) {
                appServerUrl = getIntent().getExtras().getString(ARG_APP_SERVER_URL);
            }
        }
        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra(ARG_USER_ID)) {
                userId = getIntent().getExtras().getString(ARG_USER_ID);
            }
        }

        final SignInModelFactory signInModelFactory = new SignInModelFactory(getApplication(), appServerUrl, userId);

        signInModel = ViewModelProviders.of(this, signInModelFactory).get(SignInModel.class);

        signInModel.getScreenLiveData().observe(this, new Observer<Integer>() {

            @Override
            public void onChanged(@Nullable final Integer screen) {
                switch (screen) {
                    case SignInModel.SCREEN_FACE_CAPTURE_SIMPLE:
                        setSignInFaceCaptureSimpleFragment();
                        break;
                }
            }

        });

        signInModel.getErrorEvent().observe(this, new Observer<ErrorCode>() {

            @Override
            public void onChanged(@Nullable final ErrorCode errorCode) {
                ErrorCode error = errorCode;
                if (errorCode == null) {
                    error = ErrorCode.SERVER_ERROR;
                }
                showErrorDialog(error);
            }

        });

        signInModel.getCancelEvent().observe(this, new Observer<Void>() {

            @Override
            public void onChanged(@Nullable final Void aVoid) {
                finish();
            }

        });

        signInModel.getSuccessEvent().observe(this, new Observer<String>() {

            @Override
            public void onChanged(@Nullable final String username) {
                final Intent intent = new Intent();
                intent.putExtra(OUT_USERNAME, username);

                setResult(RESULT_SUCCESS, intent);
                finish();
            }

        });

        signInModel.getFailEvent().observe(this, new Observer<Void>() {

            @Override
            public void onChanged(@Nullable final Void aVoid) {
                setResult(RESULT_FAIL);
                finish();
            }

        });
    }

    private void setSignInFaceCaptureSimpleFragment() {
        final FaceCaptureSimpleArguments faceCaptureSimpleArguments = new FaceCaptureSimpleArguments.Builder().build();

        final Bundle arguments = new Bundle();
        arguments.putSerializable(FaceCaptureSimpleFragment.ARGUMENTS, faceCaptureSimpleArguments);

        final Fragment fragment = new SignInFaceCaptureSimpleFragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

    private void showErrorDialog(final ErrorCode errorCode) {
        final Bundle arguments = new Bundle();
        arguments.putInt(SignInErrorDialogFragment.ARG_ICON_RESOURCE_ID, errorCode.getIconResourceId());
        arguments.putInt(SignInErrorDialogFragment.ARG_TITLE_RESOURCE_ID, R.string.error_title);
        arguments.putInt(SignInErrorDialogFragment.ARG_MESSAGE_RESOURCE_ID, errorCode.getMessageResourceId());

        final DialogFragment dialogFragment = new SignInErrorDialogFragment();
        dialogFragment.setArguments(arguments);
        dialogFragment.show(getSupportFragmentManager(), SignInErrorDialogFragment.TAG);
    }

}
