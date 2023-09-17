package com.github.hugovallada;

public enum RideStatus {
    REQUESTED, ACCEPTED, IN_PROGRESS, COMPLETED, CANCELLED;

    public boolean inProgress() {
        return this == IN_PROGRESS || this == ACCEPTED;
    }

    public boolean isRequestedOrInProgress() {
        return this == REQUESTED || inProgress();
    }
}
