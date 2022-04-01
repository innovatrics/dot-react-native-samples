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
import com.innovatrics.android.dot.sample.adapter.RecyclerAdvancedFlatInstructionAdapter;
import com.innovatrics.android.dot.sample.dto.InstructionItem;
import com.innovatrics.android.dot.sample.model.OnboardingModel;

import java.util.ArrayList;
import java.util.List;

public class OnboardingInstructionsFragment extends Fragment {

    private OnboardingModel onboardingModel;
    private RecyclerView recyclerView;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding_instructions, container, false);
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
        onboardingModel = ViewModelProviders.of(getActivity()).get(OnboardingModel.class);
    }

    private void setupRecyclerView() {
        final List<InstructionItem> instructionItems = new ArrayList<>();
        InstructionItem instructionItem;

        instructionItem = new InstructionItem();
        instructionItem.setInstructionTitleResId(R.string.onboarding_instruction_step_scan_document_title);
        instructionItem.setInstructionTextResId(R.string.onboarding_instruction_step_scan_document_text);
        instructionItem.setInstructionImageResId(R.drawable.ic_instruction_scan_document);
        instructionItems.add(instructionItem);

        instructionItem = new InstructionItem();
        instructionItem.setInstructionTitleResId(R.string.onboarding_instruction_step_take_photo_title);
        instructionItem.setInstructionTextResId(R.string.onboarding_instruction_step_take_photo_text);
        instructionItem.setInstructionImageResId(R.drawable.ic_instruction_position);
        instructionItems.add(instructionItem);

        final RecyclerAdvancedFlatInstructionAdapter adapter = new RecyclerAdvancedFlatInstructionAdapter();
        adapter.setList(instructionItems);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setupButton() {
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                onboardingModel.startDocumentCapture();
            }

        });
    }

}
