/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli;

import org.junit.jupiter.api.Test;

/**
 * Tests on 'croiseur-cli solver *' commands.
 */
final class CroiseurCliSolverTest extends FluentTestHelper {

    @Test
    void solver() {
        whenOneRunsCli("solver");
        thenCli().doesNotWriteToStdOut()
                 .and().writesToStdErr(
                         """
                         Missing required subcommand
                         Usage: croiseur-cli solver COMMAND
                         Solve crosswords and list available solvers

                         Commands:
                           list, ls    List available solvers
                           run, solve  Solve a crossword puzzle
                         """)
                 .and().exitsWithCode(INPUT_ERROR);
    }

    @Test
    void solverList() {
        whenOneRunsCli("solver", "list");
        thenCli().writesToStdOut(
                         """
                         Name            \tDescription                                          \s
                         ----            	-----------                                          \s
                         Ginsberg        \tA crossword solver based on Ginsberg's papers.       \s
                         Crossword Composer\tThe solver powering the Crossword Composer software. Does not support pre-filled grids.
                         XWords RS       \tThe solver powering the XWords RS tool.              \s
                         """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void solverRun() {
        whenOneRunsCli("solver", "run");
        thenCli().doesNotWriteToStdOut()
                 .and().writesToStdErr(
                         """
                         Missing required option: '--size=<INTEGERxINTEGER>'
                         Usage: croiseur-cli solver run [-p] [-r[=SEED]] -s=<INTEGERxINTEGER> [-b=<
                                                        (COORDINATE,LETTER)>...]...
                                                        [-B=<COORDINATE>...]... [-d=<PROVIDER:
                                                        DICTIONARY>...]... [-H=<(COORDINATE,WORD)
                                                        >...]... [-V=<(COORDINATE,WORD)>...]...
                                                        [<solver>]
                         Solve a crossword puzzle
                               [<solver>]   The name of the solver to use
                           -b, --box, --boxes=<(COORDINATE,LETTER)>...
                                            Pre-filled boxes e.g. '--boxes ((1,2),A) ((3,4),B)...'
                           -B, --shaded-box, --shaded-boxes=<COORDINATE>...
                                            Shaded boxes, e.g. '--shaded-boxes (1,2) (3,4)...'
                           -d, --dictionary, --dictionaries=<PROVIDER:DICTIONARY>...
                                            Dictionary identifiers
                           -H, --across, --horizontal=<(COORDINATE,WORD)>...
                                            Pre-filled horizontal slots, e.g. '--horizontal ((0,0),
                                              hello) ((5,0),world)...
                           -p, --progress   Show solver progress
                           -r, --random, --shuffle[=SEED]
                                            Shuffle the dictionaries before solving
                           -s, --size=<INTEGERxINTEGER>
                                            Grid dimensions, e.g. '--size 7x15' for a grid of width 7
                                              and height 15
                           -V, --down, --vertical=<(COORDINATE,WORD)>...
                                            Pre-filled vertical slots, e.g. '--vertical ((0,0),hello)
                                              ((5,0),world)...
                         """)
                 .and().exitsWithCode(INPUT_ERROR);
    }

    @Test
    void solverRunSize4x4Success() {
        whenOneRunsCli("solver", "run", "--size", "4x4");
        thenCli().writesToStdOut(
                         """
                         Result: SUCCESS

                         |F|T|P|S|
                         |L|O|L|A|
                         |O|U|E|N|
                         |C|R|A|G|

                         """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void solverRunSize4x4PrefilledSlot() {
        whenOneRunsCli(
                "solver", "run",
                "--size", "4x4",
                "--down", "((0,0),LOLA)",
                "--shaded-boxes", "(1,0)", "(2,1)"
        );
        thenCli().writesToStdOut(
                         """
                         Result: SUCCESS

                         |L|#|N|T|
                         |O|A|#|U|
                         |L|A|N|E|
                         |A|C|T|S|

                         """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void solverRunSize4x4PrefilledSlotInconsistent() {
        whenOneRunsCli(
                "solver", "run",
                "--size", "4x4",
                "--down", "((0,0),LOLA)",
                "--across", "((0,0),STOP)", // S != L at (0,0)
                "--shaded-boxes", "(1,0)", "(2,1)"
        );
        thenCli().doesNotWriteToStdOut()
                 .and().writes(toStdErr().startingWith(
                         "java.lang.IllegalArgumentException: Conflict in prefilled boxes"))
                 .and().exitsWithCode(RUNTIME_ERROR);
    }

    @Test
    void solverRunSize4x4PrefilledBoxes() {
        whenOneRunsCli(
                "solver", "run",
                "--size", "4x4",
                "--boxes", "((0,0),L)", "((0,1),O)", "((0,2),L)", "((0,3),A)",
                "--shaded-boxes", "(1,0)", "(2,1)"
        );
        thenCli().writesToStdOut(
                         """
                         Result: SUCCESS

                         |L|#|N|T|
                         |O|A|#|U|
                         |L|A|N|E|
                         |A|C|T|S|

                         """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void solverRunSize4x4NoSolution() {
        whenOneRunsCli(
                "solver", "run",
                "--size", "4x4",
                "--down", "((0,0),BBBB)", // "BBBB" not in dictionary
                "--shaded-boxes", "(1,0)", "(2,1)"
        );
        thenCli().writesToStdOut(
                         """
                         Result: IMPOSSIBLE

                         |B|#| |
                         |B| |#|
                         |B| | |
                         |B| | |

                         """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS); // TODO not a success exit code?
    }

    @Test
    void solverRunXWordsRsSize3x3() {
        whenOneRunsCli("solver", "run", "XWords RS", "--size", "3x3");
        thenCli().writesToStdOut(
                         """
                         Result: SUCCESS

                         |Z|U|Z|
                         |U|T|U|
                         |Z|U|G|

                         """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void solverRunCrosswordComposerSize3x3() {
        whenOneRunsCli("solver", "run", "Crossword Composer", "--size", "3x3")
                .thenCli().writesToStdOut(
                        """
                        Result: SUCCESS

                        |G|A|D|
                        |L|B|J|
                        |C|M|D|

                        """)
                .and().doesNotWriteToStdErr()
                .and().exitsWithCode(SUCCESS);
    }
}
