package com.innovatrics.android.dot.sample.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.innovatrics.android.dot.sample.fragment.EyeGazeLivenessCheckFragment;
import com.innovatrics.dot.face.liveness.eyegaze.EyeGazeLivenessConfiguration;
import com.innovatrics.dot.face.liveness.eyegaze.EyeGazeLivenessFragment;

public class EyeGazeLivenessCheckActivity extends AppCompatActivity {

    public static final String ARGUMENTS = "arguments";
    public static final String OUT_SCORE = "score";
    public static final int RESULT_INTERRUPTED = 0;
    public static final int RESULT_NO_CAMERA_PERMISSION = 3;
    public static final int RESULT_FACE_CAPTURE_FAIL = 4;
    public static final int RESULT_FACE_TRACKING_FAILED = 5;
    public static final int RESULT_NO_MORE_SEGMENTS = 6;
    public static final int RESULT_EYES_NOT_DETECTED = 7;
    public static final int RESULT_DONE = 8;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EyeGazeLivenessConfiguration configuration = null;

        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra(ARGUMENTS)) {
                configuration = (EyeGazeLivenessConfiguration) getIntent().getExtras().getSerializable(ARGUMENTS);
            }
        }

        setResult(RESULT_INTERRUPTED);
        setFragment(configuration);
    }

    private void setFragment(final EyeGazeLivenessConfiguration eyeGazeLivenessConfiguration) {
        if (getSupportFragmentManager().findFragmentById(android.R.id.content) != null) {
            return;
        }

        final Bundle arguments = new Bundle();
        arguments.putSerializable(EyeGazeLivenessFragment.CONFIGURATION, eyeGazeLivenessConfiguration);

        final Fragment fragment = new EyeGazeLivenessCheckFragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

}
