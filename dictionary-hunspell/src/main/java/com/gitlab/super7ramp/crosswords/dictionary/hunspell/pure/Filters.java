package com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure;

import java.util.function.Predicate;

final class Filters {

    private Filters() {
        // Nothing to do.
    }

    static Predicate<String> atLeastTwoCharacters() {
        return s -> s.length() >= 2;
    }
}
