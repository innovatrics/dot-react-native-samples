package com.innovatrics.android.dot.sample.dto;

import java.io.Serializable;

public class InstructionItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private int instructionTitleResId;
    private int instructionTextResId;
    private int instructionImageResId;

    public int getInstructionTitleResId() {
        return instructionTitleResId;
    }

    public void setInstructionTitleResId(final int instructionTitleResId) {
        this.instructionTitleResId = instructionTitleResId;
    }

    public int getInstructionTextResId() {
        return instructionTextResId;
    }

    public void setInstructionTextResId(final int instructionTextResId) {
        this.instructionTextResId = instructionTextResId;
    }

    public int getInstructionImageResId() {
        return instructionImageResId;
    }

    public void setInstructionImageResId(final int instructionImageResId) {
        this.instructionImageResId = instructionImageResId;
    }

}
