package com.innovatrics.android.dot.sample.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.innovatrics.android.dot.sample.R;
import com.innovatrics.android.dot.sample.model.OnboardingModel;
import com.innovatrics.android.dot.sample.model.OnboardingVerificationFailModel;

public class OnboardingVerificationFailFragment extends Fragment {

    private OnboardingVerificationFailModel onboardingVerificationFailModel;
    private OnboardingModel onboardingModel;
    private ImageView documentPhotoImageView;
    private ImageView selfiePhotoImageView;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding_verification_fail, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        documentPhotoImageView = view.findViewById(R.id.document_photo);
        selfiePhotoImageView = view.findViewById(R.id.selfie_photo);
        button = view.findViewById(R.id.button);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        onboardingVerificationFailModel = ViewModelProviders.of(this).get(OnboardingVerificationFailModel.class);
        onboardingModel = ViewModelProviders.of(getActivity()).get(OnboardingModel.class);

        onboardingVerificationFailModel.getDocumentBitmapLiveData().observe(this, new Observer<Bitmap>() {

            @Override
            public void onChanged(@Nullable final Bitmap bitmap) {
                documentPhotoImageView.setImageBitmap(bitmap);
            }

        });

        onboardingVerificationFailModel.getSelfieBitmapLiveData().observe(this, new Observer<Bitmap>() {

            @Override
            public void onChanged(@Nullable final Bitmap bitmap) {
                selfiePhotoImageView.setImageBitmap(bitmap);
            }

        });

        setupDocumentPhotoImageView();
        setupSelfiePhotoImageView();
        setupButton();

        if (savedInstanceState == null) {
            onboardingVerificationFailModel.loadDocumentImage(getContext(), onboardingModel.getDocumentFaceUri());
            onboardingVerificationFailModel.loadSelfieImage(getContext(), onboardingModel.getFaceCaptureUri());
        }
    }

    private void setupDocumentPhotoImageView() {
        documentPhotoImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                onboardingModel.restartDocumentCapture();
            }

        });
    }

    private void setupSelfiePhotoImageView() {
        selfiePhotoImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                onboardingModel.restartFaceCapture();
            }

        });
    }

    private void setupButton() {
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                new OnboardingAbortDialogFragment().show(getChildFragmentManager(), OnboardingAbortDialogFragment.TAG);
            }

        });
    }

}
