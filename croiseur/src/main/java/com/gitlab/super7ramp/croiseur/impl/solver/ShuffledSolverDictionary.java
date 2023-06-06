/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.solver;

import com.gitlab.super7ramp.croiseur.spi.solver.Dictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A dictionary shuffled once with given randomness source.
 */
final class ShuffledSolverDictionary implements Dictionary {

    /** The shuffled words. */
    private final List<String> words;

    /**
     * Constructs an instance.
     *
     * @param dictionary the dictionary to shuffle
     * @param random     the randomness source to use to shuffle
     */
    ShuffledSolverDictionary(final Dictionary dictionary, final Random random) {
        words = new ArrayList<>(dictionary.words());
        Collections.shuffle(words, random);
    }

    @Override
    public Collection<String> words() {
        return words;
    }
}
