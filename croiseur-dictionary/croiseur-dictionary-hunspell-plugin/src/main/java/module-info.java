/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import com.gitlab.super7ramp.croiseur.dictionary.hunspell.plugin.HunspellDictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;

/**
 * {@link DictionaryProvider} for Hunspell dictionaries.
 */
module com.gitlab.super7ramp.croiseur.dictionary.hunspell.plugin {
    requires com.gitlab.super7ramp.croiseur.dictionary.common;
    requires com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec;
    requires com.gitlab.super7ramp.croiseur.spi.dictionary;
    provides DictionaryProvider with HunspellDictionaryProvider;

    requires java.logging;
}