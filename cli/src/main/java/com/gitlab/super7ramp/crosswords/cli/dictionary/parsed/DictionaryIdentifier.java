package com.gitlab.super7ramp.crosswords.cli.dictionary.parsed;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A dictionary identifier.
 */
public record DictionaryIdentifier(Optional<String> providerName, String dictionaryName) {

    /**
     * Textual representation pattern.
     */
    private static final Pattern PATTERN = Pattern.compile("((?<provider>[^:]+):)?" +
            "(?<dictionary>[^:]+)");

    /**
     * Create a new {@link DictionaryIdentifier} from its textual representation.
     *
     * @param text the textual representation
     * @return the value
     */
    public static DictionaryIdentifier valueOf(final String text) {
        final Matcher matcher = PATTERN.matcher(text);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid format: Expected " + PATTERN.pattern() +
                    ", was " + text);
        }
        final Optional<String> provider = Optional.ofNullable(matcher.group("provider"));
        final String dictionaryName = matcher.group("dictionary");
        return new DictionaryIdentifier(provider, dictionaryName);
    }
}
