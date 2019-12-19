package com.innovatrics.android.dot.sample.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.innovatrics.android.dot.sample.fragment.FaceDetectorFragment;

public class FaceDetectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFragment();
    }

    private void setFragment() {
        if (getSupportFragmentManager().findFragmentById(android.R.id.content) != null) {
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new FaceDetectorFragment())
                .commit();
    }

}
