/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests on 'croiseur-cli dictionary *' commands.
 */
final class CroiseurCliDictionaryTest extends CroiseurCliTestRuntime {

    @Test
    void dictionary() {
        final int exitCode = cli("dictionary");

        assertEquals(INPUT_ERROR, exitCode);
        assertEquals("", out());
        assertEquals("""
                     Missing required subcommand
                     Usage: croiseur-cli dictionary COMMAND
                     List and print available dictionaries

                     Commands:
                       cat             Display dictionary entries
                       get-default     Return the default dictionary
                       grep, search    Display dictionary entries which match a given pattern
                       list, ls        List available dictionaries
                       list-providers  List available dictionary providers
                     """, err());
    }

    @Test
    void dictionaryCat() {
        final int exitCode = cli("dictionary", "cat");

        assertEquals(INPUT_ERROR, exitCode);
        assertEquals("", out());
        assertEquals("""
                     Missing required parameter: '<PROVIDER:DICTIONARY>'
                     Usage: croiseur-cli dictionary cat <PROVIDER:DICTIONARY>
                     Display dictionary entries
                           <PROVIDER:DICTIONARY>
                              The identifier of the dictionary to display
                     """, err());
    }

    @Test
    void dictionaryCatUkacd() {
        final int exitCode =
                cli("dictionary", "cat", "Local Text Provider:The UK Advanced Cryptics Dictionary");

        assertEquals(SUCCESS, exitCode);
        final String out = out();
        assertEquals(250592, out.lines().count());
        assertEquals("""
                     A
                     AA
                     AACHEN
                     AARDVARK
                     AARDVARKS
                     AARDWOLF
                     AARDWOLVES
                     AARHUS""", out.lines().limit(8).collect(joining("\n")));
        assertEquals("", err());
    }

    @Test
    void dictionaryCatUnknown() {
        final int exitCode = cli("dictionary", "cat", "unknown:unknown");

        assertEquals(SUCCESS, exitCode); // TODO shouldn't be an error code returned?
        assertEquals("", out());
        assertEquals("No dictionary found\n", err());
    }

    @Test
    @Disabled("To fix")
    void dictionaryGetDefault() {
        final int exitCode = cli("dictionary", "get-default");

        assertEquals(SUCCESS, exitCode);
        assertEquals("General English dictionary, English, provided by Local XML Provider",
                     out()); // FIXME German is returned
        assertEquals("", err());
    }

    @Test
    void dictionaryListProviders() {
        final int exitCode = cli("dictionary", "list-providers");

        assertEquals(SUCCESS, exitCode);
        assertEquals("""
                     Provider        \tDescription                                          \s
                     --------        \t-----------                                          \s
                     Local Hunspell Provider\tProvides access to local dictionaries in the Hunspell format.
                     Local Text Provider\tProvides access to local dictionaries in a simple text format.
                     Local XML Provider\tProvides access to local dictionaries in an XML format.
                     """, out());
        assertEquals("", err());
    }

    @Test
    void dictionaryList() {
        final int exitCode = cli("dictionary", "list");

        assertEquals(SUCCESS, exitCode);
        assertEquals("""
                     Provider        \tName                                            	Locale         \s
                     --------        \t----                                            	------         \s
                     Local XML Provider\tGeneral German dictionary                       	German (Germany)
                     Local Text Provider\tThe UK Advanced Cryptics Dictionary             	English (United Kingdom)
                     Local XML Provider\tGeneral British English dictionary              	English (United Kingdom)
                     Local XML Provider\tGeneral Spanish dictionary                      	Spanish (Spain)\s
                     Local XML Provider\tGeneral French dictionary                       	French (France)\s
                     Local XML Provider\tGeneral Italian Dictionary                      	Italian (Italy)\s
                     """, out());
        assertEquals("", err());
    }
}
