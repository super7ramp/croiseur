/*
 * Copyright (c) 2010, 2021, Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2022, Antoine Belvire.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.gitlab.super7ramp.crosswords.solver.paulgb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;

/**
 * Native library loader.
 * <p>
 * This class provides a way to load native libraries in non-standard but traditional locations.
 * See {@link #loadLibrary} for details.
 * <p>
 * This is based on com.sun.glass.utils.NativeLibLoader from JavaFx.
 *
 * @see
 * <a href="https://github.com/openjdk/jfx/blob/jfx17/modules/javafx.graphics/src/main/java/com/sun/glass/utils/NativeLibLoader.java">Original JavaFX's
 * NativeLibLoader</a>
 * @see
 * <a href="https://github.com/adamheinrich/native-utils/blob/master/src/main/java/cz/adamh/utils/NativeUtils.java">Adam Heinrich's NativeUtils</a>
 */
// TODO simplify
final class NativeLibLoader {

    /** Prevents instantiation. */
    private NativeLibLoader() {
        // Nothing to do.
    }

    /**
     * Loads the given library, using the following lookup strategy:
     * <ol>
     * <li>If the native library comes bundled as a resource it is extracted and loaded;
     * <li>The library is loaded via {@link System#loadLibrary};
     * <li>On iOS, native library is statically linked and detected from the existence of a
     * {@code JNI_OnLoad_libraryname} function.
     * </ol>
     *
     * @param libName name of the library, as one would give it to {@link System#loadLibrary}
     */
    static void loadLibrary(final String libName) {
        final StackWalker walker =
                StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        final Class<?> caller = walker.getCallerClass();
        loadLibraryInternal(libName, caller);
    }

    private static void loadLibraryInternal(final String libraryName, final Class<?> caller) {
        // if the library is available in the jar, copy it to cache and load it from there
        if (loadLibraryFromResource(libraryName, caller)) {
            return;
        }

        // Finally we will use System.loadLibrary.
        try {
            System.loadLibrary(libraryName);
            System.err.println("System.loadLibrary(" + libraryName + ") succeeded");
        } catch (final UnsatisfiedLinkError ex2) {
            //On iOS, we link all libraries statically. Presence of library
            //is recognized by existence of JNI_OnLoad_libraryname() C function.
            //If libraryname contains hyphen, it needs to be translated
            //to underscore to form valid C function identifier.
            if ("ios".equals(System.getProperty("os.name")
                                   .toLowerCase(Locale.ROOT)) && libraryName.contains("-")) {
                final String iosLibraryName = libraryName.replace("-", "_");
                System.loadLibrary(iosLibraryName);
                return;
            }
            // Rethrow exception
            throw ex2;
        }
    }

    /**
     * If there is a library with the platform-correct name at the root of the resources in this
     * jar, use that.
     *
     * @param libraryName the library name
     * @param caller      the caller class
     * @return {@code true} if the library has been successfully loaded, {@code false} otherwise
     */
    private static boolean loadLibraryFromResource(final String libraryName,
                                                   final Class<?> caller) {
        try {
            final String reallib = "/" + System.mapLibraryName(libraryName);
            final InputStream is = caller.getResourceAsStream(reallib);
            if (is != null) {
                final String fp = cacheLibrary(is, reallib, caller);
                System.load(fp);
                System.err.println("Loaded library " + reallib + " from resource");
                return true;
            }
        } catch (final Throwable t) {
            // we should only be here if the resource exists in the module, but
            // for some reasons it can't be loaded.
            System.err.println("Loading library " + libraryName + " from resource failed: " + t);
            t.printStackTrace();
        }
        return false;
    }

    // FIXME should we really cache the extracted library so that it can be reused on next start
    //  (with checksum verification and all) or just extract + delete after use (e.g. using File
    //  .deleteOnExit()) on every starts?
    private static String cacheLibrary(InputStream is, String name, Class<?> caller) throws IOException {
        final String username = System.getProperty("user.name", "anonymous");
        final String tmpCache =
                System.getProperty("java.io.tmpdir") + "/.jni_loader_" + username +
                        "/cache/";
        final File cacheDir = new File(tmpCache);
        if (cacheDir.exists()) {
            if (!cacheDir.isDirectory()) {
                throw new IOException("Cache exists but is not a directory: " + cacheDir);
            }
        } else {
            if (!cacheDir.mkdirs()) {
                throw new IOException("Can not create cache at " + cacheDir);
            }
        }
        // we have a cache directory. Add the file here
        final File f = new File(cacheDir, name);
        // if it exists, calculate checksum and keep if same as input stream.
        boolean write = true;
        if (f.exists()) {
            byte[] isHash;
            byte[] fileHash;
            try {
                DigestInputStream dis = new DigestInputStream(is, MessageDigest.getInstance("MD5"));
                dis.getMessageDigest().reset();
                byte[] buffer = new byte[4096];
                while (dis.read(buffer) != -1) { /* empty loop body is intentional */ }
                isHash = dis.getMessageDigest().digest();
                is.close();
                is = caller.getResourceAsStream(name); // mark/reset not supported, we have to
                // reread
            } catch (final NoSuchAlgorithmException nsa) {
                isHash = new byte[1];
            }
            fileHash = calculateCheckSum(f);
            if (!Arrays.equals(isHash, fileHash)) {
                Files.delete(f.toPath());
            } else {
                // hashes are the same, we already have the file.
                write = false;
            }
        }
        if (write) {
            Path path = f.toPath();
            Files.copy(is, path);
        }

        return f.getAbsolutePath();
    }

    private static byte[] calculateCheckSum(File file) {
        try {
            // not looking for security, just a checksum. MD5 should be faster than SHA
            try (final InputStream stream = new FileInputStream(file); final DigestInputStream dis = new DigestInputStream(stream, MessageDigest.getInstance("MD5"))) {
                dis.getMessageDigest().reset();
                byte[] buffer = new byte[4096];
                while (dis.read(buffer) != -1) { /* empty loop body is intentional */ }
                return dis.getMessageDigest().digest();
            }

        } catch (final IllegalArgumentException | NoSuchAlgorithmException | IOException |
                 SecurityException e) {
            // IOException also covers MalformedURLException
            // SecurityException means some untrusted applet
            // Fall through...
        }
        return new byte[0];
    }

}
