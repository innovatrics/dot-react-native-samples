package com.innovatrics.android.dot.sample.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.innovatrics.android.dot.sample.model.OnboardingModel;

public class OnboardingLivenessCheckFailDialogFragment extends DialogFragment {

    public static final String TAG = OnboardingLivenessCheckFailDialogFragment.class.getSimpleName();

    private OnboardingModel onboardingModel;

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onboardingModel = ViewModelProviders.of(getActivity()).get(OnboardingModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage("Liveness Check failed")
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        onboardingModel.restartLivenessCheck();
                    }

                })
                .create();
    }

}
