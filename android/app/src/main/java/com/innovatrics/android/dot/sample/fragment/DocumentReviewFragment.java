package com.innovatrics.android.dot.sample.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.innovatrics.android.dot.dto.Rect;
import com.innovatrics.android.dot.sample.R;
import com.innovatrics.android.dot.sample.adapter.DocumentReviewRecyclerViewAdapter;
import com.innovatrics.android.dot.sample.documentreview.DocumentItem;
import com.innovatrics.android.dot.sample.documentreview.DocumentReviewFieldItem;
import com.innovatrics.android.dot.sample.documentreview.DocumentReviewItem;
import com.innovatrics.android.dot.sample.documentreview.DocumentReviewedItem;
import com.innovatrics.android.dot.sample.model.DocumentReviewModel;
import com.innovatrics.android.dot.sample.model.DocumentReviewModelFactory;
import com.innovatrics.android.dot.sample.utils.DocumentUtils;
import com.innovatrics.android.dot.sample.view.DocumentReviewEditText;
import com.innovatrics.android.dot.utils.Utils;

import java.util.List;

public abstract class DocumentReviewFragment extends Fragment {

    public static final String ARG_FRONT_IMAGE_URI = "front_image_uri";
    public static final String ARG_BACK_IMAGE_URI = "back_image_uri";
    public static final String ARG_VISUAL_INSPECTION_ZONE_ITEMS = "visual_inspection_zone_items";
    public static final String ARG_MACHINE_READABLE_ZONE_ITEMS = "machine_readable_zone_items";
    public static final String ARG_DATA_CONFIDENCE_THRESHOLD = "data_confidence_threshold";

    private static final float ZOOM_FRAGMENT_RATIO = 0.5f;
    private static final float DEFAULT_DATA_CONFIDENCE_THRESHOLD = 0.90f;

