package com.innovatrics.android.dot.sample.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.innovatrics.android.dot.sample.R;

public class ProgressBarDialogFragment extends DialogFragment {

    public static final String TAG = ProgressBarDialogFragment.class.getSimpleName();

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_progress_bar, null);

        final AlertDialog dialog = new AlertDialog(getContext(), R.style.ThemeOverlay_AppCompat_Dialog) {

            @Override
            public void onBackPressed() {
                getActivity().onBackPressed();
            }

        };
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

}
