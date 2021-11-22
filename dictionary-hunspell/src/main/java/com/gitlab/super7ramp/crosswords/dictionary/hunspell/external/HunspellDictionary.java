package com.gitlab.super7ramp.crosswords.dictionary.hunspell.external;

import com.gitlab.super7ramp.crosswords.dictionary.api.Dictionary;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.external.wordforms.WordFormGenerator;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.util.SegmentableUrl;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class HunspellDictionary implements Dictionary {

    /** Extension of dictionary file. */
    private static String DIC_EXTENSION = ".dic";

    /** Extension of affix file. */
    private static String AFF_EXTENSION = ".aff";

    /** URL of dictionary. */
    private final URL dicURL;

    /**
     * Constructor.
     *
     * @param aDicURL URL to Hunspell dictionary (.dic); affix file is expected to have same basename and extension .aff
     */
    public HunspellDictionary(final URL aDicURL) {
        dicURL = Objects.requireNonNull(aDicURL);
    }

    @Override
    public Locale locale() {
        final String languageTag = languageTag();
        return Locale.forLanguageTag(languageTag);
    }

    @Override
    public Set<String> lookup(final Predicate<String> predicate) {
        return WordFormGenerator.of(affPath(), dicPath()).generate().collect(Collectors.toSet());
    }

    private String languageTag() {
        final String dicFileName = new SegmentableUrl(dicURL).lastPathSegment();
        return dicFileName.replace(DIC_EXTENSION, "").replace("_", "-");
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
