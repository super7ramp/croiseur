package com.gitlab.super7ramp.crosswords.solver.lib;

import java.util.Set;

interface Backtracker {

    Set<WordVariable> backtrackFrom(final WordVariable variable);
}
