/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests on 'croiseur-cli' command.
 */
final class CroiseurCliTest extends CroiseurCliTestRuntime {

    @Test
    void noArg() {
        final int exitCode = cli();

        assertEquals(INPUT_ERROR, exitCode);
        assertEquals("", out());
        assertEquals("""
                     Missing required subcommand
                     Usage: croiseur-cli COMMAND
                                          
                     Commands:
                       help        Display help information about the specified command
                       solver      Solve crosswords and list available solvers
                       dictionary  List and print available dictionaries
                     """, err());
    }

}
