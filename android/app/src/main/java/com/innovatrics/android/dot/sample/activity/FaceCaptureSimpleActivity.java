package com.innovatrics.android.dot.sample.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.innovatrics.android.dot.sample.fragment.DemoFaceCaptureSimpleFragment;
import com.innovatrics.dot.face.simplecapture.FaceSimpleCaptureConfiguration;
import com.innovatrics.dot.face.simplecapture.FaceSimpleCaptureFragment;

public class FaceCaptureSimpleActivity extends AppCompatActivity {

    public static final int RESULT_INTERRUPTED = 0;
    public static final int RESULT_NO_CAMERA_PERMISSION = 3;
    public static final int RESULT_SUCCESS = 4;

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

        final FaceSimpleCaptureConfiguration faceSimpleCaptureConfiguration = new FaceSimpleCaptureConfiguration.Builder().build();

        final Bundle arguments = new Bundle();
        arguments.putSerializable(FaceSimpleCaptureFragment.CONFIGURATION, faceSimpleCaptureConfiguration);

        final Fragment fragment = new DemoFaceCaptureSimpleFragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

}
