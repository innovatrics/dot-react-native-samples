package com.innovatrics.android.dot.sample.utils;

import com.innovatrics.android.dot.sample.documentreview.DocumentReviewFieldItem;
import com.innovatrics.android.dot.sample.documentreview.DocumentReviewItem;
import com.innovatrics.android.dot.sample.documentreview.DocumentReviewedItem;

import java.util.ArrayList;
import java.util.List;

public final class DocumentUtils {

    /**
     * Convert {@link List} of {@link DocumentReviewItem} to {@link List} of {@link DocumentReviewedItem}.
     *
     * @param sourceList Items from Document Review Screen.
     * @return Reviewed items as an output from Document Review Screen.
     */
    public static List<DocumentReviewedItem> toDocumentReviewedItemList(final List<DocumentReviewItem> sourceList) {
        final List<DocumentReviewedItem> targetList = new ArrayList<>();

        for (final DocumentReviewItem documentReviewItem : sourceList) {
            if (documentReviewItem instanceof DocumentReviewFieldItem) {
                final DocumentReviewFieldItem documentReviewFieldItem = ((DocumentReviewFieldItem) documentReviewItem);

                if (documentReviewFieldItem.isReadonly()) {
                    continue;
                }

                final DocumentReviewedItem documentReviewedItem = new DocumentReviewedItem(
                        documentReviewFieldItem.getFieldId(),
                        documentReviewFieldItem.getLine(),
                        documentReviewFieldItem.getValue());
                targetList.add(documentReviewedItem);
            }
        }

        return targetList;
    }

}
