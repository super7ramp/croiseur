/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.szunami;

import java.util.Objects;

/**
 * A dictionary.
 * <p>
 * Used to create a trie on Rust side.
 *
 * @param words the words
 * @see <a href="https://docs.rs/xwords/0.3.1/xwords/trie/struct.Trie.html">The Crate
 * Documentation</a>
 */
public record Dictionary(Iterable<String> words) {

    /**
     * Constructs an instance.
     *
     * @param words the words
     * @throws NullPointerException if given word list is {@code null}
     */
    public Dictionary {
        Objects.requireNonNull(words, "Words must be non-null");
    }
}
