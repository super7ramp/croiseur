package com.gitlab.super7ramp.crosswords.dictionary.hunspell;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

/**
 * Access to dictionaries in well-known paths.
 */
final class DefaultDictionaries {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(DefaultDictionaries.class.getName());

    /**
     * Well-known Hunspell dictionary paths.
     */
    private static final Path[] DICTIONARY_PATHS = {
            Path.of("/", "usr", "share", "hunspell"),
            Path.of("/", "usr", "share", "myspell")
    };

    /**
     * Dictionary extension.
     */
    private static final String DIC_EXTENSION = ".dic";

    /**
     * Private constructor, static methods only.
     */
    private DefaultDictionaries() {
        // Nothing to do.
    }

    /**
     * Get the paths to default dictionaries.
     *
     * @return the paths to default dictionaries
     */
    static List<URL> get() {
        final List<URL> defaultPaths = new ArrayList<>();
        for (final Path directory : DICTIONARY_PATHS) {
            if (Files.isDirectory(directory)) {
                try (final Stream<Path> files = Files.list(directory)) {
                    files
                            .filter(not(Files::isSymbolicLink))
                            .filter(f -> f.getFileName().toString().endsWith(DIC_EXTENSION))
                            .map(DefaultDictionaries::toUrl)
                            .forEach(defaultPaths::add);
                } catch (final IOException e) {
                    LOGGER.log(Level.WARNING, "Failed to open Hunspell dictionary directory", e);
                }
            }
        }
        return defaultPaths;
    }

    private static URL toUrl(Path path) {
        try {
            return path.toUri().toURL();
        } catch (final MalformedURLException e) {
            /*
             * Wrapping into an unchecked exception to allow usage in Stream.
             * Should not happen anyway since file existence is verified beforehand.
             */
            throw new UncheckedIOException(e);
        }
    }
}
