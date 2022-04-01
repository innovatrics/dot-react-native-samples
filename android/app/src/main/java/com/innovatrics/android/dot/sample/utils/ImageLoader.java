package com.innovatrics.android.dot.sample.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import com.innovatrics.android.commons.Logger;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
    private static final String TAG = DotUtils.dotTag(ImageLoader.class);
    private static final float CORNER_RADIUS_RATIO = 0.06F;
    private final ExecutorService executorService;

    private ImageLoader() {
        this.executorService = Executors.newCachedThreadPool();
    }

    public static ImageLoader getInstance() {
        return ImageLoader.InstanceHolder.INSTANCE;
    }

    public void loadBitmapInBackground(final Context context, final Uri uri, final ImageLoader.Listener listener) {
        if (uri != null) {
            this.executorService.execute(new Runnable() {
                public void run() {
                    try {
                        Bitmap var1 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                        listener.onLoaded(var1);
                    } catch (IOException var2) {
                        Logger.e(TAG, "Unable to read image from Uri: " + uri.getPath(), var2);
                    }

                }
            });
        }
    }

    public void loadBitmapDrawableInBackground(final Uri uri, final ImageLoader.Listener listener) {
        if (uri != null) {
            this.executorService.execute(new Runnable() {
                public void run() {
                    Drawable var1 = Drawable.createFromPath(uri.getPath());
                    listener.onLoaded(var1);
                }
            });
        }
    }

    public void loadBitmapDrawableWithBorderInBackground(final Context context, final Uri uri, final ImageLoader.Listener listener) {
        if (uri != null) {
            this.executorService.execute(new Runnable() {
                public void run() {
                    try {
                        float var1 = DotUtils.toPixels(context, 8, 1);
                        Bitmap var2 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                        Bitmap var3 = ImageLoader.this.addBorder(var2, (int)var1);
                        BitmapDrawable var4 = new BitmapDrawable(context.getResources(), var3);
                        listener.onLoaded((Drawable)var4);
                    } catch (IOException var5) {
                        Logger.e(TAG, "Unable to read image from Uri: " + uri.getPath(), var5);
                    }

                }
            });
        }
    }

    public void loadRoundedBitmapDrawableInBackground(final Context context, final Uri uri, final ImageLoader.Listener listener) {
        if (uri != null) {
            this.executorService.execute(new Runnable() {
                public void run() {
                    try {
                        Bitmap var1 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                        RoundedBitmapDrawable var2 = RoundedBitmapDrawableFactory.create(context.getResources(), var1);
                        float var3 = Math.min((float)var1.getWidth(), (float)var1.getHeight()) * 0.06F;
                        var2.setCornerRadius(var3);
                        listener.onLoaded((Drawable)var2);
                    } catch (IOException var4) {
                        Logger.e(TAG, "Unable to read image from Uri: " + uri.getPath(), var4);
                    }

                }
            });
        }
    }

    public void loadCircleBitmapDrawableInBackground(final Context context, final Uri uri, final ImageLoader.Listener listener) {
        if (uri != null) {
            this.executorService.execute(new Runnable() {
                public void run() {
                    try {
                        Bitmap var1 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                        Bitmap var2;
                        if (var1.getWidth() >= var1.getHeight()) {
                            var2 = Bitmap.createBitmap(var1, var1.getWidth() / 2 - var1.getHeight() / 2, 0, var1.getHeight(), var1.getHeight());
                        } else {
                            var2 = Bitmap.createBitmap(var1, 0, var1.getHeight() / 2 - var1.getWidth() / 2, var1.getWidth(), var1.getWidth());
                        }

                        RoundedBitmapDrawable var3 = RoundedBitmapDrawableFactory.create(context.getResources(), var2);
                        var3.setCircular(true);
                        listener.onLoaded((Drawable)var3);
                    } catch (IOException var4) {
                        Logger.e(TAG, "Unable to read image from Uri: " + uri.getPath(), var4);
                    }

                }
            });
        }
    }

    private Bitmap addBorder(Bitmap bitmap, int borderSize) {
        Bitmap var3 = Bitmap.createBitmap(bitmap.getWidth() + borderSize * 2, bitmap.getHeight() + borderSize * 2, bitmap.getConfig());
        Canvas var4 = new Canvas(var3);
        var4.drawColor(-1);
        var4.drawBitmap(bitmap, (float)borderSize, (float)borderSize, (Paint)null);
        return var3;
    }

    private static class InstanceHolder {
        private static final ImageLoader INSTANCE = new ImageLoader();

        private InstanceHolder() {
        }
    }

    public interface Listener {
        void onLoaded(Bitmap var1);

        void onLoaded(Drawable var1);
    }
}
