package com.innovatrics.android.dot.sample.enums;

import com.innovatrics.android.dot.sample.R;

public enum ErrorCode {

    SERVER_ERROR(R.drawable.ic_modal_server_error, R.string.onboarding_error_server_error),
    INVALID_REQUEST_BODY(R.drawable.ic_modal_server_error, R.string.onboarding_error_invalid_request_body),
    USER_ALREADY_EXISTS(R.drawable.ic_modal_server_error, R.string.onboarding_error_user_already_exists),
    INVALID_BEARER_TOKEN(R.drawable.ic_modal_server_error, R.string.onboarding_error_invalid_bearer_token),
    FIELDS_DO_NOT_MATCH(R.drawable.ic_modal_server_error, R.string.onboarding_error_fields_do_not_match),
    NO_PHOTO_UPLOAD_YET(R.drawable.ic_modal_server_error, R.string.onboarding_error_no_photo_upload_yet),
    NO_DOCUMENT_UPLOAD_YET(R.drawable.ic_modal_server_error, R.string.onboarding_error_no_document_upload_yet),
    NOT_SAME_PERSON(R.drawable.ic_modal_server_error, R.string.onboarding_error_not_same_person),
    NOT_SAME_PERSON_LIVENESS_FACE(R.drawable.ic_modal_server_error, R.string.onboarding_error_not_same_person),
    NOT_SAME_PERSON_DOCUMENT_FACE(R.drawable.ic_modal_server_error, R.string.onboarding_error_not_same_person),
    INVALID_USERID(R.drawable.ic_modal_server_error, R.string.onboarding_error_invalid_userid),
    INVALID_PREDICTION(R.drawable.ic_modal_server_error, R.string.onboarding_error_invalid_prediction),
    UNEXPECTED_ERROR(R.drawable.ic_modal_server_error, R.string.onboarding_error_unexpected_error),
    NO_FACE_DETECTED(R.drawable.ic_modal_server_error, R.string.onboarding_error_no_face_detected),
    PROBE_IMAGE_NO_FACE_DETECTED(R.drawable.ic_modal_server_error, R.string.onboarding_error_probe_image_no_face_detected),
    REFERENCE_IMAGE_NO_FACE_DETECTED(R.drawable.ic_modal_server_error, R.string.onboarding_error_reference_image_no_face_detected),
    MULTIPLE_FACES_DETECTED(R.drawable.ic_modal_server_error, R.string.onboarding_error_multiple_faces_detected),
    INCOMPATIBLE_TEMPLATES(R.drawable.ic_modal_server_error, R.string.onboarding_error_incompatible_templates),
    NOT_ENOUGH_DATA(R.drawable.ic_modal_server_error, R.string.onboarding_error_not_enough_data),
    DOCUMENT_NOT_RECOGNIZED(R.drawable.ic_modal_document_not_recognized, R.string.onboarding_error_document_not_recognized),
    IMAGE_DECODE_FAILED(R.drawable.ic_modal_document_not_recognized, R.string.onboarding_error_image_decode_failed),
    DOCUMENT_DUPLICATE_FRONT(R.drawable.ic_modal_duplicate_front, R.string.onboarding_error_document_duplicate_front),
    DOCUMENT_DUPLICATE_BACK(R.drawable.ic_modal_duplicate_back, R.string.onboarding_error_document_duplicate_back),
    UNABLE_TO_EXPORT_TO_ABIS(0, 0);

    private int iconResourceId;
    private int messageResourceId;

    ErrorCode(final int iconResourceId, final int messageResourceId) {
        this.iconResourceId = iconResourceId;
        this.messageResourceId = messageResourceId;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public int getMessageResourceId() {
        return messageResourceId;
    }

}
