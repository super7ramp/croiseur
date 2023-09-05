/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.regex.Pattern;

/**
 * A Hamcrest String matcher matching a solver run URI.
 */
public final class SolverRunUriMatcher extends TypeSafeMatcher<String> {

    private static final Pattern PATTERN;

    static {
        final String prefix = ".*/solvers/runs/";
        final String uuidRegex =
                "[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}";
        PATTERN = Pattern.compile(prefix + uuidRegex);
    }

    private SolverRunUriMatcher() {
        // Nothing to do.
    }

    public static SolverRunUriMatcher isValidSolverRunUri() {
        return new SolverRunUriMatcher();
    }

    @Override
    protected boolean matchesSafely(final String item) {
        return PATTERN.matcher(item).matches();
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("Solver run URI");
    }
}
