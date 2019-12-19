package com.innovatrics.android.dot.sample.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.innovatrics.android.dot.dto.Rect;
import com.innovatrics.android.dot.sample.http.dto.Roi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

    /**
     * Convert {@link Roi} to {@link Rect}.
     *
     * @param roi {@link Roi}.
     * @return {@link Rect}.
     */
    public static Rect toRect(final Roi roi) {
        if (roi == null) {
            return null;
        }

        return new Rect(
                roi.getX(),
                roi.getY(),
                roi.getX() + roi.getWidth(),
                roi.getY() + roi.getHeight());
    }

    /**
     * Get byte array from {@link Uri}.
     *
     * @param context Android {@link Context}.
     * @param uri     {@link Uri} of the file to read.
     * @return byte array.
     * @throws IOException
     */
    public static byte[] getBytes(final Context context, final Uri uri) throws IOException {
        final InputStream inputStream = context.getContentResolver().openInputStream(uri);

        try {
            return getBytes(inputStream);
        } finally {
            inputStream.close();
        }
    }

    /**
     * Get byte from {@link InputStream}.
     *
     * @param inputStream {@link InputStream}.
     * @return byte array.
     * @throws IOException
     */
    private static byte[] getBytes(final InputStream inputStream) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        final byte[] bytes;

        try {
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, length);
            }

            bytes = byteArrayOutputStream.toByteArray();
        } finally {
            byteArrayOutputStream.close();
        }

        return bytes;
    }

    /**
     * Get bytes from {@link Bitmap}.
     *
     * @param bitmap {@link Bitmap}.
     * @return byte array.
     * @throws IOException
     */
    public static byte[] getBytes(final Bitmap bitmap) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        final byte[] bytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();

        return bytes;
    }

    /**
     * Save byte array to a file.
     *
     * @param data Data.
     * @param uri  Destination Uri.
     */
    public static void saveByteArray(final byte[] data, final Uri uri) {
        final File file = new File(uri.getPath());

        try {
            final OutputStream os = new FileOutputStream(file);
            os.write(data);
            os.flush();
            os.close();
        } catch (final Exception e) {
            throw new RuntimeException("Unable to store data.", e);
        }
    }

    public static File getTempFile(Context context) {
        try {
            return File.createTempFile("photo_", ".jpg", context.getCacheDir());
        } catch (final IOException e) {
            throw new RuntimeException("Unable to create photo file.");
        }
    }

}
