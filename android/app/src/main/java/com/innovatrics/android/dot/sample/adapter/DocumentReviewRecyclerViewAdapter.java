package com.innovatrics.android.dot.sample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.innovatrics.android.dot.sample.R;
import com.innovatrics.android.dot.sample.documentreview.DocumentReviewCategoryItem;
import com.innovatrics.android.dot.sample.documentreview.DocumentReviewFieldItem;
import com.innovatrics.android.dot.sample.documentreview.DocumentReviewItem;

import java.util.List;

public class DocumentReviewRecyclerViewAdapter extends RecyclerView.Adapter<DocumentReviewRecyclerViewAdapter.ViewHolder> {

    public static final int TYPE_CATEGORY = 0;
    public static final int TYPE_FIELD = 1;
    public static final int TYPE_READONLY_FIELD = 2;

    public abstract class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(final View itemView) {
            super(itemView);
        }

        public abstract void bindTo(DocumentReviewItem item);

        public abstract void clear();

    }

    public class CategoryViewHolder extends ViewHolder {

        public final TextView nameTextView;

        public CategoryViewHolder(final View view) {
            super(view);
            nameTextView = view.findViewById(R.id.name);
        }

        @Override
        public void bindTo(final DocumentReviewItem item) {
            clear();

            final DocumentReviewCategoryItem categoryItem = (DocumentReviewCategoryItem) item;

            nameTextView.setText(categoryItem.getName());
        }

        @Override
        public void clear() {
            nameTextView.clearComposingText();
        }

    }

    public class FieldViewHolder extends ViewHolder {

        public final View containerView;
        public final TextView nameTextView;
        public final TextView valueTextView;
        public final ImageView iconImageView;

        public FieldViewHolder(final View view) {
            super(view);
            containerView = view.findViewById(R.id.container);
            nameTextView = view.findViewById(R.id.name);
            valueTextView = view.findViewById(R.id.value);
            iconImageView = view.findViewById(R.id.icon);
        }

        @Override
        public void bindTo(final DocumentReviewItem item) {
            clear();

            final DocumentReviewFieldItem fieldItem = (DocumentReviewFieldItem) item;

            containerView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View v) {
                    selectedItem.setValue(fieldItem);
                }

            });

            if (fieldItem.getScore() < dataConfidenceThreshold && !fieldItem.isConfirmed()) {
                containerView.setBackgroundResource(R.drawable.bg_solid_steal_radius_8dp);
                iconImageView.setVisibility(View.VISIBLE);
            }

            nameTextView.setText(fieldItem.getName());
            valueTextView.setText(fieldItem.getValue());
        }

        @Override
        public void clear() {
            containerView.setOnClickListener(null);
            containerView.setBackgroundDrawable(null);
            nameTextView.clearComposingText();
            valueTextView.clearComposingText();
            iconImageView.setVisibility(View.GONE);
        }

    }

    public class ReadonlyFieldViewHolder extends ViewHolder {

        public final TextView nameTextView;
        public final TextView valueTextView;

        public ReadonlyFieldViewHolder(final View view) {
            super(view);
            nameTextView = view.findViewById(R.id.name);
            valueTextView = view.findViewById(R.id.value);
        }

        @Override
        public void bindTo(final DocumentReviewItem item) {
            clear();

            final DocumentReviewFieldItem fieldItem = (DocumentReviewFieldItem) item;

            nameTextView.setText(fieldItem.getName());
            valueTextView.setText(fieldItem.getValue());
        }

        @Override
        public void clear() {
            nameTextView.clearComposingText();
            valueTextView.clearComposingText();
        }

    }

    private final MutableLiveData<DocumentReviewFieldItem> selectedItem;
    private List<DocumentReviewItem> list;
    private final float dataConfidenceThreshold;

    public DocumentReviewRecyclerViewAdapter(final MutableLiveData<DocumentReviewFieldItem> selectedItem, float dataConfidenceThreshold) {
        this.selectedItem = selectedItem;
        this.dataConfidenceThreshold = dataConfidenceThreshold;
    }

    public void setList(final List<DocumentReviewItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<DocumentReviewItem> getList() {
        return list;
    }

    @Override
    public int getItemViewType(final int position) {
        final DocumentReviewItem item = list.get(position);

        if (item instanceof DocumentReviewCategoryItem) {
            return TYPE_CATEGORY;
        }
        if (item instanceof DocumentReviewFieldItem) {
            final DocumentReviewFieldItem fieldItem = (DocumentReviewFieldItem) item;

            if (fieldItem.isReadonly()) {
                return TYPE_READONLY_FIELD;
            } else {
                return TYPE_FIELD;
            }
        }

        return -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View itemView;

        switch (viewType) {
            case TYPE_CATEGORY:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_document_review_item_category, parent, false);
                return new CategoryViewHolder(itemView);
            case TYPE_FIELD:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_document_review_item_field, parent, false);
                return new FieldViewHolder(itemView);
            case TYPE_READONLY_FIELD:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_document_review_item_readonly_field, parent, false);
                return new ReadonlyFieldViewHolder(itemView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DocumentReviewItem item = list.get(position);

        if (item == null) {
            holder.clear();
            return;
        }

        holder.bindTo(item);
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }

        return list.size();
    }

}
