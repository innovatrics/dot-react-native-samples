package com.innovatrics.android.dot.sample.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.innovatrics.android.dot.dto.LivenessCheckArguments;
import com.innovatrics.android.dot.fragment.LivenessCheckFragment;
import com.innovatrics.android.dot.sample.fragment.DemoLivenessCheckFragment;
import com.innovatrics.android.dot.sample.model.LivenessCheckModel;
import com.innovatrics.android.dot.sample.model.LivenessCheckModelFactory;

public class LivenessCheckActivity extends AppCompatActivity {

    public static final String ARG_LIVENESS_CHECK = "liveness_check";
    public static final String ARG_REFERENCE_TEMPLATE = "reference_template";
    public static final int RESULT_INTERRUPTED = 0;
    public static final int RESULT_CAMERA_INIT_FAILED = 1;
    public static final int RESULT_CAMERA_ACCESS_FAILED = 2;
    public static final int RESULT_NO_CAMERA_PERMISSION = 3;
    public static final int RESULT_NO_FRONT_CAMERA = 4;
    public static final int RESULT_NO_MORE_SEGMENTS = 5;
    public static final int RESULT_EYES_NOT_DETECTED = 6;
    public static final int RESULT_SUCCESS = 7;
    public static final int RESULT_FAIL = 8;

    private LivenessCheckModel livenessCheckModel;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_INTERRUPTED);

        LivenessCheckArguments livenessCheckArguments = null;
        byte[] referenceTemplate = null;

        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra(ARG_LIVENESS_CHECK)) {
                livenessCheckArguments = (LivenessCheckArguments) getIntent().getExtras().getSerializable(ARG_LIVENESS_CHECK);
            }
        }
        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra(ARG_REFERENCE_TEMPLATE)) {
                referenceTemplate = getIntent().getExtras().getByteArray(ARG_REFERENCE_TEMPLATE);
            }
        }

        final LivenessCheckModelFactory livenessCheckModelFactory = new LivenessCheckModelFactory(getApplication(), livenessCheckArguments, referenceTemplate);

        livenessCheckModel = ViewModelProviders.of(this, livenessCheckModelFactory).get(LivenessCheckModel.class);

        livenessCheckModel.getLivenessCheckSuccessEvent().observe(this, new Observer<Void>() {

            @Override
            public void onChanged(@Nullable final Void aVoid) {
                setResult(RESULT_SUCCESS);
                finish();
            }

        });

        livenessCheckModel.getLivenessCheckFailEvent().observe(this, new Observer<Void>() {

            @Override
            public void onChanged(@Nullable final Void aVoid) {
                setResult(RESULT_FAIL);
                finish();
            }

        });

        setFragment(livenessCheckArguments);
    }

    private void setFragment(final LivenessCheckArguments livenessCheckArguments) {
        if (getSupportFragmentManager().findFragmentById(android.R.id.content) != null) {
            return;
        }

        final Bundle arguments = new Bundle();
        arguments.putSerializable(LivenessCheckFragment.ARGUMENTS, livenessCheckArguments);

        final Fragment fragment = new DemoLivenessCheckFragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

}
