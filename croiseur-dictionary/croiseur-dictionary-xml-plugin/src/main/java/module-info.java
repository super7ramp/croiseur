/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import re.belv.croiseur.dictionary.xml.plugin.XmlDictionaryProvider;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;

/**
 * Dictionary provider of local word lists written in XML.
 */
module re.belv.croiseur.dictionary.xml.plugin {
    requires re.belv.croiseur.dictionary.common;
    requires re.belv.croiseur.dictionary.xml.codec;
    requires transitive re.belv.croiseur.spi.dictionary;
    requires java.logging;

    provides DictionaryProvider with XmlDictionaryProvider;
    // No API exposed, only a provider
}