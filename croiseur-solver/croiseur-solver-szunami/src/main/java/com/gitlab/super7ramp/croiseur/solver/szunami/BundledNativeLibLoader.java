/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.szunami;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Native library loader.
 * <p>
 * This class allows to load native libraries bundled in jar.
 *
 * @see
 * <a href="https://github.com/adamheinrich/native-utils/blob/master/src/main/java/cz/adamh/utils/NativeUtils.java">
 * Adam Heinrich's NativeUtils</a>: BundledNativeLibLoader is basically a
 * simplification/appropriation attempt of NativeUtils.
 * @see
 * <a href="https://github.com/openjdk/jfx/blob/jfx17/modules/javafx.graphics/src/main/java/com/sun/glass/utils/NativeLibLoader.java">JavaFX's
 * NativeLibLoader</a>: JavaFX version, more complete but more complex and with an uncompatible
 * licence. Additional features:
 * <ul>
 *     <li>Looks for native library in potentially different caller jar (class is public and can
 *     be called by other modules)</li>
 *     <li>Checks for already extracted native library before trying to unbundle it; Computes and
 *     compares file checksums to determine if extraction is needed;
 *     <li>Falls back to System.loadLibrary if no native library found in the jar;
 *     <li>Keeps the native libary extracted from jar inside temporary folder after VM shutdown.
 * </ul>
 */
final class BundledNativeLibLoader {

    /** The prefix of the temporary directory in which is put the extracted native library. */
    private static final String NATIVE_FOLDER_PATH_PREFIX = "native";

    /**
     * The minimum length a prefix for a file has to have according to
     * {@link File#createTempFile(String, String)}.
     */
    private static final int MIN_PREFIX_LENGTH = 3;

    /** Prevents instantiation. */
    private BundledNativeLibLoader() {
        // Nothing to do.
    }

    /**
     * Loads the given library bundled at the root of the jar containing this code.
     * <p>
     * The file from jar is copied into a temporary directory (itself inside system's temporary
     * directory) and then loaded. Both the extracted library and temporary directory are deleted
     * after exiting the JVM.
     *
     * @param libraryName name of the library, as one would give it to {@link System#loadLibrary}
     * @throws UnsatisfiedLinkError if bundled library cannot be loaded for any reason
     */
    static void loadLibrary(final String libraryName) {

        final String platformLibraryName = System.mapLibraryName(libraryName);
        if (platformLibraryName == null || platformLibraryName.length() < MIN_PREFIX_LENGTH) {
            throw new UnsatisfiedLinkError("The filename has to be at least 3 characters long");
        }

        try (final InputStream libraryInputStream =
                     BundledNativeLibLoader.class.getResourceAsStream("/" + platformLibraryName)) {

            if (libraryInputStream == null) {
                throw new UnsatisfiedLinkError("No bundled native library named " + platformLibraryName);
            }

            final File extractedLibrary = extractLibrary(libraryInputStream, platformLibraryName);
            System.load(extractedLibrary.getAbsolutePath());

        } catch (final IOException e) {
            throw new UnsatisfiedLinkError(e.getMessage());
        }
    }

    /**
     * Extracts the library read from given {@link InputStream} inside a file with given file name.
     *
     * @param libraryInputStream the input stream reading the bundled library
     * @param libraryFilename    the desired extracted library file name
     * @return the extracted library file
     * @throws IOException if temporary directory creation or read from the input stream fails
     */
    private static File extractLibrary(final InputStream libraryInputStream,
                                       final String libraryFilename) throws IOException {
        final File temporaryDir = createTemporaryDirectory();
        final File library = new File(temporaryDir, libraryFilename);
        library.deleteOnExit();
        Files.copy(libraryInputStream, library.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return library;
    }

    /**
     * Creates a temporary directory inside system's temporary directory.
     * <p>
     * The temporary directory is named {@value NATIVE_FOLDER_PATH_PREFIX} and suffixed with a
     * timestamp.
     *
     * @return the created temporary directory
     * @throws IOException if directory cannot be created
     */
    private static File createTemporaryDirectory() throws IOException {
        final String tempDir = System.getProperty("java.io.tmpdir");
        final File generatedDir = new File(tempDir, NATIVE_FOLDER_PATH_PREFIX + System.nanoTime());
        if (!generatedDir.mkdir()) {
            throw new IOException("Failed to create temporary directory " + generatedDir.getName());
        }
        generatedDir.deleteOnExit();
        return generatedDir;
    }
}
