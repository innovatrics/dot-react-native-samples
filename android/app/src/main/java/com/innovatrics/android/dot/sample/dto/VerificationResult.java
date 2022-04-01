package com.innovatrics.android.dot.sample.dto;

public class VerificationResult {

    public enum Event {

        VERIFIED,
        NOT_VERIFIED,
        TEMPLATE_INCOMPATIBLE;

    }

    private final Event event;
    private final double score;

    public VerificationResult(final Event event) {
        this(event, 0);
    }

    public VerificationResult(final Event event, final double score) {
        this.event = event;
        this.score = score;
    }

    public Event getEvent() {
        return event;
    }

    public double getScore() {
        return score;
    }

}
