/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.common;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.CodeSource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/** A dictionary path, similar to a classpath. Immutable. */
public final class DictionaryPath {

    /** The dictionary path system property name. */
    public static final String SYSTEM_PROPERTY = "re.belv.croiseur.dictionary.path";

    /**
     * The dictionary path. Contains files or directory path, separated by ':', similarly to a classpath.
     *
     * <p>Example: {@code "/a/directory/:/a/dictionary.extension"}.
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
     * Returns a {@link DictionaryPath} listing all possible locations for dictionaries, i.e. {@link #userSpecific()},
     * {@link #systemDefined()}, and {@link #relativeToJar()}.
     *
     * @return a {@link DictionaryPath} listing all possible locations for dictionaries
     */
    public static DictionaryPath getDefault() {
        return userSpecific().append(systemDefined()).append(relativeToJar());
    }

    /**
     * Returns user-specific dictionary path.
     *
     * @return user-specific dictionary path
     */
    public static DictionaryPath userSpecific() {
        final String path = System.getProperty("user.home") + "/croiseur/dictionaries";
        return new DictionaryPath(path);
    }

    /**
     * Returns the dictionary path set as system property ({@value #SYSTEM_PROPERTY}).
     *
     * @return the dictionary path set as system property; An empty path if system property is not set
     */
    public static DictionaryPath systemDefined() {
        final String path = System.getProperty(SYSTEM_PROPERTY, "");
        return new DictionaryPath(path);
    }

    /**
     * Returns the dictionary path relative to the source code location.
     *
     * @return the dictionary path relative to the source code location
     */
    public static DictionaryPath relativeToJar() {
        final URI codeLocation = codeLocation();
        final String path;
        if (codeLocation != null) {
            final Path parentDir = Path.of(codeLocation).getParent();
            path = parentDir.resolve("dictionaries").toString();
        } else {
            path = "";
        }
        return new DictionaryPath(path);
    }

    /**
     * Returns the source code location as a {@link URI}.
     *
     * @return the source code location as a {@link URI} or {@code null} if location could not be determined
     */
    private static URI codeLocation() {
        final CodeSource codeSource = DictionaryPath.class.getProtectionDomain().getCodeSource();
        try {
            return codeSource != null ? codeSource.getLocation().toURI() : null;
        } catch (final URISyntaxException e) {
            return null;
        }
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
     *
     * <p>Lookup stops at first level (no lookup inside directory contained in given directory).
     *
     * @param file the directory to expand
     * @return the files (not the directories) contained inside given file, provided given file is a directory; If given
     *     file is not a directory, returns a singleton stream containing the given file.
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
        return new DictionaryPath(path + File.pathSeparator + other.path);
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
        return List.of(path.split(File.pathSeparator));
    }

    /**
     * Resolve this path into a list of files.
     *
     * <p>The returned files are "actual" file (i.e. no directory) and exist at the moment of the call.
     *
     * @return the list of files represented by this path, at the moment of the call
     */
    public List<File> list() {
        return split().stream()
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
