/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli;

import org.junit.jupiter.api.Test;

/**
 * Tests on 'croiseur-cli' command.
 */
final class CroiseurCliTest extends CroiseurCliFluentTestHelper {

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
                           solver      Solve crosswords and list available solvers
                           dictionary  List and print available dictionaries
                         """)
                 .and().exitsWithCode(INPUT_ERROR);
    }

}
