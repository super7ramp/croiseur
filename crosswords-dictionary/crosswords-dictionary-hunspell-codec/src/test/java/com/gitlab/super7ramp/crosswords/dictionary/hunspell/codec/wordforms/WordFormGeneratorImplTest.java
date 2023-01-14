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
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests on {@link WordFormGeneratorImpl}.
 */
final class WordFormGeneratorImplTest {

    private static Aff createAff(Path affPath) {
        try {
            return new AffParser().parse(Files.readAllLines(affPath).iterator());
        } catch (IOException | ParserException e) {
            throw new AssertionError(e);
        }
    }

    private static Dic createDic(Aff aff, Path dicPath) {
        try {
            return new DicParser(aff.flagType()).parse(Files.readAllLines(dicPath).iterator());
        } catch (IOException | ParserException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    void generateSimple() throws URISyntaxException {
        final Path affFile = Path.of(WordFormGeneratorImplTest.class.getResource("/simple.aff")
                                                                    .toURI());
        final Path dicFile = Path.of(WordFormGeneratorImplTest.class.getResource("/simple.dic")
                                                                    .toURI());

        final Set<String> actual = createGenerator(affFile, dicFile).generate()
                                                                    .collect(Collectors.toSet());

        final Set<String> expected = Set.of("hello", "try", "tried", "work", "worked", "reworked"
                , "rework");
        assertEquals(expected, actual);
    }

    @Test
    void generateFr() throws URISyntaxException {
        final Path affFile = Path.of(WordFormGeneratorImplTest.class.getResource("/fr.aff")
                                                                    .toURI());
        final Path dicFile = Path.of(WordFormGeneratorImplTest.class.getResource("/fr.dic")
                                                                    .toURI());

        final long count = createGenerator(affFile, dicFile).generate().count();

        assertTrue(count > 500_000);
    }

    @Test
    void generateFrConjugation() throws URISyntaxException {
        final Path affFile = Path.of(WordFormGeneratorImplTest.class.getResource("/fr.aff")
                                                                    .toURI());
        final Path dicFile = Path.of(WordFormGeneratorImplTest.class.getResource("/fr.dic")
                                                                    .toURI());
        final Predicate<String> filter = Pattern.compile("mang").asPredicate();

        final Set<String> actual = createGenerator(affFile, dicFile).generate()
                                                                    .filter(filter)
                                                                    .collect(Collectors.toSet());

        final Set<String> expected = Set.of("mangeras", "mangeât", "mangerions", "mangeant",
                "mangerez", "mangez", "mangeasses", "mangeassions", "mangerai", "mangeriez",
                "mangeons", "mangerons", "mangeront", "mangé", "mangeraient", "mangèrent", "mange"
                , "mangions", "mangeassent", "mangeasse", "mangeas", "mangea", "mangée",
                "mange" + "âtes", "mangées", "mangeai", "mangeâmes", "mangera", "mangeassiez",
                "manger", "manges", "mangers", "mangent", "mangeait", "mangerait", "mangiez",
                "mangerais", "mangeaient", "mangés", "mangeais");
        assertTrue(actual.containsAll(expected));
    }

    private WordFormGenerator createGenerator(final Path affPath, final Path dicPath) {
        final Aff aff = createAff(affPath);
        final Dic dic = createDic(aff, dicPath);
        return new WordFormGeneratorImpl(aff, dic);
    }
}
