package com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.external;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryDescription;
import com.gitlab.super7ramp.crosswords.dictionary.common.SegmentableUrl;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.external.wordforms.WordFormGenerator;
import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

public final class HunspellDictionary implements Dictionary {

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


    @Override
    public DictionaryDescription description() {
        return new DictionaryDescription(name(), locale());
    }

    @Override
    public Stream<String> stream() {
        return WordFormGenerator.of(affPath(), dicPath()).generate();
    }

    private String name() {
        return new SegmentableUrl(dicURL).lastPathSegment();
    }

    private String languageTag() {
        final String dicFileName = new SegmentableUrl(dicURL).lastPathSegment();
        return dicFileName.replace(DIC_EXTENSION, "").replace("_", "-");
    }

    private Locale locale() {
        final String languageTag = languageTag();
        return Locale.forLanguageTag(languageTag);
    }

    private Path dicPath() {
        try {
            return Path.of(dicURL.toURI());
        } catch (final URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Path affPath() {
        final Path dicPath = dicPath();
        final String dicFileName = dicPath.getFileName().toString();
        final String affFileName = dicFileName.replace(DIC_EXTENSION, AFF_EXTENSION);
        return dicPath.resolveSibling(affFileName);
    }
}
