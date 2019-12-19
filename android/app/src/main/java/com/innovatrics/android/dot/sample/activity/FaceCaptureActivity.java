package com.innovatrics.android.dot.sample.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.innovatrics.android.dot.dto.FaceCaptureArguments;
import com.innovatrics.android.dot.fragment.FaceCaptureFragment;
import com.innovatrics.android.dot.sample.fragment.DemoFaceCaptureFragment;

public class FaceCaptureActivity extends AppCompatActivity {

    public static final int RESULT_INTERRUPTED = 0;
    public static final int RESULT_CAMERA_INIT_FAILED = 1;
    public static final int RESULT_CAMERA_ACCESS_FAILED = 2;
    public static final int RESULT_NO_CAMERA_PERMISSION = 3;
    public static final int RESULT_SUCCESS = 4;
    public static final int RESULT_FAIL = 5;
    public static final String OUT_TEMPLATE = "template";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_INTERRUPTED);
        setFragment();
    }

    private void setFragment() {
        if (getSupportFragmentManager().findFragmentById(android.R.id.content) != null) {
            return;
        }

        final FaceCaptureArguments faceCaptureArguments = new FaceCaptureArguments.Builder()
                .lightScoreThreshold(0.4d)
                .build();

        final Bundle arguments = new Bundle();
        arguments.putSerializable(FaceCaptureFragment.ARGUMENTS, faceCaptureArguments);

        final Fragment fragment = new DemoFaceCaptureFragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

}
