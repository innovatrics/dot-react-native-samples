package com.innovatrics.android.dot.sample.http.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OnboardingGenerateLivenessPositionsResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("positions")
    private List<LivenessPosition> positionList;

    public List<LivenessPosition> getPositionList() {
        return positionList;
    }

    public void setPositionList(final List<LivenessPosition> positionList) {
        this.positionList = positionList;
    }

}
