package com.gitlab.super7ramp.crosswords.dictionary.internal;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryDescription;
import com.gitlab.super7ramp.crosswords.dictionary.common.SegmentableUrl;
import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.stream.Stream;

public final class InternalDictionaryService implements Dictionary {

    /** The actual data. */
    private final InternalDictionary data;

    /** The dictionary name. */
    private final String name;

    /**
     * Constructor.
     *
     * @param path path to the serialized object
     */
    InternalDictionaryService(final URL path) {
        data = deserialize(path);
        name = new SegmentableUrl(path).lastPathSegment();
    }

    /**
     * Deserialize a {@link InternalDictionary} at given path.
     *
     * @param path serialized object path
     * @return the deserialized object
     */
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
    public DictionaryDescription description() {
        return new DictionaryDescription(name, data.locale());
    }

    @Override
    public Stream<String> stream() {
        return data.entries().stream();
    }

}
