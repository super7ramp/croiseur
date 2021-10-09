package com.gitlab.super7ramp.crosswords.solver.lib.util.function;

import java.util.function.BinaryOperator;

/**
 * Some accumulator functions.
 */
public final class Accumulators {

    /**
     * Private constructor, static methods only.
     */
    Accumulators() {
        // Nothing to do.
    }

    public static BinaryOperator<Long> multiplyLong() {
        return (accumulated, toAccumulate) -> accumulated * toAccumulate;
    }
}
