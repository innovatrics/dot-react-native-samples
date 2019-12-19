package com.innovatrics.android.dot.sample.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.innovatrics.android.dot.sample.R;
import com.innovatrics.android.dot.sample.model.SignInModel;

public class SignInErrorDialogFragment extends DialogFragment {

    public static final String TAG = SignInErrorDialogFragment.class.getSimpleName();
    public static final String ARG_ICON_RESOURCE_ID = "icon_resource_id";
    public static final String ARG_TITLE_RESOURCE_ID = "title_resource_id";
    public static final String ARG_MESSAGE_RESOURCE_ID = "message_resource_id";

    private SignInModel signInModel;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        signInModel = ViewModelProviders.of(getActivity()).get(SignInModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        int iconResourceId = 0;
        int titleResourceId = 0;
        int messageResourceId = 0;

        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_ICON_RESOURCE_ID)) {
                iconResourceId = getArguments().getInt(ARG_ICON_RESOURCE_ID);
            }
            if (getArguments().containsKey(ARG_TITLE_RESOURCE_ID)) {
                titleResourceId = getArguments().getInt(ARG_TITLE_RESOURCE_ID);
            }
            if (getArguments().containsKey(ARG_MESSAGE_RESOURCE_ID)) {
                messageResourceId = getArguments().getInt(ARG_MESSAGE_RESOURCE_ID);
            }
        }

        if (iconResourceId == 0) {
            throw new IllegalArgumentException("Argument 'iconResourceId' must be set.");
        }
        if (titleResourceId == 0) {
            throw new IllegalArgumentException("Argument 'titleResourceId' must be set.");
        }
        if (messageResourceId == 0) {
            throw new IllegalArgumentException("Argument 'messageResourceId' must be set.");
        }

        return new AlertDialog.Builder(getActivity())
                .setIcon(iconResourceId)
                .setTitle(titleResourceId)
                .setMessage(messageResourceId)
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        signInModel.cancel();
                    }

                })
                .create();
    }

}
