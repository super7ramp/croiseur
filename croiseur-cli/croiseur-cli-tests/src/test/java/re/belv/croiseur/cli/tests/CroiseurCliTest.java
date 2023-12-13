/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.tests;

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
                                                  
                         First time with Croiseur? Try this out:
                                                  
                         	croiseur-cli solver run --size 4x4
                                                  
                         This command will generate your first square grid! Next step: Discover the
                         options and the examples of the 'solver run' subcommand with:
                                                  
                         	croiseur-cli solver run --help
                         """)
                 .and().exitsWithCode(INPUT_ERROR);
    }

}
