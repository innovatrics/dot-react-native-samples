package com.innovatrics.android.dot.sample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.innovatrics.android.dot.sample.R;
import com.innovatrics.android.dot.sample.dto.InstructionItem;

import java.util.List;

public class RecyclerSimpleInstructionAdapter extends RecyclerView.Adapter<RecyclerSimpleInstructionAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView textView;

        public ViewHolder(final View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }

        public void bindTo(final InstructionItem item) {
            clear();

            textView.setText(item.getInstructionTitleResId());
            textView.setCompoundDrawablesWithIntrinsicBounds(0, item.getInstructionImageResId(), 0, 0);
        }

        public void clear() {
            textView.clearComposingText();
        }
    }

    private List<InstructionItem> list;

    public void setList(final List<InstructionItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_instruction_card_simple, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final InstructionItem item = list.get(position);

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
