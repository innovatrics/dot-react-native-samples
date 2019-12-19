package com.innovatrics.android.dot.sample.documentreview;

import java.io.Serializable;

public class DocumentReviewedItem implements Serializable {

    private final String fieldId;
    private final int line;
    private final String value;

    public DocumentReviewedItem(final String fieldId, final int line, final String value) {
        this.fieldId = fieldId;
        this.line = line;
        this.value = value;
    }

    public String getFieldId() {
        return fieldId;
    }

    public int getLine() {
        return line;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "DocumentReviewedItem{" +
                "fieldId='" + fieldId + '\'' +
                ", line=" + line +
                ", value='" + value + '\'' +
                '}';
    }

}
