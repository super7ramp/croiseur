/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.tools;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link SquareSolutionEstimator}
 */
final class SquareSolutionEstimatorTest {

    @Test
    void test() {
        final List<String> words = List.of("AB", "AC", "BC", "BD");

        final SquareSolutionEstimator.EstimationSummary estimationSummary =
                new SquareSolutionEstimator(words, 2).call();

        final double expected = 1.6; // exact value is 1_679_618/1_048_576
        final double actual = estimationSummary.estimation();
        assertEquals(expected, actual, 10E-1);
    }
}