/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Library which allows to read Hunspell dictionaries and generate all word forms from them.
 */
module re.belv.croiseur.dictionary.hunspell.codec {
    requires transitive re.belv.croiseur.common;
    requires re.belv.croiseur.dictionary.common;
    requires java.logging;

    exports re.belv.croiseur.dictionary.hunspell.codec;
}