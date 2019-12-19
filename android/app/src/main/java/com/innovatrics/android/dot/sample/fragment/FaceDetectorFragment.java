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

import com.innovatrics.android.dot.face.DetectedFace;
import com.innovatrics.android.dot.face.FaceFeatureId;
import com.innovatrics.android.dot.face.FaceFeaturePoint;
import com.innovatrics.android.dot.face.FaceImage;
import com.innovatrics.android.dot.face.IcaoAttribute;
import com.innovatrics.android.dot.face.IcaoAttributeId;
import com.innovatrics.android.dot.face.IcaoAttributeRangeStatus;
import com.innovatrics.android.dot.facedetection.FaceDetector;
import com.innovatrics.android.dot.sample.R;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
        faceDetector = new FaceDetector();
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
                List<DetectedFace> faces = faceDetector.detectFaces(FaceImage.create(image), 5);
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
                    final Bitmap croppedImage = face.createCroppedImage();
                    Map<IcaoAttributeId, IcaoAttribute> icaoAttributes = face.createIcaoAttributes(Arrays.asList(IcaoAttributeId.values()));
                    Map<FaceFeatureId, FaceFeaturePoint> faceFeatures = face.createFaceFeatures();
                    final StringBuilder faceDataStringBuilder = new StringBuilder();
                    faceDataStringBuilder.append(createIcaoString(icaoAttributes));
                    faceDataStringBuilder.append("\n\n");
                    faceDataStringBuilder.append(createFeaturesString(faceFeatures));
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
                .append(face.getEyeDistance())
                .append("\ntemplate version: ")
                .append(face.createTemplate().getVersion())
                .append("\n\n");
        return stringBuilder;
    }

    private StringBuilder createIcaoString(Map<IcaoAttributeId, IcaoAttribute> icaoAttributes) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (IcaoAttributeId attributeId : icaoAttributes.keySet()) {
            IcaoAttribute attribute = icaoAttributes.get(attributeId);
            stringBuilder.append(attributeId.name())
                    .append(": ")
                    .append(attribute.getScore())
                    .append(", compliant: ")
                    .append(attribute.getRangeStatus() == IcaoAttributeRangeStatus.IN_RANGE && attribute.isDependenciesFulfilled())
                    .append("\n");
        }
        return stringBuilder;
    }

    private StringBuilder createFeaturesString(Map<FaceFeatureId, FaceFeaturePoint> features) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (FaceFeatureId featureId : features.keySet()) {
            FaceFeaturePoint point = features.get(featureId);
            stringBuilder.append(featureId.name())
                    .append(": x=")
                    .append(point.getX())
                    .append(", y=")
                    .append(point.getY())
                    .append("\n");
        }
        return stringBuilder;
    }

}
