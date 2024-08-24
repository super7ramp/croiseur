/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.solver.prerun;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import re.belv.croiseur.spi.solver.Dictionary;

/**
 * A dictionary shuffled once with given randomness source.
 */
public final class ShuffledSolverDictionary implements Dictionary {

    /** The shuffled words. */
    private final List<String> words;

    /**
     * Constructs an instance.
     *
     * @param dictionary the dictionary to shuffle
     * @param random     the randomness source to use to shuffle
     */
    public ShuffledSolverDictionary(final Dictionary dictionary, final Random random) {
        words = new ArrayList<>(dictionary.words());
        Collections.shuffle(words, random);
    }

    @Override
    public Collection<String> words() {
        return words;
    }
}
