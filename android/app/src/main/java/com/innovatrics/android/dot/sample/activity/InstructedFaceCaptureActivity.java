package com.innovatrics.android.dot.sample.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.innovatrics.android.dot.sample.fragment.InstructedDemoFaceCaptureFragment;
import com.innovatrics.android.dot.sample.fragment.PhotoConfirmationFragment;
import com.innovatrics.android.dot.sample.fragment.TutorialFragment;
import com.innovatrics.android.dot.sample.model.InstructedFaceCaptureModel;
import com.innovatrics.android.dot.sample.model.InstructedFaceCaptureModelFactory;
import com.innovatrics.dot.face.autocapture.FaceAutoCaptureConfiguration;
import com.innovatrics.dot.face.autocapture.FaceAutoCaptureFragment;

import java.io.File;
import java.io.IOException;

public class InstructedFaceCaptureActivity extends AppCompatActivity {

    public static final int RESULT_INTERRUPTED = 0;
    public static final int RESULT_NO_CAMERA_PERMISSION = 3;
    public static final int RESULT_SUCCESS = 4;

    private InstructedFaceCaptureModel instructedFaceCaptureModel;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_INTERRUPTED);

        final Uri photoUri = Uri.fromFile(getTempFile());
        final InstructedFaceCaptureModelFactory instructedFaceCaptureModelFactory = new InstructedFaceCaptureModelFactory(photoUri);

        instructedFaceCaptureModel = ViewModelProviders.of(this, instructedFaceCaptureModelFactory).get(InstructedFaceCaptureModel.class);

        instructedFaceCaptureModel.getPrepareToCaptureEvent().observe(this, new Observer<Void>() {

            @Override
            public void onChanged(@Nullable final Void aVoid) {
                final FaceAutoCaptureConfiguration faceCaptureArguments = new FaceAutoCaptureConfiguration.Builder().build();

                final Bundle arguments = new Bundle();
                arguments.putSerializable(FaceAutoCaptureFragment.CONFIGURATION, faceCaptureArguments);

                final Fragment fragment = new InstructedDemoFaceCaptureFragment();
                fragment.setArguments(arguments);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, fragment)
                        .commit();
            }

        });

        instructedFaceCaptureModel.getFaceCaptureEvent().observe(this, new Observer<Integer>() {

            @Override
            public void onChanged(@Nullable final Integer result) {
                switch (result) {
                    case RESULT_SUCCESS:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(android.R.id.content, new PhotoConfirmationFragment())
                                .commit();
                        break;
                    default:
                        setResult(result);
                        finish();
                }
            }

        });

        instructedFaceCaptureModel.getSubmitPhotoEvent().observe(this, new Observer<Void>() {

            @Override
            public void onChanged(@Nullable final Void aVoid) {
                final Intent intent = new Intent();
                intent.setData(photoUri);

                setResult(RESULT_SUCCESS, intent);
                finish();
            }

        });

        setTutorialFragment();
    }

    private void setTutorialFragment() {
        if (getSupportFragmentManager().findFragmentById(android.R.id.content) != null) {
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new TutorialFragment())
                .commit();
    }

    private File getTempFile() {
        try {
            return File.createTempFile("photo_", ".jpg", getCacheDir());
        } catch (final IOException e) {
            throw new RuntimeException("Unable to create photo file.");
        }
    }

}
