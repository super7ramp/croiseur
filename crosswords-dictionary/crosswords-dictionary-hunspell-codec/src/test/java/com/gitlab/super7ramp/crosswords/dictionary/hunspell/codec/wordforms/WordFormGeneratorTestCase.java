/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.wordforms;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff.Aff;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff.AffParser;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.ParserException;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.dic.Dic;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.dic.DicParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class WordFormGeneratorTestCase {

    private static Aff createAff(final Path affPath) {
        try {
            return new AffParser().parse(Files.readAllLines(affPath).iterator());
        } catch (final IOException | ParserException e) {
            throw new AssertionError(e);
        }
    }

    private static Dic createDic(final Aff aff, final Path dicPath) {
        try {
            return new DicParser(aff.flagType()).parse(Files.readAllLines(dicPath).iterator());
        } catch (final IOException | ParserException e) {
            throw new AssertionError(e);
        }
    }

    private static WordFormGenerator createGenerator(final Path affPath, final Path dicPath) {
        final Aff aff = createAff(affPath);
        final Dic dic = createDic(aff, dicPath);
        return new WordFormGeneratorImpl(aff, dic);
    }

    private static Path pathOf(final String resourcePath) throws URISyntaxException {
        return Path.of(WordFormGeneratorTestCase.class.getResource("/" + resourcePath).toURI());
    }

    /**
     * Returns the base name of the test dictionary (e.g. for "simple.dic" and "simple.aff", name is
     * "simple").
     *
     * @return the base of the test dictionary
     */
    abstract String name();

    /**
     * Returns the expected generated words.
     *
     * @return the expected generated words
     */
    abstract Set<String> expected();

    @Test
    final void generate() throws URISyntaxException {
        final Path affFile = pathOf(name() + ".aff");
        final Path dicFile = pathOf(name() + ".dic");

        final Set<String> actual = createGenerator(affFile, dicFile).generate().collect(toSet());

        assertEquals(expected(), actual);
    }
}
