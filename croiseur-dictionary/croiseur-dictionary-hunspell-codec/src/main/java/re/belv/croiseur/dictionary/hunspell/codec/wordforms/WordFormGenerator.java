/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.wordforms;

import java.util.function.Supplier;
import java.util.stream.Stream;
import re.belv.croiseur.dictionary.hunspell.codec.model.aff.Aff;
import re.belv.croiseur.dictionary.hunspell.codec.model.dic.Dic;

/** Generate word forms. */
public interface WordFormGenerator extends Supplier<Stream<String>> {

    /**
     * Returns a new {@link WordFormGenerator}.
     *
     * @param aff an Aff file
     * @param dic a Dic file
     * @return a new {@link WordFormGenerator}
     */
    static WordFormGenerator of(final Aff aff, final Dic dic) {
        return new WordFormGeneratorImpl(aff, dic);
    }

    @Override
    default Stream<String> get() {
        return generate();
    }

    /**
     * Generate all the valid forms.
     *
     * @return all the valid forms
     */
    Stream<String> generate();
}
