package com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure;

import com.gitlab.super7ramp.crosswords.dictionary.common.Filters;
import com.gitlab.super7ramp.crosswords.dictionary.common.SegmentableUrl;
import com.gitlab.super7ramp.crosswords.dictionary.common.Transformers;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.parser.aff.Aff;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.parser.aff.AffParser;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.parser.common.ParserException;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.parser.dic.Dic;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.parser.dic.DicParser;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.wordforms.WordFormGenerator;
import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of {@link Dictionary} relying on local text files in Hunspell format.
 */
public final class HunspellDictionary implements Dictionary {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(HunspellDictionary.class.getName());

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
    public HunspellDictionary(final URL aDicURL) {
        dicURL = Objects.requireNonNull(aDicURL);
    }

    private static Stream<String> streamer(URL url) throws IOException {
        return new BufferedReader(new InputStreamReader(url.openStream())).lines();
    }

    @Override
    public Locale locale() {
        final String languageTag = languageTag();
        return Locale.forLanguageTag(languageTag);
    }

    @Override
    public Set<String> lookup(final Predicate<String> predicate) {
        try (final Stream<String> entries = readEntries()) {
            return entries.filter(predicate).collect(Collectors.toSet());
        }
    }

    @Override
    public String name() {
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

        return WordFormGenerator.of(optAff.get(), optDic.get())
                                .generate()
                                .filter(Filters.atLeastTwoCharacters())
                                .map(Transformers::removeHyphen)
                                .map(Transformers::removeAccentuation)
                                .map(Transformers::removeApostrophe)
                                .map(String::toUpperCase);
    }

    private Optional<Aff> readAff() {
        final URL affUrl = affUrl();
        try (final Stream<String> lines = streamer(affUrl)) {
            final Aff aff = new AffParser().parse(lines.iterator());
            return Optional.of(aff);
        } catch (final IOException | ParserException e) {
            LOGGER.log(Level.WARNING, "Failed to read aff file at " + affUrl, e);
            return Optional.empty();
        }
    }

    private Optional<Dic> readDic(final Aff aff) {
        try (final Stream<String> lines = streamer(dicURL)) {
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
