package com.innovatrics.android.dot.sample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.innovatrics.android.dot.sample.R;
import com.innovatrics.android.dot.sample.dto.InstructionItem;

import java.util.List;

public class RecyclerAdvancedFlatInstructionAdapter extends RecyclerView.Adapter<RecyclerAdvancedFlatInstructionAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView instructionImage;
        public final TextView instructionTitle;
        public final TextView instructionText;

        public ViewHolder(final View itemView) {
            super(itemView);
            instructionImage = itemView.findViewById(R.id.image_instruction);
            instructionTitle = itemView.findViewById(R.id.text_title);
            instructionText = itemView.findViewById(R.id.text_instruction);
        }

        public void bindTo(final InstructionItem item) {
            clear();

            instructionTitle.setText(item.getInstructionTitleResId());
            instructionText.setText(item.getInstructionTextResId());
            instructionImage.setImageResource(item.getInstructionImageResId());
        }

        public void clear() {
            instructionTitle.clearComposingText();
            instructionText.clearComposingText();
            instructionImage.setImageDrawable(null);
        }

    }

    private List<InstructionItem> list;

    public void setList(final List<InstructionItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_instruction_card_flat, parent, false);
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
