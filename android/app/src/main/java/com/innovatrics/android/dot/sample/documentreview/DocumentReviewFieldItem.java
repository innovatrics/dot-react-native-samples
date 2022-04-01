package com.innovatrics.android.dot.sample.documentreview;

import com.innovatrics.android.dot.sample.documentcapture.DocumentSide;
import com.innovatrics.android.dot.sample.documentcapture.Rect;

import java.io.Serializable;

public class DocumentReviewFieldItem extends DocumentReviewItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private DocumentSide documentSide;
    private String fieldId;
    private int line;
    private String name;
    private String value;
    private float score;
    private boolean confirmed;
    private Rect rect;
    private boolean readonly;

    public DocumentSide getDocumentSide() {
        return documentSide;
    }

    public void setDocumentSide(final DocumentSide documentSide) {
        this.documentSide = documentSide;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(final String fieldId) {
        this.fieldId = fieldId;
    }

    public int getLine() {
        return line;
    }

    public void setLine(final int line) {
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public float getScore() {
        return score;
    }

    public void setScore(final float score) {
        this.score = score;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(final boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(final Rect rect) {
        this.rect = rect;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(final boolean readonly) {
        this.readonly = readonly;
    }

}
