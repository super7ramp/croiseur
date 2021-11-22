package com.gitlab.super7ramp.crosswords.dictionary.internal;

import com.gitlab.super7ramp.crosswords.dictionary.api.Dictionary;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class InternalDictionaryService implements Dictionary {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(InternalDictionaryService.class.getName());

    /** The actual data. */
    private final InternalDictionary data;

    /**
     * Constructor.
     *
     * @param path path to the serialized object
     */
    InternalDictionaryService(final URL path) {
        data = deserialize(path);
    }

    private static InternalDictionary deserialize(final URL path) {
        Objects.requireNonNull(path);
        try (final ObjectInputStream ois = new ObjectInputStream(path.openStream())) {
            return (InternalDictionary) ois.readObject();
        } catch (final IOException | ClassNotFoundException e) {
            throw new MissingResourceException("Failed to deserialize dictionary at " + path,
                    InternalDictionary.class.getName(), "");
        }
    }

    @Override
    public Locale locale() {
        return data.locale();
    }

    @Override
    public Set<String> lookup(final Predicate<String> predicate) {
        return data.entries().stream().filter(predicate).collect(Collectors.toSet());
    }
}
