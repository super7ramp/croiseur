package com.gitlab.super7ramp.crosswords.solver.lib;

public final class BacktrackHint {

    private final int indexToUnassign;

    BacktrackHint(final int anIndex) {
        indexToUnassign = anIndex;
    }


    int indexToUnassign() {
        return indexToUnassign;
    }

}
