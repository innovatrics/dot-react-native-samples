package com.innovatrics.android.dot.sample.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.appcompat.widget.AppCompatEditText;

import com.innovatrics.android.dot.sample.utils.SingleLiveEvent;

public class DocumentReviewEditText extends AppCompatEditText {

    private SingleLiveEvent<Void> itemEditDoneEvent;

    public DocumentReviewEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onKeyPreIme(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && event.getFlags() == 0x48) {
            clearFocus();

            if (itemEditDoneEvent != null) {
                itemEditDoneEvent.call();
            }
        }

        return super.onKeyPreIme(keyCode, event);
    }

    public void setItemEditDoneEvent(final SingleLiveEvent<Void> itemEditDoneEvent) {
        this.itemEditDoneEvent = itemEditDoneEvent;
    }
}
