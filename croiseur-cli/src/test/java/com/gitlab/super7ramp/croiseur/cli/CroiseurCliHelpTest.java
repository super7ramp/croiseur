/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests on 'croiseur-cli help *' commands.
 */
final class CroiseurCliHelpTest extends CroiseurCliTestRuntime {

    @Test
    void help() {
        final int exitCode = cli("help");

        assertEquals(SUCCESS, exitCode);
        assertEquals("""
                     Usage: croiseur-cli COMMAND
                                          
                     Commands:
                       help        Display help information about the specified command
                       solver      Solve crosswords and list available solvers
                       dictionary  List and print available dictionaries
                     """, out());
        assertEquals("", err());
    }

    @Test
    void helpUnknown() {
        final int exitCode = cli("help", "unknown");

        assertEquals(INPUT_ERROR, exitCode);
        assertEquals("", out());
        assertEquals("""
                     Unknown subcommand 'unknown'.
                     Usage: croiseur-cli COMMAND

                     Commands:
                       help        Display help information about the specified command
                       solver      Solve crosswords and list available solvers
                       dictionary  List and print available dictionaries
                     """, err());
    }

    @Test
    void helpDictionary() {
        final int exitCode = cli("help", "dictionary");

        assertEquals(SUCCESS, exitCode);
        assertEquals("""
                     Usage: croiseur-cli dictionary COMMAND
                     List and print available dictionaries

                     Commands:
                       cat             Display dictionary entries
                       get-default     Return the default dictionary
                       grep, search    Display dictionary entries which match a given pattern
                       list, ls        List available dictionaries
                       list-providers  List available dictionary providers
                     """, out());
        assertEquals("", err());
    }

    @Test
    void helpSolver() {
        final int exitCode = cli("help", "solver");

        assertEquals(SUCCESS, exitCode);
        assertEquals("""
                     Usage: croiseur-cli solver COMMAND
                     Solve crosswords and list available solvers

                     Commands:
                       list, ls    List available solvers
                       run, solve  Solve a crossword puzzle
                     """, out());
        assertEquals("", err());
    }
}
