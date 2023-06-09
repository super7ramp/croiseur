/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli;

import org.junit.jupiter.api.Test;

/**
 * Tests on 'croiseur-cli help *' commands.
 */
final class CroiseurCliHelpTest extends CroiseurCliFluentTestHelper {

    @Test
    void help() {
        whenOneRunsCli("help");
        thenCli().writesToStdOut(
                         """
                         Usage: croiseur-cli COMMAND

                         Commands:
                           help        Display help information about the specified command
                           solver      Solve crosswords and list available solvers
                           dictionary  List and print available dictionaries
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
                           solver      Solve crosswords and list available solvers
                           dictionary  List and print available dictionaries
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
}
