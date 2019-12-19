package com.innovatrics.android.dot.sample.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.innovatrics.android.dot.dto.LivenessCheck2Arguments;
import com.innovatrics.android.dot.fragment.LivenessCheck2Fragment;
import com.innovatrics.android.dot.sample.fragment.DemoLivenessCheck2Fragment;

public class LivenessCheck2Activity extends AppCompatActivity {

    public static final String ARGUMENTS = "arguments";
    public static final String OUT_SCORE = "score";
    public static final int RESULT_INTERRUPTED = 0;
    public static final int RESULT_CAMERA_INIT_FAILED = 1;
    public static final int RESULT_CAMERA_ACCESS_FAILED = 2;
    public static final int RESULT_NO_CAMERA_PERMISSION = 3;
    public static final int RESULT_FACE_CAPTURE_FAIL = 4;
    public static final int RESULT_NO_MORE_SEGMENTS = 5;
    public static final int RESULT_EYES_NOT_DETECTED = 6;
    public static final int RESULT_DONE = 7;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LivenessCheck2Arguments arguments = null;

        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra(ARGUMENTS)) {
                arguments = (LivenessCheck2Arguments) getIntent().getExtras().getSerializable(ARGUMENTS);
            }
        }

        setResult(RESULT_INTERRUPTED);
        setFragment(arguments);
    }

    private void setFragment(final LivenessCheck2Arguments livenessCheck2Arguments) {
        if (getSupportFragmentManager().findFragmentById(android.R.id.content) != null) {
            return;
        }

        final Bundle arguments = new Bundle();
        arguments.putSerializable(LivenessCheck2Fragment.ARGUMENTS, livenessCheck2Arguments);

        final Fragment fragment = new DemoLivenessCheck2Fragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

}
