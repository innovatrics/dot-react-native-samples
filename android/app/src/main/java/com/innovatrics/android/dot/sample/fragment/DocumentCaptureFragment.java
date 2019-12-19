package com.innovatrics.android.dot.sample.fragment;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.innovatrics.android.dot.camera.FlashModes;
import com.innovatrics.android.dot.sample.R;
import com.innovatrics.android.dot.sample.documentcapture.DocumentSide;
import com.innovatrics.android.dot.sample.model.DocumentCaptureModel;
import com.innovatrics.android.dot.sample.view.CameraLayout;
import com.innovatrics.android.dot.sample.view.DocumentPlaceholderView;

import java.util.List;

public abstract class DocumentCaptureFragment extends Fragment {

    private static final int REQUEST_PERMISSIONS = 0;

    private DocumentCaptureModel model;
    private FrameLayout cameraFrame;
    private CameraLayout cameraLayout;
    private TextView captureTypeTextView;
    private TextView instructionTextView;
    private Button takePhotoButton;
    private Button flashButton;
    private View contentOverlayView;
    private DocumentPlaceholderView placeholderView;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_document_capture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cameraFrame = view.findViewById(R.id.camera_frame);
        captureTypeTextView = view.findViewById(R.id.capture_type);
        instructionTextView = view.findViewById(R.id.instruction);
        takePhotoButton = view.findViewById(R.id.take_photo);
        flashButton = view.findViewById(R.id.flash);
        contentOverlayView = view.findViewById(R.id.content_overlay);
        placeholderView = view.findViewById(R.id.document_placeholder_view);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        model = ViewModelProviders.of(this).get(DocumentCaptureModel.class);

        model.getCameraStateEvent().observe(this, new Observer<Integer>() {

            @Override
            public void onChanged(@Nullable final Integer integer) {
                switch (integer) {
                    case DocumentCaptureModel.CAMERA_STATE_EVENT_OPEN_SUCCESS:
                        cameraLayout = new CameraLayout(getActivity(), R.id.camera_layout, Camera.CameraInfo.CAMERA_FACING_BACK);
                        cameraFrame.addView(cameraLayout);
                        model.setCameraAvailable(true);
                        break;
                    case DocumentCaptureModel.CAMERA_STATE_EVENT_OPEN_FAIL:
                        onCameraInitFailed();
                        break;
                }
            }

        });

        model.getShutterEvent().observe(this, new Observer<Void>() {

            @Override
            public void onChanged(@Nullable final Void aVoid) {
                ObjectAnimator.ofFloat(contentOverlayView, "alpha", 1f, 0f).start();
            }

        });

        model.getPictureTakenEvent().observe(this, new Observer<byte[]>() {

            @Override
            public void onChanged(@Nullable final byte[] data) {
                model.setCameraAvailable(true);
                onPictureTaken(data);
            }

        });

        model.getFlashModes().observe(this, new Observer<FlashModes>() {

            @Override
            public void onChanged(@Nullable final FlashModes flashModes) {
                if (flashModes.getList() == null) {
                    flashButton.setEnabled(false);
                    return;
                }
                if (flashModes.getList().isEmpty()) {
                    flashButton.setEnabled(false);
                    return;
                }

                final String flashMode = flashModes.getActive();
                changeFlashButtonBackground(flashMode);
            }

        });

        model.getCaptureAvailable().observe(this, new Observer<Boolean>() {

            @Override
            public void onChanged(@Nullable final Boolean active) {
                if (active != null) {
                    if (active) {
                        enablePhotoButton();
                    } else {
                        disablePhotoButton();
                    }
                }
            }

        });

        setupFlashButton();
        setupTakePhotoButton();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSIONS);
            return;
        }

        model.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraFrame.removeView(cameraLayout);
        cameraLayout = null;
    }

    protected void setDocumentPlaceholderWidthRatio(final float ratio) {
        placeholderView.setDocumentWidthRatio(ratio);
    }

    protected void setDocumentPlaceholderAspect(final float aspect) {
        placeholderView.setAspect(aspect);
    }


    private void setupFlashButton() {
        flashButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                final List<String> flashModeList = model.getFlashModes().getValue().getList();

                if (flashModeList == null) {
                    return;
                }
                if (flashModeList.isEmpty()) {
                    return;
                }

                final String flashMode = model.nextCameraFlashMode();
                changeFlashButtonBackground(flashMode);
            }

        });
    }

    private void setupTakePhotoButton() {
        takePhotoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                changeDocumentSide(null);
                model.setCameraAvailable(false);
                model.takePicture();
            }

        });
    }

    private void enablePhotoButton() {
        takePhotoButton.setEnabled(true);
        takePhotoButton.setAlpha(1f);
    }

    private void disablePhotoButton() {
        takePhotoButton.setEnabled(false);
        takePhotoButton.setAlpha(0.5f);
    }

    private void changeFlashButtonBackground(final String flashMode) {
        if (flashMode == null) {
            return;
        }

        switch (flashMode) {
            case Camera.Parameters.FLASH_MODE_OFF:
                flashButton.setBackgroundResource(R.drawable.ic_flash_off_white_24dp);
                break;
            case Camera.Parameters.FLASH_MODE_AUTO:
                flashButton.setBackgroundResource(R.drawable.ic_flash_auto_white_24dp);
                break;
            case Camera.Parameters.FLASH_MODE_ON:
                flashButton.setBackgroundResource(R.drawable.ic_flash_on_white_24dp);
                break;
            case Camera.Parameters.FLASH_MODE_RED_EYE:
                flashButton.setBackgroundResource(R.drawable.ic_flash_red_eye_white_24dp);
                break;
            case Camera.Parameters.FLASH_MODE_TORCH:
                flashButton.setBackgroundResource(R.drawable.ic_flash_torch_white_24dp);
                break;
        }
    }

    protected abstract void onCameraInitFailed();

    protected abstract void onPictureTaken(byte[] data);

    protected void changeDocumentSide(final DocumentSide documentSide) {
        model.setDocumentSide(documentSide);

        if (documentSide == null) {
            captureTypeTextView.setVisibility(View.GONE);
            instructionTextView.setVisibility(View.GONE);
            return;
        }

        captureTypeTextView.setVisibility(View.VISIBLE);
        instructionTextView.setVisibility(View.VISIBLE);

        switch (documentSide) {
            case FRONT:
                captureTypeTextView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_document_side_front_white), null, null, null);
                captureTypeTextView.setText(R.string.document_capture_type_front);
                instructionTextView.setText(R.string.document_capture_instruction_front);
                break;
            case BACK:
                captureTypeTextView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_document_side_back_white), null, null, null);
                captureTypeTextView.setText(R.string.document_capture_type_back);
                instructionTextView.setText(R.string.document_capture_instruction_back);
                break;
        }
    }

}
