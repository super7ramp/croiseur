/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.wordforms;

import java.util.Optional;
import java.util.function.Function;

/** Applies an affix to a word. */
interface AffixRuleApplicator extends Function<String, Optional<String>> {
    // Marker interface
}
