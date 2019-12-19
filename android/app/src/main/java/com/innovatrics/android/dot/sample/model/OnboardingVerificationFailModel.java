package com.innovatrics.android.dot.sample.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.innovatrics.android.dot.utils.ImageLoader;

public class OnboardingVerificationFailModel extends ViewModel {

    private final MutableLiveData<Bitmap> documentBitmapLiveData;
    private final MutableLiveData<Bitmap> selfieBitmapLiveData;
    private final ImageLoader.Listener documentImageLoaderListener;
    private final ImageLoader.Listener selfieImageLoaderListener;

    public OnboardingVerificationFailModel() {
        documentBitmapLiveData = new MutableLiveData<>();
        selfieBitmapLiveData = new MutableLiveData<>();

        documentImageLoaderListener = new ImageLoader.Listener() {

            @Override
            public void onLoaded(final Bitmap bitmap) {
                documentBitmapLiveData.postValue(bitmap);
            }

            @Override
            public void onLoaded(final Drawable drawable) {
            }

        };

        selfieImageLoaderListener = new ImageLoader.Listener() {

            @Override
            public void onLoaded(final Bitmap bitmap) {
                selfieBitmapLiveData.postValue(bitmap);
            }

            @Override
            public void onLoaded(final Drawable drawable) {
            }

        };
    }

    public MutableLiveData<Bitmap> getDocumentBitmapLiveData() {
        return documentBitmapLiveData;
    }

    public MutableLiveData<Bitmap> getSelfieBitmapLiveData() {
        return selfieBitmapLiveData;
    }

    public void loadDocumentImage(final Context context, final Uri uri) {
        ImageLoader.getInstance().loadBitmapInBackground(context, uri, documentImageLoaderListener);
    }

    public void loadSelfieImage(final Context context, final Uri uri) {
        ImageLoader.getInstance().loadBitmapInBackground(context, uri, selfieImageLoaderListener);
    }

}
