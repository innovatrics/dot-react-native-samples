package com.innovatrics.android.dot.sample.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.innovatrics.android.dot.Dot;
import com.innovatrics.android.dot.dto.LivenessCheck2Arguments;
import com.innovatrics.android.dot.dto.LivenessCheckArguments;
import com.innovatrics.android.dot.livenesscheck.liveness.DotPosition;
import com.innovatrics.android.dot.livenesscheck.model.SegmentConfiguration;
import com.innovatrics.android.dot.sample.R;
import com.innovatrics.android.dot.sample.documentcapture.DocumentSide;
import com.innovatrics.android.dot.sample.dto.OnboardingData;
import com.innovatrics.android.dot.utils.LicenseUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Sample demonstrating Digital Onboarding Toolkit on mobile devices.
 * Build and deploy app to mobile device (only armeabi-v7a and arm64-v8a is currently supported).
 */
public class DemoActivity extends AppCompatActivity {

    private static final int REQUEST_FACE_CAPTURE = 0;
    private static final int REQUEST_INSTRUCTED_FACE_CAPTURE = 1;
    private static final int REQUEST_FACE_CAPTURE_SIMPLE = 2;
    private static final int REQUEST_FACEDETECTOR = 3;
    private static final int REQUEST_VERIFICATION = 4;
    private static final int REQUEST_LIVENESS_CHECK = 5;
    private static final int REQUEST_LIVENESS_CHECK_2 = 6;
    private static final int REQUEST_DOCUMENT_CAPTURE = 7;
    private static final int REQUEST_ONBOARDING = 8;
    private static final int REQUEST_SIGN_IN = 9;
    private static final String KEY_RESULT_DATA = "result_data";
    private static final String KEY_PHOTO_URI = "photo_uri";
    private static final String KEY_DOCUMENT_FACE_URI = "document_face_uri";
    private static final String KEY_DOCUMENT_FRONT_URI = "document_front_uri";
    private static final String KEY_DOCUMENT_BACK_URI = "document_back_uri";
    private static final String KEY_TEMPLATE = "template";

