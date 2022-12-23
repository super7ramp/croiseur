package com.gitlab.super7ramp.crosswords.dictionary.internal.plugin;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

/**
 * Implementation of {@link DictionaryProvider}.
 */
public final class InternalDictionaryProvider implements DictionaryProvider {

    /**
     * Constructor.
     */
    public InternalDictionaryProvider() {
        // Nothing to do.
    }

    private static List<URL> defaultPaths() {
        final List<URL> defaultPaths = new ArrayList<>();
        for (final Locale locale : Locale.getAvailableLocales()) {
            final URL path =
                    InternalDictionaryProvider.class.getResource("/" + locale.toString() + ".obj");
            if (path != null) {
                defaultPaths.add(path);
            }
        }
        return defaultPaths;
    }

    @Override
    public DictionaryProviderDescription description() {
        return new DictionaryProviderDescription("internal", "Internal binary dictionary backend");
    }

    @Override
    public Collection<Dictionary> get() {
        return defaultPaths().stream().map(InternalDictionaryService::new).collect(toList());
    }
}
