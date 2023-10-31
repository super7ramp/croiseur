/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests on {@link DictionaryPath}.
 */
final class DictionaryPathTest {

    @TempDir
    private Path systemDictionaryPath;

    @BeforeEach
    void before() {
        System.setProperty(DictionaryPath.SYSTEM_PROPERTY, systemDictionaryPath.toString());
    }

    @AfterEach
    void after() {
        System.clearProperty(DictionaryPath.SYSTEM_PROPERTY);
    }

    @Test
    void split() {
        final String p1 = Path.of("a", "b", "c").toString();
        final String p2 = Path.of("d", "e", "f").toString();
        final String p3 = Path.of("g", "h").toString();
        // "/a/b/c:/d/e/f:/g/h/" on Unix
        final String path = String.join(File.pathSeparator, new String[]{p1, p2, p3});

        final DictionaryPath dictionaryPath = DictionaryPath.of(path);
        final List<String> paths = dictionaryPath.split();

        assertEquals(List.of(p1, p2, p3), paths);
    }

    @Test
    void list() throws IOException {
        Files.createFile(systemDictionaryPath.resolve("dic1"));
        Files.createFile(systemDictionaryPath.resolve("dic2"));
        Files.createFile(systemDictionaryPath.resolve("dic3"));
        Files.createDirectory(systemDictionaryPath.resolve("folder")); // will be ignored

        final List<File> dictionaries = DictionaryPath.getDefault().list();
        assertEquals(3, dictionaries.size());

        final Set<String> dictionaryNames =
                dictionaries.stream().map(File::getName).collect(toSet());
        assertEquals(Set.of("dic1", "dic2", "dic3"), dictionaryNames);
    }
}
