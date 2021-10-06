package com.gitlab.super7ramp.crosswords.db;

import com.gitlab.super7ramp.crosswords.solver.lib.WordVariable;

import java.util.Set;

public interface WordDatabase {

    Set<String> findPossibleValues(final WordVariable wordVariable);

    long countPossibleValues(final WordVariable wordVariable);
}
