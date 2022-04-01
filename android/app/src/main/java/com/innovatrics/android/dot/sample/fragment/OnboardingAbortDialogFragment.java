package com.innovatrics.android.dot.sample.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class OnboardingAbortDialogFragment extends DialogFragment {

    public static final String TAG = OnboardingAbortDialogFragment.class.getSimpleName();

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage("Do you really want to abort onboarding?")
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        getActivity().finish();
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                    }

                })
                .create();
    }

}
