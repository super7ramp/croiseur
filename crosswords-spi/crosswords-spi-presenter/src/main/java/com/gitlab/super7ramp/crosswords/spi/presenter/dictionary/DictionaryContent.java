/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.spi.presenter.dictionary;

import com.gitlab.super7ramp.crosswords.common.dictionary.ProvidedDictionaryDescription;

import java.util.List;

/**
 * The words of a dictionary for presentation purposes.
 *
 * @param description the description of the dictionary and its provider
 * @param words all the words inside the dictionary
 */
public record DictionaryContent(ProvidedDictionaryDescription description, List<String> words) {
    // Nothing to add.
}
