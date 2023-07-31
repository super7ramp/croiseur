/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli;

import org.junit.jupiter.api.Test;

/**
 * Tests on 'croiseur-cli' command.
 */
final class CroiseurCliTest extends FluentTestHelper {

    @Test
    void noArg() {
        whenOneRunsCli(/* without argument */);
        thenCli().doesNotWriteToStdOut()
                 .and().writesToStdErr(
                         """
                         Missing required subcommand
                         Usage: croiseur-cli COMMAND
                                              
                         Commands:
                           help        Display help information about the specified command
                           dictionary  List and print available dictionaries
                           solver      Solve crosswords and list available solvers
                           clue        Get crossword clues and list available clue providers
                           puzzle      Manage saved puzzles
                         """)
                 .and().exitsWithCode(INPUT_ERROR);
    }

}
