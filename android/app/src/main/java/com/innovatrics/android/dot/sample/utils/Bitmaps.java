package com.innovatrics.android.dot.sample.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import com.innovatrics.android.commons.Logger;

import java.io.File;
import java.io.FileOutputStream;

public class Bitmaps {

    private static final String TAG = DotUtils.dotTag(Bitmaps.class);

    public Bitmaps() {
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int var3 = options.outHeight;
        int var4 = options.outWidth;
        int var5 = 1;
        if (var3 > reqHeight || var4 > reqWidth) {
            int var6 = var3 / 2;

            for(int var7 = var4 / 2; var6 / var5 > reqHeight && var7 / var5 > reqWidth; var5 *= 2) {
            }
        }

        return var5;
    }

    public static Bitmap fromJpeg(byte[] jpeg, int width, int height) {
        if (jpeg != null) {
            BitmapFactory.Options var3 = null;
            if (width > 0 && height > 0) {
                var3 = new BitmapFactory.Options();
                var3.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length, var3);
                var3.inJustDecodeBounds = false;
                var3.inSampleSize = calculateInSampleSize(var3, width, height);
            }

            Bitmap var4 = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length, var3);
            int var5 = getOrientation(jpeg);
            if (var4 != null && var5 != 0) {
                Matrix var6 = new Matrix();
                var6.postRotate((float)var5);
                Bitmap var7 = Bitmap.createBitmap(var4, 0, 0, var4.getWidth(), var4.getHeight(), var6, true);
                var4.recycle();
                return var7;
            } else {
                return var4;
            }
        } else {
            return null;
        }
    }

    public static void saveBitmapAsJpeg(Bitmap bitmap, Uri uri) {
        File var2 = new File(uri.getPath());

        try {
            FileOutputStream var3 = new FileOutputStream(var2);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, var3);
            var3.flush();
            var3.close();
        } catch (Exception var4) {
            Logger.e(TAG, "Unable to save bitmap.", var4);
        }

    }

    public static Bitmap fromJpeg(byte[] jpeg) {
        return fromJpeg(jpeg, 0, 0);
    }

    public static int getOrientation(byte[] jpeg) {
        if (jpeg == null) {
            return 0;
        } else {
            int var1 = 0;
            int var2 = 0;

            while(true) {
                while(true) {
                    int var3;
                    if (var1 + 3 < jpeg.length && (jpeg[var1++] & 255) == 255) {
                        var3 = jpeg[var1] & 255;
                        if (var3 == 255) {
                            continue;
                        }

                        ++var1;
                        if (var3 == 216 || var3 == 1) {
                            continue;
                        }

                        if (var3 != 217 && var3 != 218) {
                            var2 = pack(jpeg, var1, 2, false);
                            if (var2 < 2 || var1 + var2 > jpeg.length) {
                                Logger.e(TAG, "Invalid length");
                                return 0;
                            }

                            if (var3 != 225 || var2 < 8 || pack(jpeg, var1 + 2, 4, false) != 1165519206 || pack(jpeg, var1 + 6, 2, false) != 0) {
                                var1 += var2;
                                var2 = 0;
                                continue;
                            }

                            var1 += 8;
                            var2 -= 8;
                        }
                    }

                    if (var2 > 8) {
                        var3 = pack(jpeg, var1, 4, false);
                        if (var3 != 1229531648 && var3 != 1296891946) {
                            Logger.e(TAG, "Invalid byte order");
                            return 0;
                        }

                        boolean var4 = var3 == 1229531648;
                        int var5 = pack(jpeg, var1 + 4, 4, var4) + 2;
                        if (var5 < 10 || var5 > var2) {
                            Logger.e(TAG, "Invalid offset");
                            return 0;
                        }

                        var1 += var5;
                        var2 -= var5;

                        for(var5 = pack(jpeg, var1 - 2, 2, var4); var5-- > 0 && var2 >= 12; var2 -= 12) {
                            var3 = pack(jpeg, var1, 2, var4);
                            if (var3 == 274) {
                                int var6 = pack(jpeg, var1 + 8, 2, var4);
                                switch(var6) {
                                    case 1:
                                        return 0;
                                    case 2:
                                    case 4:
                                    case 5:
                                    case 7:
                                    default:
                                        Logger.i(TAG, "Unsupported orientation");
                                        return 0;
                                    case 3:
                                        return 180;
                                    case 6:
                                        return 90;
                                    case 8:
                                        return 270;
                                }
                            }

                            var1 += 12;
                        }
                    }

                    return 0;
                }
            }
        }
    }

    private static int pack(byte[] bytes, int offset, int length, boolean littleEndian) {
        byte var4 = 1;
        if (littleEndian) {
            offset += length - 1;
            var4 = -1;
        }

        int var5;
        for(var5 = 0; length-- > 0; offset += var4) {
            var5 = var5 << 8 | bytes[offset] & 255;
        }

        return var5;
    }

    public static Bitmap mirrorVertical(Bitmap bitmap) {
        Matrix var1 = new Matrix();
        var1.postScale(-1.0F, 1.0F, (float)bitmap.getWidth() / 2.0F, (float)bitmap.getHeight() / 2.0F);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), var1, true);
    }

}
