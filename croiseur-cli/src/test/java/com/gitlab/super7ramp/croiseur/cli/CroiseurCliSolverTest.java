/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests on 'croiseur-cli solver *' commands.
 */
final class CroiseurCliSolverTest extends CroiseurCliTestRuntime {

    @Test
    void solver() {
        final int exitCode = cli("solver");

        assertEquals(INPUT_ERROR, exitCode);
        assertEquals("", out());
        assertEquals("""
                     Missing required subcommand
                     Usage: croiseur-cli solver COMMAND
                     Solve crosswords and list available solvers

                     Commands:
                       list, ls    List available solvers
                       run, solve  Solve a crossword puzzle
                     """, err());
    }

    @Test
    void solverList() {
        final int exitCode = cli("solver", "list");

        assertEquals(SUCCESS, exitCode);
        assertEquals("""
                     Name            \tDescription                                          \s
                     ----            	-----------                                          \s
                     Ginsberg        \tA crossword solver based on Ginsberg's papers.       \s
                     Crossword Composer\tThe solver powering the Crossword Composer software. Does not support pre-filled grids.
                     XWords RS       \tThe solver powering the XWords RS tool.              \s
                     """, out());
        assertEquals("", err());
    }

    @Test
    void solverRun() {
        final int exitCode = cli("solver", "run");

        assertEquals(INPUT_ERROR, exitCode);
        assertEquals("", out());
        assertEquals("""
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
                     """, err());
    }

    @Test
    void solverRunSize4x4Success() {
        final int exitCode = cli("solver", "run", "--size", "4x4");

        assertEquals(SUCCESS, exitCode);
        assertEquals("""
                     Result: SUCCESS

                     |F|T|P|S|
                     |L|O|L|A|
                     |O|U|E|N|
                     |C|R|A|G|

                     """, out());
        assertEquals("", err());
    }

    @Test
    void solverRunSize4x4Prefilled() {
        final int exitCode = cli(
                "solver", "run",
                "--size", "4x4",
                "--down", "((0,0),LOLA)",
                "--shaded-boxes", "(1,0)", "(2,1)"
        );

        assertEquals(SUCCESS, exitCode);
        assertEquals("""
                     Result: SUCCESS

                     |L| |N|T|
                     |O|A| |U|
                     |L|A|N|E|
                     |A|C|T|S|

                     """, out());
        assertEquals("", err());
    }

    @Test
    void solverRunSize4x4PrefilledInconsistent() {
        final int exitCode = cli(
                "solver", "run",
                "--size", "4x4",
                "--down", "((0,0),LOLA)",
                "--across", "((0,0),STOP)", // S != L at (0,0)
                "--shaded-boxes", "(1,0)", "(2,1)"
        );

        assertEquals(RUNTIME_ERROR, exitCode);
        assertEquals("", out());
        // TODO a less scary error message
        assertEquals("java.lang.IllegalArgumentException: Conflict in prefilled boxes",
                     err().lines().findFirst().orElse(""));
    }

    @Test
    void solverRunSize4x4NoSolution() {
        final int exitCode = cli(
                "solver", "run",
                "--size", "4x4",
                "--down", "((0,0),BBBB)", // "BBBB" not in dictionary
                "--shaded-boxes", "(1,0)", "(2,1)"
        );

        // TODO not a success exit code?
        assertEquals(SUCCESS, exitCode);
        assertEquals("""
                     Result: IMPOSSIBLE

                     |B|
                     |B|
                     |B|
                     |B|

                     """, out());
        assertEquals("", err());
    }
}
