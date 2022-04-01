package com.innovatrics.android.dot.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.innovatrics.android.dot.sample.dto.VerificationResult;
import com.innovatrics.android.dot.sample.fragment.VerificationFragment;
import com.innovatrics.android.dot.sample.model.VerificationModel;
import com.innovatrics.android.dot.sample.model.VerificationModelFactory;
import com.innovatrics.dot.face.simplecapture.FaceSimpleCaptureConfiguration;
import com.innovatrics.dot.face.simplecapture.FaceSimpleCaptureFragment;

public class VerificationActivity extends AppCompatActivity {

    public static final String ARG_REFERENCE_TEMPLATE = "reference_template";
    public static final int RESULT_INTERRUPTED = 0;
    public static final int RESULT_NO_CAMERA_PERMISSION = 3;
    public static final int RESULT_SUCCESS = 4;
    public static final int RESULT_FAIL = 5;
    public static final int RESULT_TEMPLATE_INCOMPATIBLE = 6;
    public static final String OUT_SCORE = "score";

    private VerificationModel verificationModel;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_INTERRUPTED);

        byte[] referenceTemplate = null;

        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra(ARG_REFERENCE_TEMPLATE)) {
                referenceTemplate = getIntent().getExtras().getByteArray(ARG_REFERENCE_TEMPLATE);
            }
        }

        final VerificationModelFactory verificationModelFactory = new VerificationModelFactory(referenceTemplate);

        verificationModel = ViewModelProviders.of(this, verificationModelFactory).get(VerificationModel.class);

        verificationModel.getVerificationDoneEvent().observe(this, new Observer<VerificationResult>() {

            @Override
            public void onChanged(@Nullable final VerificationResult verificationResult) {
                if (verificationResult == null) {
                    return;
                }
                if (verificationResult.getEvent() == null) {
                    return;
                }

                final Intent intent;

                switch (verificationResult.getEvent()) {
                    case VERIFIED:
                        intent = new Intent();
                        intent.putExtra(OUT_SCORE, (float) verificationResult.getScore());
                        setResult(RESULT_SUCCESS, intent);
                        finish();
                        break;
                    case NOT_VERIFIED:
                        intent = new Intent();
                        intent.putExtra(OUT_SCORE, (float) verificationResult.getScore());
                        setResult(RESULT_FAIL, intent);
                        finish();
                        break;
                    case TEMPLATE_INCOMPATIBLE:
                        setResult(RESULT_TEMPLATE_INCOMPATIBLE);
                        finish();
                        break;
                }
            }

        });

        setFragment();
    }

    private void setFragment() {
        if (getSupportFragmentManager().findFragmentById(android.R.id.content) != null) {
            return;
        }

        final FaceSimpleCaptureConfiguration faceSimpleCaptureConfiguration = new FaceSimpleCaptureConfiguration.Builder().build();

        final Bundle arguments = new Bundle();
        arguments.putSerializable(FaceSimpleCaptureFragment.CONFIGURATION, faceSimpleCaptureConfiguration);

        final Fragment fragment = new VerificationFragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

}
