/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli;

import org.junit.jupiter.api.Test;

/**
 * Tests on 'croiseur-cli dictionary *' commands.
 */
final class CroiseurCliDictionaryTest extends FluentTestHelper {

    @Test
    void dictionary() {
        whenOneRunsCli("dictionary");
        thenCli().doesNotWriteToStdOut()
                 .and().writesToStdErr(
                         """
                         Missing required subcommand
                         Usage: croiseur-cli dictionary COMMAND
                         List and print available dictionaries

                         Commands:
                           cat             Display dictionary entries
                           get-default     Return the default dictionary
                           grep, search    Display dictionary entries which match a given pattern
                           list, ls        List available dictionaries
                           list-providers  List available dictionary providers
                         """)
                 .and().exitsWithCode(INPUT_ERROR);
    }

    @Test
    void dictionaryCat() {
        whenOneRunsCli("dictionary", "cat");
        thenCli().doesNotWriteToStdOut()
                 .and().writesToStdErr(
                         """
                         Missing required parameter: 'PROVIDER:DICTIONARY'
                         Usage: croiseur-cli dictionary cat PROVIDER:DICTIONARY
                         Display dictionary entries
                               PROVIDER:DICTIONARY   The identifier of the dictionary to display
                         """)
                 .and().exitsWithCode(INPUT_ERROR);
    }

    @Test
    void dictionaryCatUkacd() {
        whenOneRunsCli("dictionary", "cat",
                       "Local Text Provider:The UK Advanced Cryptics Dictionary");
        thenCli().writes(toStdOut().aTotalOf(250592).lines().startingWith(
                         """
                         A
                         AA
                         AACHEN
                         AARDVARK
                         AARDVARKS
                         AARDWOLF
                         AARDWOLVES
                         AARHUS
                         """))
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void dictionaryCatUnknown() {
        whenOneRunsCli("dictionary", "cat", "unknown:unknown");
        thenCli().doesNotWriteToStdOut()
                 .and().writesToStdErr("No dictionary found\n")
                 .and().exitsWithCode(APPLICATIVE_ERROR);
    }

    @Test
    void dictionaryGetDefault() {
        whenOneRunsCli("dictionary", "get-default");
        thenCli().writesToStdOut(
                         "General British English dictionary, English (United Kingdom), provided by Local XML Provider\n")
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void dictionaryListProviders() {
        whenOneRunsCli("dictionary", "list-providers");
        thenCli().writesToStdOut(
                         """
                         Provider        \tDescription                                          \s
                         --------        \t-----------                                          \s
                         Local Hunspell Provider\tProvides access to local dictionaries in the Hunspell format.
                         Local Text Provider\tProvides access to local dictionaries in a simple text format.
                         Local XML Provider\tProvides access to local dictionaries in an XML format.
                         """).and()
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void dictionaryList() {
        whenOneRunsCli("dictionary", "list");
        thenCli().writesToStdOut(
                         """
                         Provider        \tName                                            	Locale         \s
                         --------        \t----                                            	------         \s
                         Local XML Provider\tGeneral British English dictionary              	English (United Kingdom)
                         Local Text Provider\tThe UK Advanced Cryptics Dictionary             	English (United Kingdom)
                         Local XML Provider\tGeneral German dictionary                       	German (Germany)
                         Local XML Provider\tGeneral Spanish dictionary                      	Spanish (Spain)\s
                         Local XML Provider\tGeneral French dictionary                       	French (France)\s
                         Local XML Provider\tGeneral Italian Dictionary                      	Italian (Italy)\s
                         """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }
}