    private DocumentReviewModel model;
    private DocumentReviewRecyclerViewAdapter adapter;
    private Animator editFrameViewAnimator;
    private Animator editContainerViewAnimator;
    private View.OnLayoutChangeListener documentImageDetailContainerViewOnLayoutChangeListener;
    private TextView infoBarTextView;
    private TextView subtitleTextView;
    private ImageView documentFrontImageView;
    private ImageView documentBackImageView;
    private RecyclerView recyclerView;
    private Button confirmButton;
    private View editFrameView;
    private View editContainerView;
    private FrameLayout documentImageDetailContainerView;
    private ImageView documentImageDetailView;
    private View documentImageDetailOverlayView;
    private View documentImageDetailWarningIconView;
    private TextView nameTextView;
    private DocumentReviewEditText valueEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_document_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        infoBarTextView = view.findViewById(R.id.info_bar);
        subtitleTextView = view.findViewById(R.id.subtitle);
        documentFrontImageView = view.findViewById(R.id.document_front_image);
        documentBackImageView = view.findViewById(R.id.document_back_image);
        recyclerView = view.findViewById(R.id.recycler_view);
        confirmButton = view.findViewById(R.id.confirm);
        editFrameView = view.findViewById(R.id.edit_frame);
        editContainerView = view.findViewById(R.id.edit_container);
        documentImageDetailContainerView = view.findViewById(R.id.document_image_detail_container);
        documentImageDetailView = view.findViewById(R.id.document_image_detail);
        documentImageDetailOverlayView = view.findViewById(R.id.document_image_detail_overlay);
        documentImageDetailWarningIconView = view.findViewById(R.id.document_image_detail_warning_icon);
        nameTextView = view.findViewById(R.id.name);
        valueEditText = view.findViewById(R.id.value);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Uri documentFrontImageUri = null;
        Uri documentBackImageUri = null;
        List<DocumentItem> visualInspectionZoneItemList = null;
        List<DocumentItem> machineReadableZoneItemList = null;
        float dataConfidenceThreshold = DEFAULT_DATA_CONFIDENCE_THRESHOLD;

        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_FRONT_IMAGE_URI)) {
                documentFrontImageUri = getArguments().getParcelable(ARG_FRONT_IMAGE_URI);
            }
            if (getArguments().containsKey(ARG_BACK_IMAGE_URI)) {
                documentBackImageUri = getArguments().getParcelable(ARG_BACK_IMAGE_URI);
            }
            if (getArguments().containsKey(ARG_VISUAL_INSPECTION_ZONE_ITEMS)) {
                visualInspectionZoneItemList = (List<DocumentItem>) getArguments().getSerializable(ARG_VISUAL_INSPECTION_ZONE_ITEMS);
            }
            if (getArguments().containsKey(ARG_MACHINE_READABLE_ZONE_ITEMS)) {
                machineReadableZoneItemList = (List<DocumentItem>) getArguments().getSerializable(ARG_MACHINE_READABLE_ZONE_ITEMS);
            }
            if (getArguments().containsKey(ARG_DATA_CONFIDENCE_THRESHOLD)) {
                dataConfidenceThreshold = getArguments().getFloat(ARG_DATA_CONFIDENCE_THRESHOLD);
            }
        }

        final DocumentReviewModelFactory documentReviewModelFactory = new DocumentReviewModelFactory(
                getActivity().getApplication(),
                dataConfidenceThreshold,
                documentFrontImageUri,
                documentBackImageUri,
                visualInspectionZoneItemList,
                machineReadableZoneItemList);

        model = ViewModelProviders.of(this, documentReviewModelFactory).get(DocumentReviewModel.class);

        model.getFrontImageDrawable().observe(this, new Observer<Drawable>() {

            @Override
            public void onChanged(@Nullable final Drawable drawable) {
                documentFrontImageView.setImageDrawable(drawable);
            }

        });

        model.getBackImageDrawable().observe(this, new Observer<Drawable>() {

            @Override
            public void onChanged(@Nullable final Drawable drawable) {
                documentBackImageView.setImageDrawable(drawable);
            }

        });

        model.getList().observe(this, new Observer<List<DocumentReviewItem>>() {

            @Override
            public void onChanged(@Nullable final List<DocumentReviewItem> list) {
                adapter.setList(list);
            }

        });

        model.getSelectedItem().observe(this, new Observer<DocumentReviewFieldItem>() {

            @Override
            public void onChanged(@Nullable final DocumentReviewFieldItem documentReviewFieldItem) {
                if (documentReviewFieldItem == null) {
                    return;
                }

                confirmButton.setVisibility(View.GONE);

                if (documentReviewFieldItem.getDocumentSide() != null) {
                    switch (documentReviewFieldItem.getDocumentSide()) {
                        case FRONT:
                            documentImageDetailView.setImageDrawable(model.getFrontImageDrawable().getValue());
                            break;
                        case BACK:
                            documentImageDetailView.setImageDrawable(model.getBackImageDrawable().getValue());
                            break;
                    }

                    if (documentImageDetailView.getDrawable() != null && documentReviewFieldItem.getRect() != null) {
                        documentImageDetailContainerViewOnLayoutChangeListener = new View.OnLayoutChangeListener() {

                            @Override
                            public void onLayoutChange(final View v, final int left, final int top, final int right, final int bottom, final int oldLeft, final int oldTop, final int oldRight, final int oldBottom) {
                                if (documentImageDetailOverlayView.getVisibility() == View.GONE && documentImageDetailWarningIconView.getVisibility() == View.GONE) {
                                    resizeImageViewAndSetOverlay(documentReviewFieldItem.getRect());
                                }
                            }

                        };

                        documentImageDetailContainerView.addOnLayoutChangeListener(documentImageDetailContainerViewOnLayoutChangeListener);
                    }
                }

                nameTextView.setText(documentReviewFieldItem.getName());
                valueEditText.setText(documentReviewFieldItem.getValue());
                valueEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            documentReviewFieldItem.setValue(valueEditText.getText().toString());
                            documentReviewFieldItem.setConfirmed(true);

                            adapter.notifyDataSetChanged();

                            valueEditText.clearFocus();

                            final InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(valueEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                            model.updateUncertainValues();
                            model.getItemEditDoneEvent().call();
                        }

                        return false;
                    }

                });
                valueEditText.requestFocus();

                editFrameViewAnimator = ObjectAnimator.ofFloat(editFrameView, "alpha", 0f, 1f);
                editFrameViewAnimator.setDuration(150L);
                editFrameViewAnimator.addListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(final Animator animation) {
                        editFrameView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(final Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(final Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(final Animator animation) {
                    }

                });
                editFrameViewAnimator.start();

                final InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(valueEditText, InputMethodManager.SHOW_IMPLICIT);

                editContainerViewAnimator = ObjectAnimator.ofFloat(editContainerView, "alpha", 0f, 1f);
                editContainerViewAnimator.setStartDelay(300L);
                editContainerViewAnimator.setDuration(150L);
                editContainerViewAnimator.addListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(final Animator animation) {
                        editContainerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(final Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(final Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(final Animator animation) {
                    }

                });
                editContainerViewAnimator.start();
            }

        });

        model.getItemEditDoneEvent().observe(this, new Observer<Void>() {

            @Override
            public void onChanged(@Nullable final Void aVoid) {
                model.clearSelectedItem();
                cancelEditDetailAnimations();

                confirmButton.setVisibility(View.VISIBLE);
                editFrameView.setVisibility(View.GONE);
                editFrameView.setAlpha(0f);
                editContainerView.setVisibility(View.GONE);
                editContainerView.setAlpha(0f);
                resetDocumentImageDetail();
                nameTextView.clearComposingText();
                valueEditText.clearComposingText();
            }

        });

        model.getUncertainValues().observe(this, new Observer<Integer>() {

            @Override
            public void onChanged(@Nullable final Integer integer) {
                if (integer > 0) {
                    infoBarTextView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.common_error));
                    infoBarTextView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_warning_white_16dp), null, null, null);
                    infoBarTextView.setText(getResources().getQuantityString(R.plurals.document_review_info_bar_uncertain_items, integer, integer));
                    subtitleTextView.setText(getString(R.string.document_review_subtitle_uncertain_items));
                } else {
                    final TypedValue typedValue = new TypedValue();
                    if (getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true)) {
                        infoBarTextView.setBackgroundColor(typedValue.data);
                    }
                    infoBarTextView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_info_white_16dp), null, null, null);
                    infoBarTextView.setText(getString(R.string.document_review_info_bar_ok));
                    subtitleTextView.setText(getString(R.string.document_review_subtitle_ok));
                    confirmButton.setEnabled(true);
                }
            }

        });

        adapter = new DocumentReviewRecyclerViewAdapter(model.getSelectedItem(), model.getDataConfidenceThreshold());

        setupRecyclerView();
        setupConfirmButton();
        setupEditFrameView();
        setupValueEditText();
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelEditDetailAnimations();
    }

    private void setupRecyclerView() {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(final int position) {
                switch (adapter.getItemViewType(position)) {
                    case DocumentReviewRecyclerViewAdapter.TYPE_CATEGORY:
                        return 2;
                    case DocumentReviewRecyclerViewAdapter.TYPE_FIELD:
                    default:
                        return 1;
                }
            }

        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setupConfirmButton() {
        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                final List<DocumentReviewedItem> ocrFieldEntryList = DocumentUtils.toDocumentReviewedItemList(adapter.getList());
                onReviewed(ocrFieldEntryList);
            }

        });
    }

    private void setupEditFrameView() {
        editFrameView.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                return true;
            }

        });
    }

    private void setupValueEditText() {
        valueEditText.setItemEditDoneEvent(model.getItemEditDoneEvent());
    }

    private void resizeImageViewAndSetOverlay(final Rect rect) {
        final float viewAspectRatio = (float) documentImageDetailView.getWidth() / (float) documentImageDetailView.getHeight();
        final float imageAspectRatio = (float) documentImageDetailView.getDrawable().getIntrinsicWidth() / (float) documentImageDetailView.getDrawable().getIntrinsicHeight();
        final float mutualRatio = viewAspectRatio / imageAspectRatio;

        float containerWidth;
        float containerHeight;

        float normScale;

        if (mutualRatio > 1) {
            containerWidth = (float) documentImageDetailView.getWidth() / mutualRatio;
            containerHeight = (float) documentImageDetailView.getHeight();
            normScale = (float) documentImageDetailView.getHeight() / (float) documentImageDetailView.getDrawable().getIntrinsicHeight();
        } else {
            containerWidth = (float) documentImageDetailView.getWidth();
            containerHeight = (float) documentImageDetailView.getHeight() * mutualRatio;
            normScale = (float) documentImageDetailView.getWidth() / (float) documentImageDetailView.getDrawable().getIntrinsicWidth();
        }

        final RectF normRect = new RectF(
                (float) rect.getLeft() * normScale,
                (float) rect.getTop() * normScale,
                (float) rect.getRight() * normScale,
                (float) rect.getBottom() * normScale);

        final PointF rectCenter = new PointF(normRect.centerX(), normRect.centerY());
        final PointF containerCenter = new PointF(containerWidth / 2f, containerHeight / 2f);

        final float centerDiffX = containerCenter.x - rectCenter.x;
        final float centerDiffY = containerCenter.y - rectCenter.y;

        final float scaleX = (float) documentImageDetailView.getWidth() / normRect.width();
        final float scaleY = (float) documentImageDetailView.getHeight() / normRect.height();
        final float scale = Math.min(scaleX, scaleY);

        resizeDocumentImageDetailView(centerDiffX, centerDiffY, scale);
        showDocumentImageDetailOverlayView(normRect);
    }

    private void resizeDocumentImageDetailView(final float x, final float y, final float scale) {
        final float zoomScale = scale * ZOOM_FRAGMENT_RATIO;

        documentImageDetailView.setX(x * zoomScale);
        documentImageDetailView.setY(y * zoomScale);
        documentImageDetailView.setScaleX(zoomScale);
        documentImageDetailView.setScaleY(zoomScale);
    }

    private void showDocumentImageDetailOverlayView(final RectF normRect) {
        final float documentImageDetailContainerViewWidth = (float) documentImageDetailContainerView.getWidth();
        final float documentImageDetailContainerViewHeight = (float) documentImageDetailContainerView.getHeight();

        final float viewAspectRatio = documentImageDetailContainerViewWidth / documentImageDetailContainerViewHeight;
        final float normRectAspectRatio = normRect.width() / normRect.height();
        final float mutualRatio = viewAspectRatio / normRectAspectRatio;

        float fragmentWidth;
        float fragmentHeight;

        if (mutualRatio > 1) {
            fragmentWidth = documentImageDetailContainerViewWidth / mutualRatio;
            fragmentHeight = documentImageDetailContainerViewHeight;
        } else {
            fragmentWidth = documentImageDetailContainerViewWidth;
            fragmentHeight = documentImageDetailContainerViewHeight * mutualRatio;
        }

        fragmentWidth *= ZOOM_FRAGMENT_RATIO;
        fragmentHeight *= ZOOM_FRAGMENT_RATIO;

        final float warningCircleImageViewOffset = Utils.toPixels(getContext(), 13, TypedValue.COMPLEX_UNIT_DIP);

        documentImageDetailOverlayView.getLayoutParams().width = (int) fragmentWidth;
        documentImageDetailOverlayView.getLayoutParams().height = (int) fragmentHeight;
        documentImageDetailOverlayView.setVisibility(View.VISIBLE);

        documentImageDetailWarningIconView.getLayoutParams().width = (int) (fragmentWidth + warningCircleImageViewOffset);
        documentImageDetailWarningIconView.getLayoutParams().height = (int) (fragmentHeight + warningCircleImageViewOffset);
        documentImageDetailWarningIconView.setVisibility(View.VISIBLE);
    }

    private void resetDocumentImageDetail() {
        documentImageDetailContainerView.removeOnLayoutChangeListener(documentImageDetailContainerViewOnLayoutChangeListener);
        documentImageDetailView.setX(0f);
        documentImageDetailView.setY(0f);
        documentImageDetailView.setScaleX(1f);
        documentImageDetailView.setScaleY(1f);
        documentImageDetailView.setImageDrawable(null);
        documentImageDetailOverlayView.setVisibility(View.GONE);
        documentImageDetailWarningIconView.setVisibility(View.GONE);
    }

    private void cancelEditDetailAnimations() {
        if (editFrameViewAnimator != null && editFrameViewAnimator.isStarted()) {
            editFrameViewAnimator.cancel();
        }
        if (editContainerViewAnimator != null && editContainerViewAnimator.isStarted()) {
            editContainerViewAnimator.cancel();
        }
    }

    protected abstract void onReviewed(List<DocumentReviewedItem> list);

}
