/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import com.gitlab.super7ramp.croiseur.dictionary.xml.plugin.XmlDictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;

/**
 * {@link DictionaryProvider} for XML dictionaries.
 */
module com.gitlab.super7ramp.croiseur.dictionary.xml.plugin {
    requires com.gitlab.super7ramp.croiseur.dictionary.common;
    requires com.gitlab.super7ramp.croiseur.dictionary.xml.codec;
    requires transitive com.gitlab.super7ramp.croiseur.spi.dictionary;
    provides DictionaryProvider with XmlDictionaryProvider;
    // No API exposed, only a provider

    requires java.logging;
}