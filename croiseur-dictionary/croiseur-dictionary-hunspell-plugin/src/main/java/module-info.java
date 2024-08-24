/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import re.belv.croiseur.dictionary.hunspell.plugin.HunspellDictionaryProvider;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;

/**
 * Dictionary provider of local word lists written as Hunspell dictionaries.
 */
module re.belv.croiseur.dictionary.hunspell.plugin {
    requires re.belv.croiseur.dictionary.common;
    requires re.belv.croiseur.dictionary.hunspell.codec;
    requires re.belv.croiseur.spi.dictionary;
    requires java.logging;

    provides DictionaryProvider with
            HunspellDictionaryProvider;
}
