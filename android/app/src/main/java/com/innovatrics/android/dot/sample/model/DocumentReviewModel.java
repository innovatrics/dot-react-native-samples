package com.innovatrics.android.dot.sample.model;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.innovatrics.android.dot.sample.R;
import com.innovatrics.android.dot.sample.documentreview.DocumentItem;
import com.innovatrics.android.dot.sample.documentreview.DocumentReviewCategoryItem;
import com.innovatrics.android.dot.sample.documentreview.DocumentReviewFieldItem;
import com.innovatrics.android.dot.sample.documentreview.DocumentReviewItem;
import com.innovatrics.android.dot.sample.utils.ImageLoader;
import com.innovatrics.android.dot.sample.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DocumentReviewModel extends AndroidViewModel {

    private final float dataConfidenceThreshold;
    private final Uri frontImageUri;
    private final Uri backImageUri;
    private final List<DocumentItem> visualInspectionZoneItemList;
    private final List<DocumentItem> machineReadableZoneItemList;
    private final ExecutorService executorService;
    private final MutableLiveData<Drawable> frontImageDrawable;
    private final MutableLiveData<Drawable> backImageDrawable;
    private final MutableLiveData<DocumentReviewFieldItem> selectedItem;
    private final MutableLiveData<List<DocumentReviewItem>> list;
    private final SingleLiveEvent<Void> itemEditDoneEvent;
    private final MutableLiveData<Integer> uncertainValues;

    public DocumentReviewModel(
            @NonNull final Application application,
            final float dataConfidenceThreshold,
            final Uri frontImageUri,
            final Uri backImageUri,
            final List<DocumentItem> visualInspectionZoneItemList,
            final List<DocumentItem> machineReadableZoneItemList) {
        super(application);

        this.dataConfidenceThreshold = dataConfidenceThreshold;
        this.frontImageUri = frontImageUri;
        this.backImageUri = backImageUri;
        this.visualInspectionZoneItemList = visualInspectionZoneItemList;
        this.machineReadableZoneItemList = machineReadableZoneItemList;
        executorService = Executors.newCachedThreadPool();
        frontImageDrawable = new MutableLiveData<>();
        backImageDrawable = new MutableLiveData<>();
        selectedItem = new MutableLiveData<>();
        list = new MutableLiveData<>();
        itemEditDoneEvent = new SingleLiveEvent<>();
        uncertainValues = new MutableLiveData<>();

        loadFrontImage();
        loadBackImage();
        loadList();
    }

    public MutableLiveData<Drawable> getFrontImageDrawable() {
        return frontImageDrawable;
    }

    public MutableLiveData<Drawable> getBackImageDrawable() {
        return backImageDrawable;
    }

    public MutableLiveData<DocumentReviewFieldItem> getSelectedItem() {
        return selectedItem;
    }

    public MutableLiveData<List<DocumentReviewItem>> getList() {
        return list;
    }

    public SingleLiveEvent<Void> getItemEditDoneEvent() {
        return itemEditDoneEvent;
    }

    public MutableLiveData<Integer> getUncertainValues() {
        return uncertainValues;
    }

    public float getDataConfidenceThreshold() {
        return dataConfidenceThreshold;
    }

    public void clearSelectedItem() {
        selectedItem.setValue(null);
    }

    private void loadFrontImage() {
        ImageLoader.getInstance().loadRoundedBitmapDrawableInBackground(this.getApplication(), frontImageUri, new ImageLoader.Listener() {

            @Override
            public void onLoaded(final Bitmap bitmap) {
            }

            @Override
            public void onLoaded(final Drawable drawable) {
                frontImageDrawable.postValue(drawable);
            }

        });
    }

    private void loadBackImage() {
        ImageLoader.getInstance().loadRoundedBitmapDrawableInBackground(this.getApplication(), backImageUri, new ImageLoader.Listener() {

            @Override
            public void onLoaded(final Bitmap bitmap) {
            }

            @Override
            public void onLoaded(final Drawable drawable) {
                backImageDrawable.postValue(drawable);
            }

        });
    }

    private void loadList() {
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                final List<DocumentReviewItem> list = new ArrayList<>();

                if (visualInspectionZoneItemList != null && !visualInspectionZoneItemList.isEmpty()) {
                    final DocumentReviewCategoryItem vizDocumentReviewCategory = new DocumentReviewCategoryItem();
                    vizDocumentReviewCategory.setName(getApplication().getString(R.string.document_review_category_visual_inspection_zone));
                    list.add(vizDocumentReviewCategory);

                    for (final DocumentItem documentItem : visualInspectionZoneItemList) {
                        final DocumentReviewFieldItem documentReviewFieldItem = new DocumentReviewFieldItem();
                        documentReviewFieldItem.setDocumentSide(documentItem.getDocumentSide());
                        documentReviewFieldItem.setFieldId(documentItem.getFieldId());
                        documentReviewFieldItem.setLine(documentItem.getLine());
                        documentReviewFieldItem.setName(documentItem.getName());
                        documentReviewFieldItem.setValue(documentItem.getValue());
                        documentReviewFieldItem.setScore(documentItem.getScore());
                        documentReviewFieldItem.setRect(documentItem.getRect());
                        list.add(documentReviewFieldItem);
                    }
                }

                if (machineReadableZoneItemList != null && !machineReadableZoneItemList.isEmpty()) {
                    final DocumentReviewCategoryItem mrzDocumentReviewCategory = new DocumentReviewCategoryItem();
                    mrzDocumentReviewCategory.setName(getApplication().getString(R.string.document_review_category_machine_readable_zone));
                    list.add(mrzDocumentReviewCategory);

                    for (final DocumentItem documentItem : machineReadableZoneItemList) {
                        final DocumentReviewFieldItem documentReviewFieldItem = new DocumentReviewFieldItem();
                        documentReviewFieldItem.setDocumentSide(documentItem.getDocumentSide());
                        documentReviewFieldItem.setFieldId(documentItem.getFieldId());
                        documentReviewFieldItem.setLine(documentItem.getLine());
                        documentReviewFieldItem.setName(documentItem.getName());
                        documentReviewFieldItem.setValue(documentItem.getValue());
                        documentReviewFieldItem.setScore(documentItem.getScore());
                        documentReviewFieldItem.setRect(documentItem.getRect());
                        documentReviewFieldItem.setReadonly(true);
                        list.add(documentReviewFieldItem);
                    }
                }

                DocumentReviewModel.this.list.postValue(list);
                uncertainValues.postValue(uncertainValues(list));
            }

        });
    }

    public void updateUncertainValues() {
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                uncertainValues.postValue(uncertainValues());
            }

        });
    }

    /**
     * Get number of uncertain values by score which were not confirmed yet.
     *
     * @return
     */
    private int uncertainValues() {
        return uncertainValues(list.getValue());
    }

    /**
     * Get number of uncertain values by score which were not confirmed yet.
     *
     * @return
     */
    private int uncertainValues(final List<DocumentReviewItem> list) {
        int count = 0;

        for (final DocumentReviewItem item : list) {
            if (!(item instanceof DocumentReviewFieldItem)) {
                continue;
            }

            final DocumentReviewFieldItem fieldItem = (DocumentReviewFieldItem) item;

            if (fieldItem.isReadonly()) {
                continue;
            }

            if (fieldItem.getScore() < dataConfidenceThreshold && !fieldItem.isConfirmed()) {
                count++;
            }
        }

        return count;
    }

}
