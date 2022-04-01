package com.innovatrics.android.dot.sample.http.dto;

import com.google.gson.annotations.SerializedName;
import com.innovatrics.dot.face.liveness.eyegaze.Segment;

import java.io.Serializable;
import java.util.List;

public class OnboardingGenerateLivenessPositionsResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("positions")
    private List<Segment.Corner> positionList;

    public List<Segment.Corner> getPositionList() {
        return positionList;
    }

    public void setPositionList(final List<Segment.Corner> positionList) {
        this.positionList = positionList;
    }

}
