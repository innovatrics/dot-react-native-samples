package com.innovatrics.android.dot.sample.http;

import com.innovatrics.android.dot.sample.http.dto.OnboardingGenerateLivenessPositionsResponse;
import com.innovatrics.android.dot.sample.http.dto.OnboardingReviewRequest;
import com.innovatrics.android.dot.sample.http.dto.OnboardingStartRequest;
import com.innovatrics.android.dot.sample.http.dto.OnboardingStartResponse;
import com.innovatrics.android.dot.sample.http.dto.OnboardingUploadDocumentImagesRequest;
import com.innovatrics.android.dot.sample.http.dto.OnboardingUploadDocumentImagesResponse;
import com.innovatrics.android.dot.sample.http.dto.OnboardingUploadFaceImageRequest;
import com.innovatrics.android.dot.sample.http.dto.OnboardingUploadLivenessImagesRequest;
import com.innovatrics.android.dot.sample.http.dto.SignInStartRequest;
import com.innovatrics.android.dot.sample.http.dto.SignInStartResponse;
import com.innovatrics.android.dot.sample.http.dto.SignInUploadFaceImageRequest;
import com.innovatrics.android.dot.sample.http.dto.SignInVerifyResponse;
import com.innovatrics.android.dot.sample.http.dto.UserDetailResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Webservice {

    @POST("onboarding/start")
    Call<OnboardingStartResponse> onboardingStart(@Body OnboardingStartRequest request);

    @POST("onboarding/upload/face-image")
    Call<Void> onboardingUploadFaceImage(@Header("Authorization") String authorization, @Body OnboardingUploadFaceImageRequest request);

    @POST("onboarding/generate/liveness-positions")
    Call<OnboardingGenerateLivenessPositionsResponse> onboardingGenerateLivenessPositions(@Header("Authorization") String authorization);

    @POST("onboarding/upload/liveness-images")
    Call<Void> onboardingUploadLivenessImages(@Header("Authorization") String authorization, @Body OnboardingUploadLivenessImagesRequest request);

    @POST("onboarding/upload/document-images")
    Call<OnboardingUploadDocumentImagesResponse> onboardingUploadDocumentImages(@Header("Authorization") String authorization, @Body OnboardingUploadDocumentImagesRequest request);

    @POST("onboarding/review")
    Call<Void> onboardingReview(@Header("Authorization") String authorization, @Body OnboardingReviewRequest request);

    @POST("onboarding/verify")
    Call<Void> onboardingVerify(@Header("Authorization") String authorization);

    @POST("sign-in/start")
    Call<SignInStartResponse> signInStart(@Body SignInStartRequest request);

    @POST("sign-in/upload/face-image")
    Call<Void> signInUploadFaceImage(@Header("Authorization") String authorization, @Body SignInUploadFaceImageRequest request);

    @POST("sign-in/verify")
    Call<SignInVerifyResponse> signInVerify(@Header("Authorization") String authorization);

    @GET("user/detail")
    Call<UserDetailResponse> userDetail(@Header("Authorization") String authorization);

}
