/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.common;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A dictionary path, similar to a classpath. Immutable.
 */
public final class DictionaryPath {

    /** The dictionary path system property name. */
    public static final String SYSTEM_PROPERTY =
            "com.gitlab.super7ramp.croiseur.dictionary.path";

    /**
     * The dictionary path. Contains files or directory path, separated by ':', similarly to a
     * classpath.
     * <p>
     * Example: {@code "/a/directory/*:/a/dictionary.extension"}.
     */
    private final String path;

    /**
     * Constructs an instance.
     *
     * @param pathArg the path
     */
    private DictionaryPath(final String pathArg) {
        path = Objects.requireNonNull(pathArg);
    }

    /**
     * Returns a {@link DictionaryPath} listing all possible locations for dictionaries, i.e.
     * both {@link #system()} and {@link #user()}.
     *
     * @return a {@link DictionaryPath} listing all possible locations for dictionaries
     */
    public static DictionaryPath getDefault() {
        return user().append(system());
    }

    /**
     * Returns the dictionary path set as system property.
     *
     * @return the dictionary path set as system property
     */
    public static DictionaryPath system() {
        final String path = System.getProperty(SYSTEM_PROPERTY, "");
        return new DictionaryPath(path);
    }

    /**
     * Returns user-specific dictionary path.
     *
     * @return user-specific dictionary path
     */
    public static DictionaryPath user() {
        // TODO to implement
        return new DictionaryPath("");
    }

    /**
     * Creates a new {@link DictionaryPath} from given string representation.
     *
     * @param path the path as a string
     * @return a new {@link DictionaryPath}
     */
    public static DictionaryPath of(final String path) {
        return new DictionaryPath(path);
    }

    /**
     * Returns an empty dictionary path.
     *
     * @return an empty dictionary path
     */
    public static DictionaryPath empty() {
        return new DictionaryPath("");
    }

    /**
     * Streams the files (not the directories) contained inside given directory.
     * <p>
     * Lookup stops at first level (no lookup inside directory contained in given directory).
     *
     * @param file the directory to expand
     * @return the files (not the directories) contained inside given file, provided given
     * file is a directory; If given file is not a directory, returns a singleton stream
     * containing the given file.
     */
    private static Stream<File> expandDirectory(final File file) {
        final File[] files = file.listFiles(File::isFile);
        return files != null ? Arrays.stream(files) : Stream.of(file);
    }

    /**
     * Creates a new {@link DictionaryPath} with given path added at the end of this path.
     *
     * @param other the path to append
     * @return a new {@link DictionaryPath} with given path added at the end of this path
     */
    public DictionaryPath append(final DictionaryPath other) {
        return new DictionaryPath(path + ':' + other.path);
    }

    /**
     * Creates a new {@link DictionaryPath} with given path added at the beginning of this path.
     *
     * @param other the path to prepend
     * @return a new {@link DictionaryPath} with given path added at the beginning of this path
     */
    public DictionaryPath prepend(final DictionaryPath other) {
        return other.append(this);
    }

    /**
     * Splits the dictionary path into individual paths.
     *
     * @return the dictionary path into individual paths
     */
    public List<String> split() {
        return List.of(path.split(":"));
    }

    /**
     * Resolve this path into a list of files.
     * <p>
     * The returned files are "actual" file (i.e. no directory) and exist at the moment of the call.
     *
     * @return the list of files represented by this path, at the moment of the call
     */
    public List<File> list() {
        return split().stream()
                      .map(s -> s.replace("/*", ""))
                      .map(File::new)
                      .filter(f -> f.exists() && !Files.isSymbolicLink(f.toPath()))
                      .flatMap(DictionaryPath::expandDirectory)
                      .toList();
    }

    @Override
    public String toString() {
        return "DictionaryPath{path='" + path + "'}";
    }
}
