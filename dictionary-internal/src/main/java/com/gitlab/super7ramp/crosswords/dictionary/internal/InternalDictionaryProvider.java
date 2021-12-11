package com.gitlab.super7ramp.crosswords.dictionary.internal;

import com.gitlab.super7ramp.crosswords.dictionary.api.Dictionary;
import com.gitlab.super7ramp.crosswords.dictionary.api.spi.DictionaryProvider;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of {@link DictionaryProvider}.
 */
public class InternalDictionaryProvider implements DictionaryProvider {

    /**
     * Constructor.
     */
    public InternalDictionaryProvider() {
        // Nothing to do.
    }

    private static Stream<URL> toStream(final URL... dictionaryPaths) {
        final Stream<URL> paths;
        if (dictionaryPaths.length == 0) {
            paths = defaultPaths().stream();
        } else {
            paths = Stream.of(dictionaryPaths);
        }
        return paths;
    }

    private static List<URL> defaultPaths() {
        final List<URL> defaultPaths = new ArrayList<>();
        for (final Locale locale : Locale.getAvailableLocales()) {
            final URL path = InternalDictionaryProvider.class.getResource("/" + locale.toString() + ".obj");
            if (path != null) {
                defaultPaths.add(path);
            }
        }
        return defaultPaths;
    }

    @Override
    public String name() {
        return "internal";
    }

    @Override
    public Quality quality() {
        return Quality.GOOD;
    }

    @Override
    public String description() {
        return "Internal binary dictionary backend";
    }

    @Override
    public Collection<Dictionary> get(final URL... dictionaryPaths) {
        return toStream(dictionaryPaths).map(InternalDictionaryService::new).collect(Collectors.toList());
    }
}
