package com.innovatrics.android.dot.sample.activity;

        import android.os.Bundle;

        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        import com.innovatrics.android.dot.sample.fragment.DemoDocumentCaptureFragment;

public class DocumentCaptureActivity extends AppCompatActivity {

    public static final int RESULT_INTERRUPTED = 0;
    public static final int RESULT_CAMERA_INIT_FAILED = 1;
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

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new DemoDocumentCaptureFragment())
                .commit();
    }

}
