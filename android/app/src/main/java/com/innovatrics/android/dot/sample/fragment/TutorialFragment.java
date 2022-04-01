package com.innovatrics.android.dot.sample.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.innovatrics.android.dot.sample.R;
import com.innovatrics.android.dot.sample.adapter.RecyclerSimpleInstructionAdapter;
import com.innovatrics.android.dot.sample.dto.InstructionItem;
import com.innovatrics.android.dot.sample.model.InstructedFaceCaptureModel;

import java.util.ArrayList;
import java.util.List;

public class TutorialFragment extends Fragment {

    private InstructedFaceCaptureModel instructedFaceCaptureModel;
    private RecyclerView recyclerView;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutorial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler);
        button = view.findViewById(R.id.button);

        setupRecyclerView();
        setupButton();
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        instructedFaceCaptureModel = ViewModelProviders.of(getActivity()).get(InstructedFaceCaptureModel.class);
    }

    private void setupRecyclerView() {
        final List<InstructionItem> instructionItems = new ArrayList<>();
        InstructionItem instructionItem;

        instructionItem = new InstructionItem();
        instructionItem.setInstructionTitleResId(R.string.face_capture_instruction_step_position_text);
        instructionItem.setInstructionImageResId(R.drawable.ic_instruction_position);
        instructionItems.add(instructionItem);

        instructionItem = new InstructionItem();
        instructionItem.setInstructionTitleResId(R.string.face_capture_instruction_step_light_text);
        instructionItem.setInstructionImageResId(R.drawable.ic_instruction_lighting);
        instructionItems.add(instructionItem);

        instructionItem = new InstructionItem();
        instructionItem.setInstructionTitleResId(R.string.face_capture_instruction_step_done_text);
        instructionItem.setInstructionImageResId(R.drawable.ic_instruction_finish);
        instructionItems.add(instructionItem);

        final RecyclerSimpleInstructionAdapter adapter = new RecyclerSimpleInstructionAdapter();
        adapter.setList(instructionItems);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setupButton() {
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                instructedFaceCaptureModel.prepareAndStartCapture();
            }

        });
    }

}