    private final TextWatcher onboardingUseCasesTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
        }

        @Override
        public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        }

        @Override
        public void afterTextChanged(final Editable s) {
            final boolean enabled = appServerUrlEditText.getText().length() > 0 && userIdEditText.getText().length() > 0;
            onboardingButton.setEnabled(enabled);
            signInButton.setEnabled(enabled);
        }

    };

    private String resultData;
    private byte[] template;
    private Uri photoUri;
    private Uri documentFaceUri;
    private Uri documentFrontUri;
    private Uri documentBackUri;
    private Button faceCaptureButton;
    private Button instructedFaceCaptureButton;
    private Button faceCaptureSimpleButton;
    private Button faceDetectorButton;
    private Button verificationButton;
    private Button livenessCheckMovingTransitionButton;
    private Button livenessCheckFadingTransitionButton;
    private Button livenessCheck2MovingTransitionButton;
    private Button documentCaptureButton;
    private Button documentReviewButton;
    private EditText appServerUrlEditText;
    private EditText userIdEditText;
    private Button onboardingButton;
    private Button signInButton;
    private TextView resultDataTextView;
    private ImageView photoImageView;
    private ImageView documentFaceImageView;
    private ImageView documentFrontImageView;
    private ImageView documentBackImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        faceCaptureButton = findViewById(R.id.face_capture);
        instructedFaceCaptureButton = findViewById(R.id.instructed_face_capture);
        faceCaptureSimpleButton = findViewById(R.id.face_capture_simple);
        faceDetectorButton = findViewById(R.id.facedetector);
        verificationButton = findViewById(R.id.verification);
        livenessCheckMovingTransitionButton = findViewById(R.id.liveness_check_moving_transition);
        livenessCheckFadingTransitionButton = findViewById(R.id.liveness_check_fading_transition);
        livenessCheck2MovingTransitionButton = findViewById(R.id.liveness_check_2_moving_transition);
        documentCaptureButton = findViewById(R.id.document_capture);
        documentReviewButton = findViewById(R.id.document_review);
        appServerUrlEditText = findViewById(R.id.app_server_url);
        userIdEditText = findViewById(R.id.user_id);
        onboardingButton = findViewById(R.id.onboarding);
        signInButton = findViewById(R.id.sign_in);
        resultDataTextView = findViewById(R.id.result_data);
        photoImageView = findViewById(R.id.photo);
        documentFaceImageView = findViewById(R.id.document_face);
        documentFrontImageView = findViewById(R.id.document_front);
        documentBackImageView = findViewById(R.id.document_back);

        faceCaptureButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(DemoActivity.this, FaceCaptureActivity.class);
                startActivityForResult(intent, REQUEST_FACE_CAPTURE);
            }

        });

        instructedFaceCaptureButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(DemoActivity.this, InstructedFaceCaptureActivity.class);
                startActivityForResult(intent, REQUEST_INSTRUCTED_FACE_CAPTURE);
            }

        });

        faceCaptureSimpleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(DemoActivity.this, FaceCaptureSimpleActivity.class);
                startActivityForResult(intent, REQUEST_FACE_CAPTURE_SIMPLE);
            }

        });

        faceDetectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(DemoActivity.this, FaceDetectorActivity.class);
                startActivityForResult(intent, REQUEST_FACEDETECTOR);
            }
        });

        verificationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                if (template == null) {
                    Toast.makeText(DemoActivity.this, "Capture your face first", Toast.LENGTH_SHORT).show();
                    return;
                }

                final Intent intent = new Intent(DemoActivity.this, VerificationActivity.class);
                intent.putExtra(VerificationActivity.ARG_REFERENCE_TEMPLATE, template);

                startActivityForResult(intent, REQUEST_VERIFICATION);
            }

        });

        livenessCheckMovingTransitionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                final List<SegmentConfiguration> segmentList = createSegmentConfigurationList();

                final LivenessCheckArguments livenessCheckArguments = new LivenessCheckArguments.Builder()
                        .segmentList(segmentList)
                        .transitionType(LivenessCheckArguments.TransitionType.MOVING)
                        .dotColorResId(R.color.colorPrimary)
                        .build();

                final Intent intent = new Intent(DemoActivity.this, LivenessCheckActivity.class);
                intent.putExtra(LivenessCheckActivity.ARG_LIVENESS_CHECK, livenessCheckArguments);
                intent.putExtra(LivenessCheckActivity.ARG_REFERENCE_TEMPLATE, template);

                startActivityForResult(intent, REQUEST_LIVENESS_CHECK);
            }

        });

        livenessCheckFadingTransitionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                final List<SegmentConfiguration> segmentList = createSegmentConfigurationList();

                final LivenessCheckArguments livenessCheckArguments = new LivenessCheckArguments.Builder()
                        .segmentList(segmentList)
                        .transitionType(LivenessCheckArguments.TransitionType.FADING)
                        .drawableResId(R.mipmap.ic_launcher)
                        .backgroundColorResId(R.color.colorPrimaryDark)
                        .build();

                final Intent intent = new Intent(DemoActivity.this, LivenessCheckActivity.class);
                intent.putExtra(LivenessCheckActivity.ARG_LIVENESS_CHECK, livenessCheckArguments);
                intent.putExtra(LivenessCheckActivity.ARG_REFERENCE_TEMPLATE, template);

                startActivityForResult(intent, REQUEST_LIVENESS_CHECK);
            }

        });

        livenessCheck2MovingTransitionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                final List<SegmentConfiguration> segmentList = createSegmentConfigurationList();

                final LivenessCheck2Arguments livenessCheck2Arguments = new LivenessCheck2Arguments.Builder()
                        .segmentList(segmentList)
                        .transitionType(LivenessCheck2Arguments.TransitionType.MOVING)
                        .dotColorResId(R.color.colorPrimary)
                        .build();

                final Intent intent = new Intent(DemoActivity.this, LivenessCheck2Activity.class);
                intent.putExtra(LivenessCheck2Activity.ARGUMENTS, livenessCheck2Arguments);

                startActivityForResult(intent, REQUEST_LIVENESS_CHECK_2);
            }

        });

        documentCaptureButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(DemoActivity.this, DocumentCaptureActivity.class);
                startActivityForResult(intent, REQUEST_DOCUMENT_CAPTURE);
            }

        });

        documentReviewButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(DemoActivity.this, DocumentReviewActivity.class);
                startActivity(intent);
            }

        });

        appServerUrlEditText.addTextChangedListener(onboardingUseCasesTextWatcher);
        userIdEditText.addTextChangedListener(onboardingUseCasesTextWatcher);

        onboardingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(DemoActivity.this, OnboardingActivity.class);
                intent.putExtra(OnboardingActivity.ARG_APP_SERVER_URL, appServerUrlEditText.getText().toString());
                intent.putExtra(OnboardingActivity.ARG_USER_ID, userIdEditText.getText().toString());

                startActivityForResult(intent, REQUEST_ONBOARDING);
            }

        });

        signInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(DemoActivity.this, SignInActivity.class);
                intent.putExtra(SignInActivity.ARG_APP_SERVER_URL, appServerUrlEditText.getText().toString());
                intent.putExtra(SignInActivity.ARG_USER_ID, userIdEditText.getText().toString());

                startActivityForResult(intent, REQUEST_SIGN_IN);
            }

        });

        if (savedInstanceState != null) {
            resultData = savedInstanceState.getString(KEY_RESULT_DATA);
            if (resultData != null) {
                resultDataTextView.setText(resultData);
            }

            template = savedInstanceState.getByteArray(KEY_TEMPLATE);

            photoUri = savedInstanceState.getParcelable(KEY_PHOTO_URI);
            if (photoUri != null) {
                photoImageView.setImageURI(photoUri);
            }

            documentFaceUri = savedInstanceState.getParcelable(KEY_DOCUMENT_FACE_URI);
            if (documentFaceUri != null) {
                documentFaceImageView.setImageURI(documentFaceUri);
            }

            documentFrontUri = savedInstanceState.getParcelable(KEY_DOCUMENT_FRONT_URI);
            if (documentFrontUri != null) {
                documentFrontImageView.setImageURI(documentFrontUri);
            }

            documentBackUri = savedInstanceState.getParcelable(KEY_DOCUMENT_BACK_URI);
            if (documentBackUri != null) {
                documentBackImageView.setImageURI(documentBackUri);
            }
        }

        if (Dot.getInstance().isInitialized()) {
            setButtonsEnabled(true);
        } else {
            setButtonsEnabled(false);

            final byte[] license = LicenseUtils.loadRawLicense(this, R.raw.sample_license);
            final Dot.Listener dotListener = new Dot.Listener() {

                @Override
                public void onInitSuccess() {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(DemoActivity.this, "DOT Android Kit is initialized.", Toast.LENGTH_SHORT).show();
                            setButtonsEnabled(true);
                        }

                    });
                }

                @Override
                public void onInitFail(final String message) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(DemoActivity.this, "DOT Android Kit: " + message, Toast.LENGTH_SHORT).show();
                            setButtonsEnabled(false);
                        }

                    });
                }

                @Override
                public void onClosed() {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(DemoActivity.this, "DOT Android Kit is closed.", Toast.LENGTH_SHORT).show();
                            setButtonsEnabled(false);
                        }

                    });
                }

            };

            Dot.getInstance().initAsync(license, dotListener);
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_RESULT_DATA, resultData);
        outState.putByteArray(KEY_TEMPLATE, template);
        outState.putParcelable(KEY_PHOTO_URI, photoUri);
        outState.putParcelable(KEY_DOCUMENT_FACE_URI, documentFaceUri);
        outState.putParcelable(KEY_DOCUMENT_FRONT_URI, documentFrontUri);
        outState.putParcelable(KEY_DOCUMENT_BACK_URI, documentBackUri);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        resultData = null;
        photoUri = null;
        documentFaceUri = null;
        documentFrontUri = null;
        documentBackUri = null;
        resultDataTextView.setText(null);
        photoImageView.setImageResource(0);
        documentFaceImageView.setImageResource(0);
        documentFrontImageView.setImageResource(0);
        documentBackImageView.setImageResource(0);

        final float score;

        switch (requestCode) {
            case REQUEST_FACE_CAPTURE:
                switch (resultCode) {
                    case FaceCaptureActivity.RESULT_INTERRUPTED:
                        Toast.makeText(this, "Face Capture interrupted", Toast.LENGTH_SHORT).show();
                        break;
                    case FaceCaptureActivity.RESULT_CAMERA_INIT_FAILED:
                        Toast.makeText(this, "Camera init failed", Toast.LENGTH_SHORT).show();
                        break;
                    case FaceCaptureActivity.RESULT_CAMERA_ACCESS_FAILED:
                        Toast.makeText(this, "Camera access failed", Toast.LENGTH_SHORT).show();
                        break;
                    case FaceCaptureActivity.RESULT_NO_CAMERA_PERMISSION:
                        Toast.makeText(this, "No camera permission", Toast.LENGTH_SHORT).show();
                        break;
                    case FaceCaptureActivity.RESULT_SUCCESS:
                        template = data.getByteArrayExtra(FaceCaptureActivity.OUT_TEMPLATE);
                        photoUri = data.getData();
                        photoImageView.setImageURI(photoUri);
                        Toast.makeText(this, "Face Capture success", Toast.LENGTH_SHORT).show();
                        break;
                    case FaceCaptureActivity.RESULT_FAIL:
                        Toast.makeText(this, "Face Capture failed", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Unknown Face Capture result code: " + resultCode, Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_INSTRUCTED_FACE_CAPTURE:
                switch (resultCode) {
                    case InstructedFaceCaptureActivity.RESULT_INTERRUPTED:
                        Toast.makeText(this, "Instructed Face Capture interrupted", Toast.LENGTH_SHORT).show();
                        break;
                    case InstructedFaceCaptureActivity.RESULT_CAMERA_INIT_FAILED:
                        Toast.makeText(this, "Camera init failed", Toast.LENGTH_SHORT).show();
                        break;
                    case InstructedFaceCaptureActivity.RESULT_CAMERA_ACCESS_FAILED:
                        Toast.makeText(this, "Camera access failed", Toast.LENGTH_SHORT).show();
                        break;
                    case InstructedFaceCaptureActivity.RESULT_NO_CAMERA_PERMISSION:
                        Toast.makeText(this, "No camera permission", Toast.LENGTH_SHORT).show();
                        break;
                    case InstructedFaceCaptureActivity.RESULT_SUCCESS:
                        photoUri = data.getData();
                        photoImageView.setImageURI(photoUri);
                        Toast.makeText(this, "Instructed Face Capture success", Toast.LENGTH_SHORT).show();
                        break;
                    case InstructedFaceCaptureActivity.RESULT_FAIL:
                        Toast.makeText(this, "Instructed Face Capture failed", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Unknown Instructed Face Capture result code: " + resultCode, Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_FACE_CAPTURE_SIMPLE:
                switch (resultCode) {
                    case FaceCaptureSimpleActivity.RESULT_INTERRUPTED:
                        Toast.makeText(this, "Face Capture Simple interrupted", Toast.LENGTH_SHORT).show();
                        break;
                    case FaceCaptureSimpleActivity.RESULT_CAMERA_INIT_FAILED:
                        Toast.makeText(this, "Camera init failed", Toast.LENGTH_SHORT).show();
                        break;
                    case FaceCaptureSimpleActivity.RESULT_CAMERA_ACCESS_FAILED:
                        Toast.makeText(this, "Camera access failed", Toast.LENGTH_SHORT).show();
                        break;
                    case FaceCaptureSimpleActivity.RESULT_NO_CAMERA_PERMISSION:
                        Toast.makeText(this, "No camera permission", Toast.LENGTH_SHORT).show();
                        break;
                    case FaceCaptureSimpleActivity.RESULT_SUCCESS:
                        photoUri = data.getData();
                        photoImageView.setImageURI(photoUri);
                        Toast.makeText(this, "Face Capture Simple success", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Unknown Face Capture Simple result code: " + resultCode, Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_VERIFICATION:
                switch (resultCode) {
                    case VerificationActivity.RESULT_INTERRUPTED:
                        Toast.makeText(this, "Face Capture Simple interrupted", Toast.LENGTH_SHORT).show();
                        break;
                    case VerificationActivity.RESULT_CAMERA_INIT_FAILED:
                        Toast.makeText(this, "Camera init failed", Toast.LENGTH_SHORT).show();
                        break;
                    case VerificationActivity.RESULT_CAMERA_ACCESS_FAILED:
                        Toast.makeText(this, "Camera access failed", Toast.LENGTH_SHORT).show();
                        break;
                    case VerificationActivity.RESULT_NO_CAMERA_PERMISSION:
                        Toast.makeText(this, "No camera permission", Toast.LENGTH_SHORT).show();
                        break;
                    case VerificationActivity.RESULT_SUCCESS:
                        score = data.getFloatExtra(VerificationActivity.OUT_SCORE, 0f);
                        Toast.makeText(this, "Verification success, score: " + score, Toast.LENGTH_SHORT).show();
                        break;
                    case VerificationActivity.RESULT_FAIL:
                        score = data.getFloatExtra(VerificationActivity.OUT_SCORE, 0f);
                        Toast.makeText(this, "Verification fail, score: " + score, Toast.LENGTH_SHORT).show();
                        break;
                    case VerificationActivity.RESULT_TEMPLATE_INCOMPATIBLE:
                        Toast.makeText(this, "Templates are not compatible", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Unknown Face Capture Simple result code: " + resultCode, Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_LIVENESS_CHECK:
                switch (resultCode) {
                    case LivenessCheckActivity.RESULT_INTERRUPTED:
                        Toast.makeText(this, "Liveness Check interrupted", Toast.LENGTH_SHORT).show();
                        break;
                    case LivenessCheckActivity.RESULT_CAMERA_INIT_FAILED:
                        Toast.makeText(this, "Camera init failed", Toast.LENGTH_SHORT).show();
                        break;
                    case LivenessCheckActivity.RESULT_CAMERA_ACCESS_FAILED:
                        Toast.makeText(this, "Camera access failed", Toast.LENGTH_SHORT).show();
                        break;
                    case LivenessCheckActivity.RESULT_NO_CAMERA_PERMISSION:
                        Toast.makeText(this, "No camera permission", Toast.LENGTH_SHORT).show();
                        break;
                    case LivenessCheckActivity.RESULT_NO_FRONT_CAMERA:
                        Toast.makeText(this, "No front camera on device available", Toast.LENGTH_SHORT).show();
                        break;
                    case LivenessCheckActivity.RESULT_NO_MORE_SEGMENTS:
                        Toast.makeText(this, "Liveness Check failed no more segments", Toast.LENGTH_SHORT).show();
                        break;
                    case LivenessCheckActivity.RESULT_EYES_NOT_DETECTED:
                        Toast.makeText(this, "Liveness Check failed eyes not detected", Toast.LENGTH_SHORT).show();
                        break;
                    case LivenessCheckActivity.RESULT_SUCCESS:
                        Toast.makeText(this, "Liveness Check success", Toast.LENGTH_SHORT).show();
                        break;
                    case LivenessCheckActivity.RESULT_FAIL:
                        Toast.makeText(this, "Liveness Check failed", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Unknown Liveness Check result code: " + resultCode, Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_LIVENESS_CHECK_2:
                switch (resultCode) {
                    case LivenessCheck2Activity.RESULT_INTERRUPTED:
                        Toast.makeText(this, "Liveness check interrupted", Toast.LENGTH_SHORT).show();
                        break;
                    case LivenessCheck2Activity.RESULT_CAMERA_INIT_FAILED:
                        Toast.makeText(this, "Failed to init camera", Toast.LENGTH_SHORT).show();
                        break;
                    case LivenessCheck2Activity.RESULT_CAMERA_ACCESS_FAILED:
                        Toast.makeText(this, "Camera access failed", Toast.LENGTH_SHORT).show();
                        break;
                    case LivenessCheck2Activity.RESULT_NO_CAMERA_PERMISSION:
                        Toast.makeText(this, "No camera permission", Toast.LENGTH_SHORT).show();
                        break;
                    case LivenessCheck2Activity.RESULT_FACE_CAPTURE_FAIL:
                        Toast.makeText(this, "Face Capture failed", Toast.LENGTH_SHORT).show();
                        break;
                    case LivenessCheck2Activity.RESULT_NO_MORE_SEGMENTS:
                        Toast.makeText(this, "Liveness check failed no more segments", Toast.LENGTH_SHORT).show();
                        break;
                    case LivenessCheck2Activity.RESULT_EYES_NOT_DETECTED:
                        Toast.makeText(this, "Liveness check failed eyes not detected", Toast.LENGTH_SHORT).show();
                        break;
                    case LivenessCheck2Activity.RESULT_DONE:
                        photoUri = data.getData();
                        photoImageView.setImageURI(photoUri);
                        score = data.getFloatExtra(LivenessCheck2Activity.OUT_SCORE, 0);
                        Toast.makeText(this, "Done, score: " + score, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Unknown Liveness check result code: " + resultCode, Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_DOCUMENT_CAPTURE:
                switch (resultCode) {
                    case DocumentCaptureActivity.RESULT_SUCCESS:
                        documentFrontUri = data.getData();
                        photoImageView.setImageURI(documentFrontUri);
                        break;
                    case DocumentCaptureActivity.RESULT_CAMERA_INIT_FAILED:
                        Toast.makeText(this, "Camera init failed", Toast.LENGTH_SHORT).show();
                        break;
                    case DocumentCaptureActivity.RESULT_INTERRUPTED:
                        Toast.makeText(this, "Document Capture interrupted", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Unknown Document Capture result code: " + resultCode, Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_ONBOARDING:
                switch (resultCode) {
                    case OnboardingActivity.RESULT_INTERRUPTED:
                        Toast.makeText(this, "Onboarding interrupted", Toast.LENGTH_SHORT).show();
                        break;
                    case OnboardingActivity.RESULT_CAMERA_INIT_FAILED:
                        Toast.makeText(this, "Camera init failed", Toast.LENGTH_SHORT).show();
                        break;
                    case OnboardingActivity.RESULT_CAMERA_ACCESS_FAILED:
                        Toast.makeText(this, "Camera access failed", Toast.LENGTH_SHORT).show();
                        break;
                    case OnboardingActivity.RESULT_NO_CAMERA_PERMISSION:
                        Toast.makeText(this, "No camera permission", Toast.LENGTH_SHORT).show();
                        break;
                    case OnboardingActivity.RESULT_NO_FRONT_CAMERA:
                        Toast.makeText(this, "No front camera on device available", Toast.LENGTH_SHORT).show();
                        break;
                    case OnboardingActivity.RESULT_SUCCESS_BUT_ABIS_FAILED:
                        showOnboardingData(data);
                        Toast.makeText(this, "Onboarding success, but server was not able to export data to ABIS", Toast.LENGTH_SHORT).show();
                        break;
                    case OnboardingActivity.RESULT_SUCCESS:
                        showOnboardingData(data);
                        Toast.makeText(this, "Onboarding success", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Unknown Onboarding result code: " + resultCode, Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_SIGN_IN:
                switch (resultCode) {
                    case SignInActivity.RESULT_INTERRUPTED:
                        Toast.makeText(this, "Sign In interrupted", Toast.LENGTH_SHORT).show();
                        break;
                    case SignInActivity.RESULT_CAMERA_INIT_FAILED:
                        Toast.makeText(this, "Camera init failed", Toast.LENGTH_SHORT).show();
                        break;
                    case SignInActivity.RESULT_CAMERA_ACCESS_FAILED:
                        Toast.makeText(this, "Camera access failed", Toast.LENGTH_SHORT).show();
                        break;
                    case SignInActivity.RESULT_NO_CAMERA_PERMISSION:
                        Toast.makeText(this, "No camera permission", Toast.LENGTH_SHORT).show();
                        break;
                    case SignInActivity.RESULT_SUCCESS:
                        final String username = data.getStringExtra(SignInActivity.OUT_USERNAME);
                        Toast.makeText(this, "Sign In success. User name: " + username, Toast.LENGTH_SHORT).show();
                        break;
                    case SignInActivity.RESULT_FAIL:
                        Toast.makeText(this, "Sign In failed", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Unknown Sign In result code: " + resultCode, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setButtonsEnabled(final boolean enabled) {
        faceCaptureButton.setEnabled(enabled);
        instructedFaceCaptureButton.setEnabled(enabled);
        faceCaptureSimpleButton.setEnabled(enabled);
        faceDetectorButton.setEnabled(enabled);
        verificationButton.setEnabled(enabled);
        livenessCheckMovingTransitionButton.setEnabled(enabled);
        livenessCheckFadingTransitionButton.setEnabled(enabled);
        livenessCheck2MovingTransitionButton.setEnabled(enabled);
        documentCaptureButton.setEnabled(enabled);
        documentReviewButton.setEnabled(enabled);
        appServerUrlEditText.setEnabled(enabled);
        userIdEditText.setEnabled(enabled);
    }

    private List<SegmentConfiguration> createSegmentConfigurationList() {
        final List<SegmentConfiguration> list = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            final DotPosition position = (DotPosition.getRandomPositionExclude(Arrays.asList(
                    i > 0 ? list.get(i - 1).getTargetPosition() : null,
                    i > 1 ? list.get(i - 2).getTargetPosition() : null)));
            list.add(new SegmentConfiguration(position.name(), 1000));
        }

        return list;
    }

    private void showOnboardingData(final Intent data) {
        final OnboardingData onboardingData = (OnboardingData) data.getExtras().getSerializable(OnboardingActivity.OUT_ONBOARDING_DATA);

        resultData = onboardingData.toString();
        photoUri = Uri.parse(onboardingData.getFaceCaptureUriString());
        documentFaceUri = Uri.parse(onboardingData.getDocumentFaceUriString());
        if (onboardingData.getUriStringMap().containsKey(DocumentSide.FRONT)) {
            documentFrontUri = Uri.parse(onboardingData.getUriStringMap().get(DocumentSide.FRONT));
        }
        if (onboardingData.getUriStringMap().containsKey(DocumentSide.BACK)) {
            documentBackUri = Uri.parse(onboardingData.getUriStringMap().get(DocumentSide.BACK));
        }

        resultDataTextView.setText(resultData);
        photoImageView.setImageURI(photoUri);
        documentFaceImageView.setImageURI(documentFaceUri);
        documentFrontImageView.setImageURI(documentFrontUri);
        documentBackImageView.setImageURI(documentBackUri);
    }

}
