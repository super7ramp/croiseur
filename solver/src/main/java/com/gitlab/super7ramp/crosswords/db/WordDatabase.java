package main.java.com.gitlab.super7ramp.crosswords.db;

import main.java.com.gitlab.super7ramp.crosswords.solver.WordVariable;

import java.util.Set;

public interface WordDatabase {

    Set<String> findPossibleValues(final WordVariable wordVariable);

    long countPossibleValues(final WordVariable wordVariable);
}
