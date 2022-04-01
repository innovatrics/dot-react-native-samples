package com.innovatrics.android.dot.sample.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import com.innovatrics.android.dot.sample.fragment.DemoDocumentCaptureFragment;
import com.innovatrics.dot.document.autocapture.DocumentAutoCaptureConfiguration;
import com.innovatrics.dot.document.autocapture.DocumentAutoCaptureFragment;

public class DocumentCaptureActivity extends AppCompatActivity {

    public static final int RESULT_INTERRUPTED = 0;
    public static final int RESULT_CAMERA_MISSING_PERMISSIONS = 1;
    public static final int RESULT_SUCCESS = 2;

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

        final DocumentAutoCaptureConfiguration documentAutoCaptureConfiguration = new DocumentAutoCaptureConfiguration.Builder()
                .detectionLayerVisible(true).build();

        final Bundle arguments = new Bundle();
        arguments.putSerializable(DocumentAutoCaptureFragment.CONFIGURATION, documentAutoCaptureConfiguration);

        final Fragment fragment = new DemoDocumentCaptureFragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

}
