package com.innovatrics.android.dot.sample.model;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.innovatrics.android.dot.sample.documentcapture.DocumentSide;
import com.innovatrics.android.dot.sample.documentreview.DocumentItem;
import com.innovatrics.android.dot.sample.documentreview.DocumentReviewedItem;
import com.innovatrics.android.dot.sample.dto.OnboardingData;
import com.innovatrics.android.dot.sample.dto.OnboardingDocumentData;
import com.innovatrics.android.dot.sample.dto.OnboardingDocumentReviewedData;
import com.innovatrics.android.dot.sample.enums.ErrorCode;
import com.innovatrics.android.dot.sample.http.CallbackImpl;
import com.innovatrics.android.dot.sample.http.Webservice;
import com.innovatrics.android.dot.sample.http.WebserviceCallback;
import com.innovatrics.android.dot.sample.http.WebserviceFactory;
import com.innovatrics.android.dot.sample.http.dto.*;
import com.innovatrics.android.dot.sample.utils.Bitmaps;
import com.innovatrics.android.dot.sample.utils.SingleLiveEvent;
import com.innovatrics.android.dot.sample.utils.Utils;
import com.innovatrics.dot.face.detection.DetectedFace;
import com.innovatrics.dot.face.image.BitmapFactory;
import com.innovatrics.dot.face.liveness.eyegaze.Segment;
import com.innovatrics.dot.face.liveness.eyegaze.SegmentImage;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OnboardingModel extends AndroidViewModel {

    // These constants serve for selecting document type. See DOT OCR Server API for model listing and DOT Core Server API for Machine Readable Zone parsing.
    // These values represent Slovak Identity Card. Replace them to precess other type of document.
    // DOCUMENT_MRZ_TYPE set to null if document does not container Machine Readable Zone.
    public static final String DOCUMENT_MODEL_METADATA_ID = "fd3d8ded-7bfb-4cd6-8012-d88fffee6ee7";
    public static final int DOCUMENT_PAGE_COUNT = 2;
    public static final String DOCUMENT_MRZ_TYPE = "TD1";
    // End of document selecting constants

    public static final int SCREEN_INSTRUCTIONS = 0;
    public static final int SCREEN_DOCUMENT_CAPTURE = 1;
    public static final int SCREEN_DOCUMENT_REVIEW = 2;
    public static final int SCREEN_FACE_CAPTURE = 3;
    public static final int SCREEN_LIVENESS_CHECK = 4;
    public static final int SCREEN_VERIFICATION_FAIL = 5;

    private static final String TAG = OnboardingModel.class.getSimpleName();
    private static final float DOCUMENT_WIDTH_TOLERANCE = 0.1f;
    private static final int DOCUMENT_PHOTO_TARGET_HEIGHT = 1024;
    private static final float LIVENESS_CHECK_THRESHOLD = 0.9999781847f;

    private final String appServerUrl;
    private final String userId;
    private final Map<DocumentSide, Uri> documentUriMap;
    private final Uri documentFaceUri;
    private final Uri faceCaptureUri;
    private final ExecutorService executorService;
    private final MutableLiveData<Integer> screenLiveData;
    private final MutableLiveData<DocumentSide> documentSideLiveData;
    private final SingleLiveEvent<Void> processingStartedEvent;
    private final SingleLiveEvent<ErrorCode> errorEvent;
    private final SingleLiveEvent<Void> livenessCheckFailEvent;
    private final SingleLiveEvent<Void> onboardingCancelEvent;
    private final SingleLiveEvent<ErrorCode> onboardingDoneEvent;
    private final Webservice webservice;
    private final OkHttpClient okHttpClient;
    private final List<Segment> segmentConfigurationList;

    private String bearerToken;
    private Float documentWidthRatio;
    private Float documentAspectRatio;
    private OnboardingDocumentData onboardingDocumentData;
    private OnboardingDocumentReviewedData onboardingDocumentReviewedData;
    private boolean documentCaptureDone;
    private boolean faceCaptureDone;

    public OnboardingModel(final Application application, final String appServerUrl, final String userId) {
        super(application);
        this.appServerUrl = appServerUrl;
        this.userId = userId;

        documentUriMap = new HashMap<>();
        documentFaceUri = Uri.fromFile(createCacheFile());
        faceCaptureUri = Uri.fromFile(createCacheFile());
        executorService = Executors.newCachedThreadPool();
        screenLiveData = new MutableLiveData<>();
        documentSideLiveData = new MutableLiveData<>();
        processingStartedEvent = new SingleLiveEvent<>();
        errorEvent = new SingleLiveEvent<>();
        livenessCheckFailEvent = new SingleLiveEvent<>();
        onboardingCancelEvent = new SingleLiveEvent<>();
        onboardingDoneEvent = new SingleLiveEvent<>();

        webservice = WebserviceFactory.create(application, appServerUrl);

        okHttpClient = new OkHttpClient();

        segmentConfigurationList = new ArrayList<>();

        final OnboardingStartRequest onboardingStartRequest = new OnboardingStartRequest();
        onboardingStartRequest.setUserId(userId);

        webservice.onboardingStart(onboardingStartRequest).enqueue(new CallbackImpl<>(new WebserviceCallback<OnboardingStartResponse>() {

            @Override
            public void onResponse(final OnboardingStartResponse response) {
                bearerToken = response.getBearerToken();
                screenLiveData.postValue(SCREEN_INSTRUCTIONS);
            }

            @Override
            public void onError(final ErrorCode errorCode) {
                errorEvent.postValue(errorCode);
            }

        }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }

    public Uri getDocumentFaceUri() {
        return documentFaceUri;
    }

    public Uri getFaceCaptureUri() {
        return faceCaptureUri;
    }

    public LiveData<Integer> getScreenLiveData() {
        return screenLiveData;
    }

    public LiveData<DocumentSide> getDocumentSideLiveData() {
        return documentSideLiveData;
    }

    public LiveData<Void> getProcessingStartedEvent() {
        return processingStartedEvent;
    }

    public LiveData<ErrorCode> getErrorEvent() {
        return errorEvent;
    }

    public SingleLiveEvent<Void> getLivenessCheckFailEvent() {
        return livenessCheckFailEvent;
    }

    public LiveData<Void> getOnboardingCancelEvent() {
        return onboardingCancelEvent;
    }

    public LiveData<ErrorCode> getOnboardingDoneEvent() {
        return onboardingDoneEvent;
    }

    public List<Segment> getSegmentConfigurationList() {
        return segmentConfigurationList;
    }

    public void setDocumentWidthRatio(final Float documentWidthRatio) {
        this.documentWidthRatio = documentWidthRatio;
    }

    public void setDocumentAspectRatio(final Float documentAspectRatio) {
        this.documentAspectRatio = documentAspectRatio;
    }

    public OnboardingDocumentData getOnboardingDocumentData() {
        return onboardingDocumentData;
    }

    private java.io.File createCacheFile() {
        try {
            return java.io.File.createTempFile("photo_", ".jpg", getApplication().getCacheDir());
        } catch (final IOException e) {
            throw new RuntimeException("Unable to create file.");
        }
    }

    public void startDocumentCapture() {
        prepareDocumentCapture();
        screenLiveData.postValue(SCREEN_DOCUMENT_CAPTURE);
    }

    public void prepareDocumentCapture() {
        onboardingDocumentData = new OnboardingDocumentData();
        onboardingDocumentData.setUriStringMap(new HashMap<DocumentSide, String>());
        onboardingDocumentData.setVisualInspectionZoneItemList(new ArrayList<DocumentItem>());
        onboardingDocumentData.setMachineReadableZoneItemList(new ArrayList<DocumentItem>());
        onboardingDocumentReviewedData = null;
        documentSideLiveData.postValue(DocumentSide.FRONT);
    }

    public void processCapturedDocumentPageInBackground(final Bitmap data) {
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                final DocumentSide documentSide = documentSideLiveData.getValue();

                if (documentSide == documentSide.FRONT && DOCUMENT_PAGE_COUNT == 2) {
                    documentSideLiveData.postValue(DocumentSide.BACK);
                } else {
                    processingStartedEvent.postValue(null);
                }

                processCapturedDocumentPage(data, documentSide);
            }

        });
    }

    private synchronized void processCapturedDocumentPage(final Bitmap data, final DocumentSide documentSide) {
        final Uri uri;

        if (documentUriMap.containsKey(documentSide)) {
            uri = documentUriMap.get(documentSide);
        } else {
            uri = Uri.fromFile(createCacheFile());
            documentUriMap.put(documentSide, uri);
        }

        if (documentWidthRatio != null && documentAspectRatio != null) {
            final Bitmap bitmap = cropAndScaleDown(data);
            Bitmaps.saveBitmapAsJpeg(bitmap, uri);
        } else {
            Bitmaps.saveBitmapAsJpeg(data, uri);
        }

        if (documentSide == DocumentSide.FRONT && DOCUMENT_PAGE_COUNT == 2) {
            return;
        }

        postProcessCapturedDocumentPage();
    }

    private void postProcessCapturedDocumentPage() {
        final List<Image> imageList = new ArrayList<>();

        for (final Map.Entry<DocumentSide, Uri> entry : documentUriMap.entrySet()) {
            final byte[] data;

            try {
                data = Utils.getBytes(getApplication(), entry.getValue());
            } catch (final IOException e) {
                Log.e(TAG, "Unable to read document image.", e);
                errorEvent.postValue(ErrorCode.UNEXPECTED_ERROR);
                return;
            }

            final Image image = new Image();
            image.setName(entry.getKey().name());
            image.setData(data);

            imageList.add(image);
        }

        final OnboardingUploadDocumentImagesRequest request = new OnboardingUploadDocumentImagesRequest();
        request.setImageList(imageList);
        request.setModelMetadataIdList(new ArrayList<String>());
        request.getModelMetadataIdList().add(DOCUMENT_MODEL_METADATA_ID);
        request.setMrzType(DOCUMENT_MRZ_TYPE);

        webservice.onboardingUploadDocumentImages("Bearer " + bearerToken, request).enqueue(new CallbackImpl<>(new WebserviceCallback<OnboardingUploadDocumentImagesResponse>() {

            @Override
            public void onResponse(final OnboardingUploadDocumentImagesResponse response) {
                boolean frontSide = false;
                boolean backSide = false;

                for (final DocumentPage documentPage : response.getDocumentPageList()) {
                    if (documentPage.getErrorName() != null) {
                        errorEvent.postValue(ErrorCode.DOCUMENT_NOT_RECOGNIZED);
                        return;
                    }

                    if (TextUtils.equals(documentPage.getPrediction(), "front")) {
                        if (frontSide) {
                            errorEvent.postValue(ErrorCode.DOCUMENT_DUPLICATE_FRONT);
                            return;
                        } else {
                            frontSide = true;
                        }
                    }

                    if (TextUtils.equals(documentPage.getPrediction(), "back")) {
                        if (backSide) {
                            errorEvent.postValue(ErrorCode.DOCUMENT_DUPLICATE_BACK);
                            return;
                        } else {
                            backSide = true;
                        }
                    }

                    if (documentPage.getTextFieldList() != null) {
                        for (final TextField textField : documentPage.getTextFieldList()) {
                            for (int i = 0; i < textField.getTextFieldLineList().size(); i++) {
                                final TextFieldLine textFieldLine = textField.getTextFieldLineList().get(i);
                                final StringBuilder name = new StringBuilder(textField.getId());

                                if (i > 0) {
                                    name.append(" (").append(i + 1).append(")");
                                }

                                final DocumentItem item = new DocumentItem();
                                item.setDocumentSide(TextUtils.equals(documentPage.getPrediction(), "front") ? DocumentSide.FRONT : DocumentSide.BACK);
                                item.setFieldId(textField.getId());
                                item.setLine(i);
                                item.setName(name.toString());
                                item.setValue(textFieldLine.getValue());
                                item.setScore(textFieldLine.getConfidence());
                                item.setRect(Utils.toRect(textFieldLine.getRoi()));
                                onboardingDocumentData.getVisualInspectionZoneItemList().add(item);
                            }
                        }
                    }

                    if (documentPage.getMrzFieldList() != null) {
                        for (final MrzField mrzField : documentPage.getMrzFieldList()) {
                            final DocumentItem item = new DocumentItem();
                            item.setDocumentSide(TextUtils.equals(documentPage.getPrediction(), "front") ? DocumentSide.FRONT : DocumentSide.BACK);
                            item.setFieldId(mrzField.getId());
                            item.setLine(0);
                            item.setName(mrzField.getId());
                            item.setValue(mrzField.getValue());
                            onboardingDocumentData.getMachineReadableZoneItemList().add(item);
                        }
                    }
                }

                loadDocumentImagesInBackground();
            }

            @Override
            public void onError(final ErrorCode errorCode) {
                errorEvent.postValue(errorCode);
            }

        }));
    }

    private void loadDocumentImagesInBackground() {
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    if (documentUriMap.containsKey(DocumentSide.FRONT)) {
                        final okhttp3.Request request = new Request.Builder()
                                .url(appServerUrl + "/user/document/image/prediction/front")
                                .header("Authorization", "Bearer " + bearerToken)
                                .build();
                        final okhttp3.Response response = okHttpClient.newCall(request).execute();
                        final Uri uri = documentUriMap.get(DocumentSide.FRONT);

                        Utils.saveByteArray(response.body().bytes(), uri);
                        onboardingDocumentData.getUriStringMap().put(DocumentSide.FRONT, uri.toString());
                    }

                    if (documentUriMap.containsKey(DocumentSide.BACK)) {
                        final okhttp3.Request request = new Request.Builder()
                                .url(appServerUrl + "/user/document/image/prediction/back")
                                .header("Authorization", "Bearer " + bearerToken)
                                .build();
                        final okhttp3.Response response = okHttpClient.newCall(request).execute();
                        final Uri uri = documentUriMap.get(DocumentSide.BACK);

                        Utils.saveByteArray(response.body().bytes(), uri);
                        onboardingDocumentData.getUriStringMap().put(DocumentSide.BACK, uri.toString());
                    }

                    final okhttp3.Request request = new Request.Builder()
                            .url(appServerUrl + "/user/document/image/face")
                            .header("Authorization", "Bearer " + bearerToken)
                            .build();
                    final okhttp3.Response response = okHttpClient.newCall(request).execute();
                    Utils.saveByteArray(response.body().bytes(), documentFaceUri);

                    screenLiveData.postValue(SCREEN_DOCUMENT_REVIEW);
                } catch (final Exception e) {
                    Log.e(TAG, "Unable to load document photo.", e);
                    errorEvent.postValue(ErrorCode.UNEXPECTED_ERROR);
                }
            }

        });
    }

    private Bitmap cropAndScaleDown(final Bitmap bitmap) {
        final Bitmap croppedBitmap = crop(bitmap);
        final Bitmap scaledBitmap = scaleDown(croppedBitmap);

        Log.i(TAG, "Input photo size: " + bitmap.getWidth() + " x " + bitmap.getHeight());
        Log.i(TAG, "Cropped photo size: " + croppedBitmap.getWidth() + " x " + croppedBitmap.getHeight());
        Log.i(TAG, "Resized photo size: " + scaledBitmap.getWidth() + " x " + scaledBitmap.getHeight());

        return scaledBitmap;
    }

    private Bitmap crop(final Bitmap bitmap) {
        final float activeWidthRatioExtended = Math.min(1.0f, documentWidthRatio + DOCUMENT_WIDTH_TOLERANCE);
        final int width = (int) (bitmap.getWidth() * activeWidthRatioExtended);
        final int height = (int) (width / documentAspectRatio);
        final int offsetX = (int) (bitmap.getWidth() * (1 - activeWidthRatioExtended)) / 2;
        final int offsetY = (bitmap.getHeight() - height) / 2;

        return Bitmap.createBitmap(bitmap, offsetX, offsetY, width, height);
    }

    private Bitmap scaleDown(final Bitmap bitmap) {
        if (bitmap.getHeight() < DOCUMENT_PHOTO_TARGET_HEIGHT) {
            return bitmap;
        }

        final float ratio = (float) bitmap.getWidth() / (float) bitmap.getHeight();
        final int targetWidth = (int) (DOCUMENT_PHOTO_TARGET_HEIGHT * ratio);
        return Bitmap.createScaledBitmap(bitmap, targetWidth, DOCUMENT_PHOTO_TARGET_HEIGHT, true);
    }

    public void documentCaptureDone(final List<DocumentReviewedItem> documentReviewedItemList) {
        Collections.sort(documentReviewedItemList, new Comparator<DocumentReviewedItem>() {

            @Override
            public int compare(final DocumentReviewedItem o1, final DocumentReviewedItem o2) {
                final int compareFieldIds = o1.getFieldId().compareTo(o2.getFieldId());

                if (compareFieldIds == 0) {
                    if (o1.getLine() < o2.getLine()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }

                return compareFieldIds;
            }

        });

        final Map<String, List<String>> map = new HashMap<>();

        for (final DocumentReviewedItem documentReviewedItem : documentReviewedItemList) {
            final List<String> list;

            if (map.containsKey(documentReviewedItem.getFieldId())) {
                list = map.get(documentReviewedItem.getFieldId());
            } else {
                list = new ArrayList<>();
                map.put(documentReviewedItem.getFieldId(), list);
            }

            list.add(documentReviewedItem.getValue());
        }

        final List<ReviewField> reviewFieldList = new ArrayList<>();

        for (final Map.Entry<String, List<String>> entry : map.entrySet()) {
            final ReviewField reviewField = new ReviewField();
            reviewField.setId(entry.getKey());
            reviewField.setLineList(entry.getValue());
            reviewFieldList.add(reviewField);
        }

        final OnboardingReviewRequest request = new OnboardingReviewRequest();
        request.setReviewFieldList(reviewFieldList);

        webservice.onboardingReview("Bearer " + bearerToken, request).enqueue(new CallbackImpl<>(new WebserviceCallback<Void>() {

            @Override
            public void onResponse(final Void response) {
                onboardingDocumentReviewedData = new OnboardingDocumentReviewedData();
                onboardingDocumentReviewedData.setUriStringMap(onboardingDocumentData.getUriStringMap());
                onboardingDocumentReviewedData.setList(documentReviewedItemList);

                documentCaptureDone = true;
                resolveState();
            }

            @Override
            public void onError(final ErrorCode errorCode) {
                errorEvent.postValue(errorCode);
            }

        }));
    }

    public void faceCaptureDone(final DetectedFace detectedFace) {
        Log.i("AAAAA", "faceCaptureDone");
        final Bitmap bitmap = BitmapFactory.create(detectedFace.createFullFrontalImage());
        Bitmaps.saveBitmapAsJpeg(bitmap, faceCaptureUri);

        final byte[] faceCapture;

        try {
            faceCapture = Utils.getBytes(getApplication(), faceCaptureUri);
        } catch (final IOException e) {
            Log.e(TAG, "Unable to read image.", e);
            errorEvent.postValue(ErrorCode.UNEXPECTED_ERROR);
            return;
        }

        final OnboardingUploadFaceImageRequest request = new OnboardingUploadFaceImageRequest();
        request.setImage(faceCapture);

        webservice.onboardingUploadFaceImage("Bearer " + bearerToken, request).enqueue(new CallbackImpl<>(new WebserviceCallback<Void>() {

            @Override
            public void onResponse(final Void response) {
                Log.i("AAAAA", "onboardingUploadFaceImage onResponse");
                prepareLivenessCheck();
            }

            @Override
            public void onError(final ErrorCode errorCode) {
                errorEvent.postValue(errorCode);
            }

        }));
    }

    private void prepareLivenessCheck() {
        webservice.onboardingGenerateLivenessPositions("Bearer " + bearerToken).enqueue(new CallbackImpl<>(new WebserviceCallback<OnboardingGenerateLivenessPositionsResponse>() {

            @Override
            public void onResponse(final OnboardingGenerateLivenessPositionsResponse response) {
                Log.i("AAAAA", "onboardingGenerateLivenessPositions onResponse");
                for (final Segment.Corner corner : response.getPositionList()) {
                    segmentConfigurationList.add(Segment.of(corner, 1000));
                }

                screenLiveData.setValue(SCREEN_LIVENESS_CHECK);
            }

            @Override
            public void onError(final ErrorCode errorCode) {
                errorEvent.postValue(errorCode);
            }

        }));
    }

    public void livenessCheckDone(final float score, final List<SegmentImage> segmentImageList) {
        if (score < LIVENESS_CHECK_THRESHOLD) {
            livenessCheckFailEvent.postValue(null);
            return;
        }

        final List<byte[]> imageList = new ArrayList<>();

        for (final SegmentImage segmentImage : segmentImageList) {
            final Bitmap bitmap = BitmapFactory.create(segmentImage.getImage());
            final byte[] image;

            try {
                image = Utils.getBytes(bitmap);
            } catch (final IOException e) {
                Log.e(TAG, "Unable to get image bytes from bitmap.", e);
                errorEvent.postValue(ErrorCode.UNEXPECTED_ERROR);
                return;
            }

            imageList.add(image);
        }

        final OnboardingUploadLivenessImagesRequest request = new OnboardingUploadLivenessImagesRequest();
        request.setImageList(imageList);

        webservice.onboardingUploadLivenessImages("Bearer " + bearerToken, request).enqueue(new CallbackImpl<>(new WebserviceCallback<Void>() {

            @Override
            public void onResponse(final Void response) {
                faceCaptureDone = true;
                resolveState();
            }

            @Override
            public void onError(final ErrorCode errorCode) {
                errorEvent.postValue(errorCode);
            }

        }));
    }

    private void resolveState() {
        if (faceCaptureDone) {
            if (documentCaptureDone) {
                processingStartedEvent.postValue(null);
                webservice.onboardingVerify("Bearer " + bearerToken).enqueue(new CallbackImpl<>(new WebserviceCallback<Void>() {

                    @Override
                    public void onResponse(final Void response) {
                        onboardingDoneEvent.postValue(null);
                    }

                    @Override
                    public void onError(final ErrorCode errorCode) {
                        switch (errorCode) {
                            case NOT_SAME_PERSON:
                                screenLiveData.postValue(SCREEN_VERIFICATION_FAIL);
                                break;
                            case UNABLE_TO_EXPORT_TO_ABIS:
                                onboardingDoneEvent.postValue(errorCode);
                                break;
                            default:
                                errorEvent.postValue(errorCode);
                                break;
                        }
                    }

                }));
            } else {
                screenLiveData.postValue(SCREEN_DOCUMENT_CAPTURE);
            }
        } else {
            screenLiveData.postValue(SCREEN_FACE_CAPTURE);
        }
    }

    public void restartDocumentCapture() {
        documentCaptureDone = false;
        startDocumentCapture();
    }

    public void restartFaceCapture() {
        faceCaptureDone = false;
        screenLiveData.setValue(SCREEN_FACE_CAPTURE);
    }

    public void restartLivenessCheck() {
        screenLiveData.setValue(SCREEN_LIVENESS_CHECK);
    }

    public OnboardingData onboardingData() {
        return new OnboardingData(
                onboardingDocumentReviewedData.getUriStringMap(),
                documentFaceUri.toString(),
                faceCaptureUri.toString(),
                onboardingDocumentReviewedData.getList());
    }

    public void cancel() {
        onboardingCancelEvent.postValue(null);
    }

}
