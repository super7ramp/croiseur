/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.wordforms;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import re.belv.croiseur.dictionary.hunspell.codec.model.aff.Aff;
import re.belv.croiseur.dictionary.hunspell.codec.model.aff.AffixClass;
import re.belv.croiseur.dictionary.hunspell.codec.model.common.Flag;

/**
 * Access to all the affix classes.
 */
final class AffixClasses implements Iterable<AffixClass> {

    /** The affix classes, indexed by their identifying flags. */
    private final Map<Flag, AffixClass> affixClasses;

    /**
     * Constructs an instance.
     *
     * @param aff the parsed aff file
     */
    AffixClasses(final Aff aff) {
        affixClasses = new HashMap<>();
        for (final AffixClass affixClass : aff.affixClasses()) {
            affixClasses.put(affixClass.flag(), affixClass);
        }
    }

    /**
     * Returns the affix classes identified by the given flags.
     *
     * @param flags the affix class flags
     * @return the affix classes identified by the given flags
     */
    Stream<AffixClass> referencedBy(final Collection<Flag> flags) {
        // flag may refer to another option than PFX/SFX, hence the null check
        return flags.stream().map(affixClasses::get).filter(Objects::nonNull);
    }

    @Override
    public Iterator<AffixClass> iterator() {
        return Collections.unmodifiableCollection(affixClasses.values()).iterator();
    }
}
