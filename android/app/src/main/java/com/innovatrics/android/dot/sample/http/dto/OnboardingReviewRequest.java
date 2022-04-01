package com.innovatrics.android.dot.sample.http.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OnboardingReviewRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("reviewFields")
    private List<ReviewField> reviewFieldList;

    public List<ReviewField> getReviewFieldList() {
        return reviewFieldList;
    }

    public void setReviewFieldList(final List<ReviewField> reviewFieldList) {
        this.reviewFieldList = reviewFieldList;
    }

}
