/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/** Tools for manipulating dictionaries. */
module re.belv.croiseur.dictionary.tools {
    requires re.belv.croiseur.dictionary.common;
    requires re.belv.croiseur.dictionary.hunspell.codec;
    requires re.belv.croiseur.dictionary.xml.codec;
    requires java.logging;

    exports re.belv.croiseur.dictionary.tools;
}
