/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import re.belv.croiseur.dictionary.txt.plugin.TxtDictionaryProvider;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;

/**
 * Dictionary provider of local word lists written as text files.
 */
module re.belv.croiseur.dictionary.txt.plugin {
    requires re.belv.croiseur.dictionary.common;
    requires transitive re.belv.croiseur.spi.dictionary;
    requires java.logging;

    provides DictionaryProvider with TxtDictionaryProvider;
    // No API exposed, only a provider
}