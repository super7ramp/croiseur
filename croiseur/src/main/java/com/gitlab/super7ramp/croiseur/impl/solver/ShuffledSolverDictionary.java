/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.solver;

import com.gitlab.super7ramp.croiseur.spi.solver.Dictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * A dictionary shuffled with given randomness source.
 */
final class ShuffledSolverDictionary implements Dictionary {

    /** The dictionary to be shuffled. */
    private final Dictionary actual;

    /** The randomness source used to shuffle. */
    private final Random random;

    /**
     * Constructs an instance.
     *
     * @param actualArg the dictionary to shuffle
     * @param randomArg the randomness source to use to shuffle
     */
    ShuffledSolverDictionary(final Dictionary actualArg, final Random randomArg) {
        actual = actualArg;
        random = randomArg;
    }

    @Override
    public Set<String> words() {
        final List<String> words = new ArrayList<>(actual.words());
        Collections.shuffle(words, random);
        return new LinkedHashSet<>(words);
    }
}
