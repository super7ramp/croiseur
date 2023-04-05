/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.presenter.dictionary;

import java.util.List;

/**
 * A dictionary search result.
 *
 * @param words the words found; Can be empty but never {@code null}
 */
public record DictionarySearchResult(List<String> words) {
    // Nothing to add.
}
