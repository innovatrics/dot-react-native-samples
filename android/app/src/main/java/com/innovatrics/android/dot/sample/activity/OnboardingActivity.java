package com.innovatrics.android.dot.sample.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.innovatrics.android.dot.sample.R;
import com.innovatrics.android.dot.sample.documentcapture.DocumentSide;
import com.innovatrics.android.dot.sample.documentreview.DocumentItem;
import com.innovatrics.android.dot.sample.dto.OnboardingData;
import com.innovatrics.android.dot.sample.enums.ErrorCode;
import com.innovatrics.android.dot.sample.fragment.*;
import com.innovatrics.android.dot.sample.model.OnboardingModel;
import com.innovatrics.android.dot.sample.model.OnboardingModelFactory;
import com.innovatrics.dot.face.autocapture.FaceAutoCaptureConfiguration;
import com.innovatrics.dot.face.autocapture.FaceAutoCaptureFragment;
import com.innovatrics.dot.face.liveness.eyegaze.EyeGazeLivenessConfiguration;
import com.innovatrics.dot.face.liveness.eyegaze.EyeGazeLivenessFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OnboardingActivity extends AppCompatActivity {

    public static final String ARG_APP_SERVER_URL = "app_server_url";
    public static final String ARG_USER_ID = "user_id";
    public static final int RESULT_INTERRUPTED = 0;
    public static final int RESULT_NO_CAMERA_PERMISSION = 3;
    public static final int RESULT_SUCCESS = 5;
    public static final int RESULT_SUCCESS_BUT_ABIS_FAILED = 6;
    public static final String OUT_ONBOARDING_DATA = "onboarding_data";

    private OnboardingModel onboardingModel;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_INTERRUPTED);

        String appServerUrl = null;
        String userId = null;

        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra(ARG_APP_SERVER_URL)) {
                appServerUrl = getIntent().getExtras().getString(ARG_APP_SERVER_URL);
            }
        }
        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra(ARG_USER_ID)) {
                userId = getIntent().getExtras().getString(ARG_USER_ID);
            }
        }

        final OnboardingModelFactory onboardingModelFactory = new OnboardingModelFactory(getApplication(), appServerUrl, userId);

        onboardingModel = ViewModelProviders.of(this, onboardingModelFactory).get(OnboardingModel.class);

        onboardingModel.getScreenLiveData().observe(this, new Observer<Integer>() {

            @Override
            public void onChanged(@Nullable final Integer screen) {
                switch (screen) {
                    case OnboardingModel.SCREEN_INSTRUCTIONS:
                        setOnboardingInstructionsFragment();
                        break;
                    case OnboardingModel.SCREEN_DOCUMENT_CAPTURE:
                        setOnboardingDocumentCaptureFragment();
                        break;
                    case OnboardingModel.SCREEN_DOCUMENT_REVIEW:
                        setOnboardingDocumentReviewFragment();
                        break;
                    case OnboardingModel.SCREEN_FACE_CAPTURE:
                        setOnboardingFaceCaptureFragment();
                        break;
                    case OnboardingModel.SCREEN_LIVENESS_CHECK:
                        setOnboardingLivenessCheckFragment();
                        break;
                    case OnboardingModel.SCREEN_VERIFICATION_FAIL:
                        setOnboardingVerificationFailFragment();
                        break;
                }

                hideProgressBarDialog();
            }

        });

        onboardingModel.getProcessingStartedEvent().observe(this, new Observer<Void>() {

            @Override
            public void onChanged(@Nullable final Void aVoid) {
                showProgressBarDialog();
            }

        });

        onboardingModel.getLivenessCheckFailEvent().observe(this, new Observer<Void>() {

            @Override
            public void onChanged(@Nullable final Void aVoid) {
                new OnboardingLivenessCheckFailDialogFragment().show(getSupportFragmentManager(), OnboardingLivenessCheckFailDialogFragment.TAG);
            }

        });

        onboardingModel.getErrorEvent().observe(this, new Observer<ErrorCode>() {

            @Override
            public void onChanged(@Nullable final ErrorCode errorCode) {
                hideProgressBarDialog();
                ErrorCode error = errorCode;
                if (errorCode == null) {
                    error = ErrorCode.SERVER_ERROR;
                }
                showErrorDialog(error);
            }

        });

        onboardingModel.getOnboardingCancelEvent().observe(this, new Observer<Void>() {

            @Override
            public void onChanged(@Nullable final Void aVoid) {
                finish();
            }

        });

        onboardingModel.getOnboardingDoneEvent().observe(this, new Observer<ErrorCode>() {

            @Override
            public void onChanged(@Nullable final ErrorCode errorCode) {
                final OnboardingData onboardingData = onboardingModel.onboardingData();

                final Intent intent = new Intent();
                intent.putExtra(OUT_ONBOARDING_DATA, onboardingData);

                final int result;

                if (errorCode != null) {
                    switch (errorCode) {
                        case UNABLE_TO_EXPORT_TO_ABIS:
                            result = RESULT_SUCCESS_BUT_ABIS_FAILED;
                            break;
                        default:
                            throw new IllegalStateException();
                    }
                } else {
                    result = RESULT_SUCCESS;
                }

                setResult(result, intent);
                finish();
            }

        });
    }

    private void setOnboardingInstructionsFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new OnboardingInstructionsFragment())
                .commit();
    }

    private void setOnboardingDocumentCaptureFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new OnboardingDocumentCaptureFragment())
                .commit();
    }

    private void setOnboardingDocumentReviewFragment() {
        final Map<DocumentSide, String> uriStringMap = onboardingModel.getOnboardingDocumentData().getUriStringMap();
        final List<DocumentItem> visualInspectionZoneItemList = onboardingModel.getOnboardingDocumentData().getVisualInspectionZoneItemList();
        final List<DocumentItem> machineReadableZoneItemList = onboardingModel.getOnboardingDocumentData().getMachineReadableZoneItemList();

        final Bundle arguments = new Bundle();
        if (uriStringMap.containsKey(DocumentSide.FRONT)) {
            final String uriString = uriStringMap.get(DocumentSide.FRONT);
            final Uri uri = Uri.parse(uriString);
            arguments.putParcelable(DocumentReviewFragment.ARG_FRONT_IMAGE_URI, uri);
        }
        if (uriStringMap.containsKey(DocumentSide.BACK)) {
            final String uriString = uriStringMap.get(DocumentSide.BACK);
            final Uri uri = Uri.parse(uriString);
            arguments.putParcelable(DocumentReviewFragment.ARG_BACK_IMAGE_URI, uri);
        }
        arguments.putSerializable(DocumentReviewFragment.ARG_VISUAL_INSPECTION_ZONE_ITEMS, new ArrayList<>(visualInspectionZoneItemList));
        arguments.putSerializable(DocumentReviewFragment.ARG_MACHINE_READABLE_ZONE_ITEMS, new ArrayList<>(machineReadableZoneItemList));
        arguments.putFloat(DocumentReviewFragment.ARG_DATA_CONFIDENCE_THRESHOLD, 0.9f);

        final Fragment fragment = new OnboardingDocumentReviewFragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

    private void setOnboardingFaceCaptureFragment() {
        final FaceAutoCaptureConfiguration faceAutoCaptureConfiguration = new FaceAutoCaptureConfiguration.Builder().build();

        final Bundle arguments = new Bundle();
        arguments.putSerializable(FaceAutoCaptureFragment.CONFIGURATION, faceAutoCaptureConfiguration);

        final Fragment fragment = new OnboardingFaceCaptureFragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

    private void setOnboardingLivenessCheckFragment() {
        final EyeGazeLivenessConfiguration eyeGazeLivenessConfiguration = new EyeGazeLivenessConfiguration.Builder(onboardingModel.getSegmentConfigurationList())
                .transitionType(EyeGazeLivenessConfiguration.TransitionType.FADE)
                .build();

        final Bundle arguments = new Bundle();
        arguments.putSerializable(EyeGazeLivenessFragment.CONFIGURATION, eyeGazeLivenessConfiguration);

        final Fragment fragment = new OnboardingLivenessCheckFragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

    private void setOnboardingVerificationFailFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new OnboardingVerificationFailFragment())
                .commit();
    }

    private void showProgressBarDialog() {
        if (getSupportFragmentManager().findFragmentByTag(ProgressBarDialogFragment.TAG) != null) {
            return;
        }

        final ProgressBarDialogFragment fragment = new ProgressBarDialogFragment();
        fragment.show(getSupportFragmentManager(), ProgressBarDialogFragment.TAG);
    }

    private void hideProgressBarDialog() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(ProgressBarDialogFragment.TAG);

        if (fragment == null) {
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
    }

    private void showErrorDialog(final ErrorCode errorCode) {
        final Bundle arguments = new Bundle();
        arguments.putInt(OnboardingErrorDialogFragment.ARG_ICON_RESOURCE_ID, errorCode.getIconResourceId());
        arguments.putInt(OnboardingErrorDialogFragment.ARG_TITLE_RESOURCE_ID, R.string.error_title);
        arguments.putInt(OnboardingErrorDialogFragment.ARG_MESSAGE_RESOURCE_ID, errorCode.getMessageResourceId());

        DialogFragment dialogFragment = new OnboardingErrorDialogFragment();
        String tag = OnboardingErrorDialogFragment.TAG;

        if (onboardingModel.getScreenLiveData().getValue() != null) {
            if (onboardingModel.getScreenLiveData().getValue() == OnboardingModel.SCREEN_DOCUMENT_CAPTURE) {
                dialogFragment = new OnboardingDocumentErrorDialogFragment();
                tag = OnboardingDocumentErrorDialogFragment.TAG;
            }
        }

        dialogFragment.setArguments(arguments);
        dialogFragment.show(getSupportFragmentManager(), tag);
    }

}
