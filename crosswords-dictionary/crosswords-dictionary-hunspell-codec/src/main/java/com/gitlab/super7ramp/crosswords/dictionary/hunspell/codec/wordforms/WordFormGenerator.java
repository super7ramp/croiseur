/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.wordforms;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff.Aff;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.dic.Dic;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Generate word forms.
 */
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
