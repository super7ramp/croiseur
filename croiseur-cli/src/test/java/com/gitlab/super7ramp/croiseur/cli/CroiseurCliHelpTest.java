/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli;

import org.junit.jupiter.api.Test;

/**
 * Tests on 'croiseur-cli help *' commands.
 */
final class CroiseurCliHelpTest extends FluentTestHelper {

    @Test
    void help() {
        whenOneRunsCli("help");
        thenCli().writesToStdOut(
                         """
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
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void helpUnknown() {
        whenOneRunsCli("help", "unknown");
        thenCli().doesNotWriteToStdOut()
                 .and().writesToStdErr(
                         """
                         Unknown subcommand 'unknown'.
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

    @Test
    void helpDictionary() {
        whenOneRunsCli("help", "dictionary");
        thenCli().writesToStdOut(
                         """
                         Usage: croiseur-cli dictionary COMMAND
                         List and print available dictionaries

                         Commands:
                           cat             Display dictionary entries
                           get-default     Return the default dictionary
                           grep, search    Display dictionary entries which match a given pattern
                           list, ls        List available dictionaries
                           list-providers  List available dictionary providers
                         """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void helpClue() {
        whenOneRunsCli("help", "clue");
        thenCli().writesToStdOut(
                         """
                         Usage: croiseur-cli clue COMMAND
                         Get crossword clues and list available clue providers

                         Commands:
                           get             Get clues for the given words
                           list-providers  List available clue providers
                         """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void helpSolver() {
        whenOneRunsCli("help", "solver");
        thenCli().writesToStdOut(
                         """
                         Usage: croiseur-cli solver COMMAND
                         Solve crosswords and list available solvers

                         Commands:
                           list, ls    List available solvers
                           run, solve  Solve a crossword puzzle
                         """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void helpPuzzle() {
        whenOneRunsCli("help", "puzzle");
        thenCli().writesToStdOut(
                         """
                         Usage: croiseur-cli puzzle COMMAND
                         Manage saved puzzles

                         Commands:
                           cat            Display saved puzzle
                           create         Save a new puzzle
                           delete, rm     Delete a saved puzzle
                           delete-all     Delete all saved puzzles
                           export         Export a puzzle to a file
                           import         Import a puzzle from a file
                           list, ls       List saved puzzles
                           list-decoders  List puzzle decoders
                           list-encoders  List puzzle encoders
                           update         Update a saved puzzle
                         """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }
}
