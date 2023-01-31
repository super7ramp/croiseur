/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import com.gitlab.super7ramp.croiseur.dictionary.txt.plugin.TxtDictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;

/**
 * {@link DictionaryProvider} for simple text file dictionaries.
 */
module com.gitlab.super7ramp.croiseur.dictionary.txt.plugin {
    requires com.gitlab.super7ramp.croiseur.dictionary.common;
    requires transitive com.gitlab.super7ramp.croiseur.spi.dictionary;
    provides DictionaryProvider with TxtDictionaryProvider;
    // No API exposed, only a provider

    requires java.logging;
}