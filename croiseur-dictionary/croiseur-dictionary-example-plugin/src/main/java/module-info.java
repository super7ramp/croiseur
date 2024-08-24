/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import re.belv.croiseur.dictionary.example.plugin.ExampleDictionaryProvider;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;

/** An example dictionary provider plugin. */
module re.belv.croiseur.dictionary.example.plugin {
    requires re.belv.croiseur.spi.dictionary;

    provides DictionaryProvider with
            ExampleDictionaryProvider;
}
