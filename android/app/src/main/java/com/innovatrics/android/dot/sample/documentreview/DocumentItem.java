package com.innovatrics.android.dot.sample.documentreview;

import com.innovatrics.android.dot.dto.Rect;
import com.innovatrics.android.dot.sample.documentcapture.DocumentSide;

import java.io.Serializable;

public class DocumentItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private DocumentSide documentSide;
    private String fieldId;
    private int line;
    private String name;
    private String value;
    private float score;
    private Rect rect;

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

    public Rect getRect() {
        return rect;
    }

    public void setRect(final Rect rect) {
        this.rect = rect;
    }

}
