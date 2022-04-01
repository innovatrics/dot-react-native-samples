package com.innovatrics.android.dot.sample.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.innovatrics.android.dot.sample.R;
import com.innovatrics.dot.face.detection.DetectedFace;
import com.innovatrics.dot.face.detection.FaceDetector;
import com.innovatrics.dot.face.detection.FaceDetectorFactory;
import com.innovatrics.dot.face.image.BgrRawImageFactory;
import com.innovatrics.dot.face.image.BitmapFactory;
import com.innovatrics.dot.face.image.FaceImageFactory;
import com.innovatrics.dot.face.quality.FaceAspects;
import com.innovatrics.dot.face.quality.FaceAttribute;
import com.innovatrics.dot.face.quality.FaceQuality;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FaceDetectorFragment extends Fragment {

    private static final int REQUEST_LOAD_IMAGE = 1;

    private ExecutorService service;
    private FaceDetector faceDetector;
    private LinearLayout scrollContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_face_detector, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scrollContainer = view.findViewById(R.id.scroll_container);
        Button buttonLoadImage = view.findViewById(R.id.button_load_image);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_LOAD_IMAGE);
            }
        });

        service = Executors.newSingleThreadExecutor();
        faceDetector = FaceDetectorFactory.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LOAD_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        scrollContainer.removeAllViews();
                        processResult(data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void processResult(Uri data) throws IOException {
        final Bitmap image = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data);
        service.submit(new Runnable() {
            @Override
            public void run() {
                List<DetectedFace> faces = faceDetector.detect(FaceImageFactory.create(BgrRawImageFactory.create(image),0.03,0.5),5);
                if (faces.size() == 0) {
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView = new TextView(getContext());
                            textView.setText("No faces detected");
                            scrollContainer.addView(textView);
                        }
                    });
                }
                for (int i = 0; i < faces.size(); i++) {
                    final DetectedFace face = faces.get(i);
                    final Bitmap croppedImage = BitmapFactory.create(face.createFullFrontalImage());
                    FaceAspects faceAspects = face.evaluateFaceAspects();
                    FaceQuality faceQuality = face.evaluateFaceQuality();

                    final StringBuilder faceDataStringBuilder = new StringBuilder();
                    faceDataStringBuilder.append(createIcaoString(faceQuality));
                    faceDataStringBuilder.append("\n\n");
                    faceDataStringBuilder.append(createFeaturesString(faceAspects));
                    final int index = i;
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            TextView textIndex = new TextView(getContext());
                            textIndex.setText("Face " + (index + 1));
                            scrollContainer.addView(textIndex);
                            TextView basicText = new TextView(getContext());
                            basicText.setText(createBasicDataString(face));
                            scrollContainer.addView(basicText);
                            ImageView imageViewCroppedImage = new ImageView(getContext());
                            imageViewCroppedImage.setAdjustViewBounds(true);
                            imageViewCroppedImage.setImageBitmap(croppedImage);
                            scrollContainer.addView(imageViewCroppedImage);
                            TextView textViewFaceData = new TextView(getContext());
                            textViewFaceData.setText(faceDataStringBuilder.toString());
                            scrollContainer.addView(textViewFaceData);
                        }
                    });
                }
            }
        });
    }

    private StringBuilder createBasicDataString(DetectedFace face) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("confidence: ")
                .append(face.getConfidence())
                .append("\neye distance: ")
                .append(face.evaluateFaceAspects().getEyeDistance())
                .append("\ntemplate version: ")
                .append(face.createTemplate().getVersion())
                .append("\n\n");
        return stringBuilder;
    }

    private StringBuilder createIcaoString(FaceQuality icaoAttributes) {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Left eye: score:")
                .append(icaoAttributes.getExpression().getEyes().getLeftEye().getScore())
                .append(", compliant: score:")
                .append(icaoAttributes.getExpression().getEyes().getLeftEye().isPreconditionsMet())
                .append("\n");
        stringBuilder.append("Right eye: score:")
                .append(icaoAttributes.getExpression().getEyes().getRightEye().getScore())
                .append(", compliant: ")
                .append(icaoAttributes.getExpression().getEyes().getRightEye().isPreconditionsMet())
                .append("\n");
        stringBuilder.append("Mouth: score:")
                .append(icaoAttributes.getExpression().getMouth().getScore())
                .append(", compliant: ")
                .append(icaoAttributes.getExpression().getMouth().isPreconditionsMet())
                .append("\n");
        stringBuilder.append("Head pitch: angle:")
                .append(icaoAttributes.getHeadPose().getPitch().getAngle())
                .append(", compliant: ")
                .append(icaoAttributes.getHeadPose().getPitch().isPreconditionsMet())
                .append("\n");
        stringBuilder.append("Head roll: angle:")
                .append(icaoAttributes.getHeadPose().getRoll().getAngle())
                .append(", compliant: ")
                .append(icaoAttributes.getHeadPose().getRoll().isPreconditionsMet())
                .append("\n");
        stringBuilder.append("Head yaw: angle:")
                .append(icaoAttributes.getHeadPose().getYaw().getAngle())
                .append(", compliant: ")
                .append(icaoAttributes.getHeadPose().getYaw().isPreconditionsMet())
                .append("\n");
        stringBuilder.append("Glasses: score:")
                .append(icaoAttributes.getWearables().getGlasses().getScore())
                .append(", compliant: ")
                .append(icaoAttributes.getWearables().getGlasses().isPreconditionsMet())
                .append("\n");
        stringBuilder.append("Mask: score:")
                .append(icaoAttributes.getWearables().getMask().getScore())
                .append(", compliant: ")
                .append(icaoAttributes.getWearables().getMask().isPreconditionsMet())
                .append("\n");
        return stringBuilder;
    }

    private StringBuilder createFeaturesString(FaceAspects features) {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Eye distance: ")
                .append(features.getEyeDistance())
                .append("\n");

        return stringBuilder;
    }

}
