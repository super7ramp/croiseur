/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.web.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import re.belv.croiseur.web.model.solver.SolverRun;

import java.util.regex.Pattern;

/**
 * A Hamcrest String matcher matching {@link SolverRun} serialized in json.
 */
public final class SolverRunJsonMatcher extends TypeSafeMatcher<String> {

    private static final Pattern PATTERN = Pattern.compile(
            "\\[?\\{\"name\":\"(?<name>[^\"]+)\",\"status\":\"(?<status>[^\"]+)\",\"progress\":\\d{1,3},\"creationTime\":\"(?<creationTime>[^\"]+)\",\"endTime\":\"?(?<endTime>([^\"]+))\"?}]?");
    private final SolverRun.Status state;

    private SolverRunJsonMatcher(final SolverRun.Status stateArg) {
        state = stateArg;
    }

    public static SolverRunJsonMatcher createdSolverRun() {
        return new SolverRunJsonMatcher(SolverRun.Status.CREATED);
    }

    public static SolverRunJsonMatcher runningSolverRun() {
        return new SolverRunJsonMatcher(SolverRun.Status.RUNNING);
    }

    public static SolverRunJsonMatcher interruptedSolverRun() {
        return new SolverRunJsonMatcher(SolverRun.Status.INTERRUPTED);
    }

    public static SolverRunJsonMatcher terminatedSolverRun() {
        return new SolverRunJsonMatcher(SolverRun.Status.TERMINATED);
    }

    @Override
    protected boolean matchesSafely(final String item) {
        final var matcher = PATTERN.matcher(item);
        if (!matcher.matches()) {
            return false;
        }
        final SolverRun.Status actualState = SolverRun.Status.valueOf(matcher.group("status"));
        return state == actualState;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("solver run in state " + state);
    }

}
