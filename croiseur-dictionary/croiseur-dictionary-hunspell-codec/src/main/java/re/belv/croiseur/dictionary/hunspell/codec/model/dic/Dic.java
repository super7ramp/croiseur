/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.model.dic;

import java.util.Collection;

/**
 * Represents a parsed ".dic" file.
 */
public record Dic(Collection<DicEntry> entries) {
    // Nothing to add.
}
