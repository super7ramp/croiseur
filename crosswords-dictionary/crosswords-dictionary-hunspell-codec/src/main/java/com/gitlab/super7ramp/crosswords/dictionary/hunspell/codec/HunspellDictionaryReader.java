/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryDescription;
import com.gitlab.super7ramp.crosswords.dictionary.common.SegmentableUrl;
import com.gitlab.super7ramp.crosswords.dictionary.common.io.BomInputStream;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.aff.Aff;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.dic.Dic;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff.AffParser;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.ParserException;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.dic.DicParser;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.wordforms.WordFormGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Dictionary relying on local text files in Hunspell format.
 */
// TODO better API: No need to mimic crossword-dictionary SPI now that plugin has been split
public final class HunspellDictionaryReader {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(HunspellDictionaryReader.class.getName());

    /**
     * Extension of dictionary file.
     */
    private static final String DIC_EXTENSION = ".dic";

    /**
     * Extension of affix file.
     */
    private static final String AFF_EXTENSION = ".aff";

    /**
     * URL of dictionary.
     */
    private final URL dicURL;

    /**
     * Constructor.
     *
     * @param aDicURL URL to Hunspell dictionary (.dic); affix file is expected to have same
     *                basename and extension .aff
     */
    public HunspellDictionaryReader(final URL aDicURL) {
        dicURL = Objects.requireNonNull(aDicURL);
    }

    private static Stream<String> streamer(URL url) throws IOException {
        return streamer(url, StandardCharsets.US_ASCII);
    }

    private static Stream<String> streamer(URL url, Charset charset) throws IOException {
        return new BufferedReader(new InputStreamReader(new BomInputStream(url.openStream()),
                charset)).lines();
    }

    /**
     * Returns a description of this dictionary.
     *
     * @return a description of this dictionary
     */
    public DictionaryDescription description() {
        return new DictionaryDescription("Hunspell Dictionary " + name(), locale());
    }

    /**
     * Returns the dictionary as a stream of String.
     *
     * @return the dictionary as a stream of String
     */
    public Stream<String> stream() {
        return readEntries();
    }

    private Locale locale() {
        final String languageTag = languageTag();
        return Locale.forLanguageTag(languageTag);
    }

    private String name() {
        return new SegmentableUrl(dicURL).lastPathSegment();
    }

    private Stream<String> readEntries() {

        final Optional<Aff> optAff = readAff();
        if (optAff.isEmpty()) {
            return Stream.empty();
        }

        final Optional<Dic> optDic = readDic(optAff.get());
        if (optDic.isEmpty()) {
            return Stream.empty();
        }

        return WordFormGenerator.of(optAff.get(), optDic.get()).generate();
    }

    private Optional<Aff> readAff() {
        final URL affUrl = affUrl();
        final Charset encoding;
        try (final Stream<String> lines = streamer(affUrl)) {
            encoding = AffParser.identifyEncoding(lines.iterator());
        } catch (final IOException e) {
            LOGGER.log(Level.WARNING, "Failed to read aff file at " + affUrl, e);
            return Optional.empty();
        }
        LOGGER.fine(() -> "Detected encoding " + encoding);
        try (final Stream<String> lines = streamer(affUrl, encoding)) {
            final Aff aff = new AffParser().parse(lines.iterator());
            return Optional.of(aff);
        } catch (final IOException | ParserException e) {
            LOGGER.log(Level.WARNING, "Failed to read aff file at " + affUrl, e);
            return Optional.empty();
        }
    }

    private Optional<Dic> readDic(final Aff aff) {
        try (final Stream<String> lines = streamer(dicURL, aff.encoding())) {
            final Dic dic = new DicParser(aff.flagType()).parse(lines.iterator());
            return Optional.of(dic);
        } catch (final IOException | ParserException e) {
            LOGGER.log(Level.WARNING, "Failed to read dic file at " + dicURL, e);
            return Optional.empty();
        }
    }

    private URL affUrl() {
        try {
            final String affUrlExternalForm = Pattern.compile(DIC_EXTENSION + "$")
                                                     .matcher(dicURL.toExternalForm())
                                                     .replaceFirst(AFF_EXTENSION);
            return new URL(affUrlExternalForm);
        } catch (final MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String languageTag() {
        final String dicFileName = new SegmentableUrl(dicURL).lastPathSegment();
        return dicFileName.replace(DIC_EXTENSION, "").replace("_", "-");
    }
}
