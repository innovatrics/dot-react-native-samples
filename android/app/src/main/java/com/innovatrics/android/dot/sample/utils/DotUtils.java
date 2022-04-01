package com.innovatrics.android.dot.sample.utils;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DotUtils {

    private static final String TAG = dotTag(DotUtils.class);

    public DotUtils() {
    }

    public static String dotTag(Class clazz) {
        return "DOT:" + clazz.getSimpleName();
    }

    public static int resolveColorAttrResourceId(Context context, int attrId) {
        TypedValue var2 = new TypedValue();
        context.getTheme().resolveAttribute(attrId, var2, true);
        return var2.resourceId;
    }

    public static float toPixels(Context context, int value, int unit) {
        DisplayMetrics var3 = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(unit, (float)value, var3);
    }

    public static boolean isAnimationSupported() {
        return Build.VERSION.SDK_INT >= 23;
    }

}
