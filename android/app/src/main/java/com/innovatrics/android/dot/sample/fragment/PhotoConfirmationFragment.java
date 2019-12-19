package com.innovatrics.android.dot.sample.fragment;

import android.graphics.drawable.Drawable;
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
import com.innovatrics.android.dot.sample.model.InstructedFaceCaptureModel;
import com.innovatrics.android.dot.sample.model.PhotoConfirmationModel;

public class PhotoConfirmationFragment extends Fragment {

    private InstructedFaceCaptureModel instructedFaceCaptureModel;
    private PhotoConfirmationModel photoConfirmationModel;
    private ImageView photoView;
    private Button retryButton;
    private Button confirmButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_confirmation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        photoView = view.findViewById(R.id.image_photo);
        retryButton = view.findViewById(R.id.retry);
        confirmButton = view.findViewById(R.id.confirm);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        instructedFaceCaptureModel = ViewModelProviders.of(getActivity()).get(InstructedFaceCaptureModel.class);

        photoConfirmationModel = ViewModelProviders.of(this).get(PhotoConfirmationModel.class);

        if (instructedFaceCaptureModel.getPhotoUri() != null) {
            photoConfirmationModel.loadImage(getContext(), instructedFaceCaptureModel.getPhotoUri());
        }

        photoConfirmationModel.getImageDrawableLiveData().observe(this, new Observer<Drawable>() {

            @Override
            public void onChanged(@Nullable final Drawable drawable) {
                photoView.setImageDrawable(drawable);
            }

        });

        setupRetryButton();
        setupConfirmButton();
    }

    private void setupRetryButton() {
        retryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                instructedFaceCaptureModel.prepareAndStartCapture();
            }

        });
    }

    private void setupConfirmButton() {
        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                instructedFaceCaptureModel.submitPhoto();
            }

        });
    }

}
