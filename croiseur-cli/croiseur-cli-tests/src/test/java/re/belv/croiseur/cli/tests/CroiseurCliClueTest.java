/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.tests;

import org.junit.jupiter.api.Test;

/** Tests on 'croiseur-cli clue *' commands. */
final class CroiseurCliClueTest extends FluentTestHelper {

    @Test
    void clue() {
        whenOneRunsCli("clue");
        thenCli()
                .doesNotWriteToStdOut()
                .and()
                .writesToStdErr(
                        """
                         Missing required subcommand
                         Usage: croiseur-cli clue COMMAND
                         Get crossword clues and list available clue providers

                         Commands:
                           get             Get clues for the given words
                           list-providers  List available clue providers
                         """)
                .and()
                .exitsWithCode(INPUT_ERROR);
    }

    @Test
    void getCluesUnknownProvider() {
        whenOneRunsCli("clue", "get", "--provider", "unknown", "CROSSWORD");
        thenCli()
                .doesNotWriteToStdOut()
                .and()
                .writesToStdErr("No clue provider found\n")
                .and()
                .exitsWithCode(APPLICATIVE_ERROR);
    }

    @Test
    void listProviders() {
        whenOneRunsCli("clue", "list-providers");
        thenCli()
                .writesToStdOut(
                        """
                                 Name            	Description                                          \s
                                 ----            	-----------                                          \s
                                 OpenAI          	Clue generator backed by OpenAI's ChatGPT service    \s
                                 """)
                .and()
                .doesNotWriteToStdErr()
                .and()
                .exitsWithCode(SUCCESS);
    }
}
