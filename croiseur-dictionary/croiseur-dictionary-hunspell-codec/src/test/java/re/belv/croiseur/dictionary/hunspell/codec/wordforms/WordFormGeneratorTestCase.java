/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.wordforms;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import re.belv.croiseur.dictionary.hunspell.codec.model.aff.Aff;
import re.belv.croiseur.dictionary.hunspell.codec.model.dic.Dic;
import re.belv.croiseur.dictionary.hunspell.codec.parser.aff.AffParser;
import re.belv.croiseur.dictionary.hunspell.codec.parser.common.ParserException;
import re.belv.croiseur.dictionary.hunspell.codec.parser.dic.DicParser;

abstract class WordFormGeneratorTestCase {

    private static Aff createAff(final Path affPath, final Charset charset) {
        try {
            return new AffParser().parse(Files.readAllLines(affPath, charset).iterator());
        } catch (final IOException | ParserException e) {
            throw new AssertionError(e);
        }
    }

    private static Dic createDic(final Aff aff, final Path dicPath, final Charset charset) {
        try {
            return new DicParser(aff.flagType())
                    .parse(Files.readAllLines(dicPath, charset).iterator());
        } catch (final IOException | ParserException e) {
            throw new AssertionError(e);
        }
    }

    private static WordFormGenerator createGenerator(final Path affPath, final Path dicPath, final Charset charset) {
        final Aff aff = createAff(affPath, charset);
        final Dic dic = createDic(aff, dicPath, charset);
        return new WordFormGeneratorImpl(aff, dic);
    }

    private static Optional<Path> pathOf(final String resourcePath) throws URISyntaxException {
        final URL url = WordFormGeneratorTestCase.class.getResource("/" + resourcePath);
        final Optional<Path> path;
        if (url != null) {
            path = Optional.of(Path.of(url.toURI()));
        } else {
            path = Optional.empty();
        }
        return path;
    }

    private static Set<String> wordsOf(final Path path) {
        try {
            return new HashSet<>(Files.readAllLines(path));
        } catch (final IOException e) {
            return Collections.emptySet();
        }
    }

    private static <T> void assertContainsAll(final Collection<T> expected, final Collection<T> actual) {
        final Collection<Executable> verifications = new ArrayList<>(expected.size());
        for (final T expectedItem : expected) {
            final Executable verification =
                    () -> assertTrue(actual.contains(expectedItem), "Expected '" + expectedItem + "' is missing");
            verifications.add(verification);
        }
        assertAll(verifications);
    }

    private static <T> void assertContainsNone(final Collection<T> unexpected, final Collection<T> actual) {
        final Collection<Executable> verifications = new ArrayList<>(unexpected.size());
        for (final T unexpectedItem : unexpected) {
            final Executable verification = () ->
                    assertFalse(actual.contains(unexpectedItem), "Unexpected '" + unexpectedItem + "' is present");
            verifications.add(verification);
        }
        assertAll(verifications);
    }

    /**
     * Returns the base name of the test dictionary (e.g. for "simple.dic" and "simple.aff", name is
     * "simple").
     *
     * @return the base of the test dictionary
     */
    abstract String name();

    /**
     * Returns the charset used to encode the test files.
     *
     * @return the charset used to encode the test files
     */
    Charset charset() {
        return Charset.defaultCharset();
    }

    @Test
    final void generate() throws URISyntaxException {

        final Path affFile = pathOf(name() + ".aff").orElseThrow();
        final Path dicFile = pathOf(name() + ".dic").orElseThrow();
        final Set<String> goods = wordsOf(pathOf(name() + ".good").orElseThrow());
        final Set<String> wrongs = pathOf(name() + ".wrong")
                .map(WordFormGeneratorTestCase::wordsOf)
                .orElseGet(Collections::emptySet);

        final Set<String> actual =
                createGenerator(affFile, dicFile, charset()).generate().collect(toSet());

        assertAll(() -> assertContainsAll(goods, actual), () -> assertContainsNone(wrongs, actual));
    }
}
