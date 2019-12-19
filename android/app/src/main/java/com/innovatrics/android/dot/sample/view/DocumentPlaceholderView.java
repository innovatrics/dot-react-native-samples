package com.innovatrics.android.dot.sample.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.innovatrics.android.dot.sample.R;
import com.innovatrics.android.dot.utils.Utils;

public class DocumentPlaceholderView extends View {

    public static final int PLACEHOLDER_STROKE_WIDTH = 2;
    public static final int PLACEHOLDER_CORNER_RADIUS = 15;

    private Paint bgPaint;
    private Paint placeHolderPaint;
    private float placeholderCornerRadiusPx;
    private Path bgPath;
    private Float aspectRatio;
    private Float documentWidthRatio;
    private RectF placeholderRect;

    public DocumentPlaceholderView(Context context) {
        super(context);
        setup(context);
    }

    public DocumentPlaceholderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public DocumentPlaceholderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context);
    }

    private void setup(Context context) {
        bgPaint = new Paint();
        bgPaint.setColor(ContextCompat.getColor(getContext(), R.color.document_capture_placeholder_background));
        bgPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        placeHolderPaint = new Paint();
        placeHolderPaint.setColor(ContextCompat.getColor(context, R.color.document_capture_placeholder_outline));
        placeHolderPaint.setAntiAlias(true);
        placeHolderPaint.setStyle(Paint.Style.STROKE);
        placeHolderPaint.setStrokeWidth(Utils.toPixels(context, PLACEHOLDER_STROKE_WIDTH, TypedValue.COMPLEX_UNIT_DIP));

        placeholderCornerRadiusPx = Utils.toPixels(context, PLACEHOLDER_CORNER_RADIUS, TypedValue.COMPLEX_UNIT_DIP);
    }

    /**
     * Set width to height document Ratio
     *
     * @param aspectRatio width to height aspect ratio
     */
    public void setAspect(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        postCalculateRect();
        invalidate();
    }

    /**
     * Set documentWidthRatio
     *
     * @param documentWidthRatio document width to view width
     */
    public void setDocumentWidthRatio(float documentWidthRatio) {
        this.documentWidthRatio = documentWidthRatio;
        postCalculateRect();
    }

    private void postCalculateRect() {
        post(new Runnable() {
            @Override
            public void run() {
                calculateRect();
                calculateBackgroundPath();
                invalidate();
            }
        });
    }

    private void calculateRect() {
        if (aspectRatio != null && documentWidthRatio != null) {
            float placeholderWidth = getWidth() * documentWidthRatio;
            float placeholderHeight = placeholderWidth / aspectRatio;
            float offsetX = (getWidth() - placeholderWidth) / 2;

            placeholderRect = new RectF(
                    offsetX,
                    (getHeight() - placeholderHeight) / 2,
                    getWidth() - offsetX,
                    (getHeight() + placeholderHeight) / 2
            );
        } else {
            placeholderRect = null;
        }
    }

    private void calculateBackgroundPath() {
        if (placeholderRect != null) {
            bgPath = new Path();
            bgPath.setFillType(Path.FillType.EVEN_ODD);
            bgPath.addRect(0, 0, getWidth(), getHeight(), Path.Direction.CW);
            bgPath.addRoundRect(placeholderRect, placeholderCornerRadiusPx, placeholderCornerRadiusPx, Path.Direction.CW);
        }
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawPath(bgPath, bgPaint);
    }

    private void drawOutline(Canvas canvas) {
        canvas.drawRoundRect(placeholderRect,
                placeholderCornerRadiusPx,
                placeholderCornerRadiusPx,
                placeHolderPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (placeholderRect != null) {
            drawBackground(canvas);
            drawOutline(canvas);
        }
    }
}
