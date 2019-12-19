package com.innovatrics.android.dot.sample.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.innovatrics.android.dot.utils.ImageLoader;

public class PhotoConfirmationModel extends ViewModel {

    private final MutableLiveData<Drawable> imageDrawableLiveData;
    private final ImageLoader.Listener imageLoaderListener;

    public PhotoConfirmationModel() {
        imageDrawableLiveData = new MutableLiveData<>();
        imageLoaderListener = new ImageLoader.Listener() {

            @Override
            public void onLoaded(final Bitmap bitmap) {
            }

            @Override
            public void onLoaded(final Drawable drawable) {
                imageDrawableLiveData.postValue(drawable);
            }

        };

    }

    public LiveData<Drawable> getImageDrawableLiveData() {
        return imageDrawableLiveData;
    }

    public void loadImage(final Context context, final Uri uri) {
        ImageLoader.getInstance().loadCircleBitmapDrawableInBackground(context, uri, imageLoaderListener);
    }

}
