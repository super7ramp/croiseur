/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
                         SAT             \tA crossword solver based on Sat4j default pseudo-boolean solver. Slow and memory intensive.
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
                         Missing required option: '--size=INTEGERxINTEGER'
                         Usage: croiseur-cli solver run [-cpS] [-r[=SEED]] -s=INTEGERxINTEGER [-b=
                                                        (COORDINATE,LETTER)...]... [-B=COORDINATE...]...
                                                        [-d=PROVIDER:DICTIONARY...]... [-H=(COORDINATE,
                                                        WORD)...]... [-V=(COORDINATE,WORD)...]...
                                                        [SOLVER]
                         Solve a crossword puzzle
                               [SOLVER]     The name of the solver to use
                           -b, --box, --boxes=(COORDINATE,LETTER)...
                                            Pre-filled boxes e.g. '--boxes ((1,2),A) ((3,4),B)...'
                           -B, --shaded-box, --shaded-boxes=COORDINATE...
                                            Shaded boxes, e.g. '--shaded-boxes (1,2) (3,4)...'
                           -c, --clues      Generate clues if solver finds a solution
                           -d, --dictionary, --dictionaries=PROVIDER:DICTIONARY...
                                            Dictionary identifiers
                           -H, --across, --horizontal=(COORDINATE,WORD)...
                                            Pre-filled horizontal slots, e.g. '--horizontal ((0,0),
                                              hello) ((5,0),world)...'
                           -p, --progress   Show solver progress
                           -r, --random, --shuffle[=SEED]
                                            Shuffle the dictionaries before solving
                           -s, --size=INTEGERxINTEGER
                                            Grid dimensions, e.g. '--size 7x15' for a grid of width 7
                                              and height 15
                           -S, --save       Save the grid. Grid will be saved before solving. Grid will
                                              then be saved after solving, if solving is successful.
                           -V, --down, --vertical=(COORDINATE,WORD)...
                                            Pre-filled vertical slots, e.g. '--vertical ((0,0),hello)
                                              ((5,0),world)...'

                         Example:
                                                  
                         The following command asks Croiseur to solve a grid of 6 columns and 7 rows,
                         with blocks and pre-filled boxes.
                                                  
                         		croiseur-cli solver run \\
                         		--size 6x7  \\
                         		--shaded-boxes '(1,1)' '(2,2)' '(3,5')' '(5,4)' \\
                         		--down '((1,2),cross)' \\
                         		--across '((0,4),words)' \\
                                                  
                         Note that the index of the boxes starts at 0 and not at 1. Also note that the
                         command does not define a particular solver or a dictionary: Therefore Croiseur
                         will select them for you.
                                                  
                         The command command quickly produces a result such as the one below:
                                                  
                         	Result: SUCCESS
                                                  
                         	|C|R|O|S|S|S|
                         	|A|#|M|I|T|O|
                         	|T|C|#|D|E|W|
                         	|A|R|D|E|N|S|
                         	|W|O|R|D|S|#|
                         	|B|S|A|#|O|T|
                         	|A|S|T|O|N|S|
                                                  
                         Numerous options exist, notably --random which allows to produce a different
                         result for each invocation. Don't hesitate to explore them!
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
    void solverRunAndSaveSize4x4Success() {
        cli("solver", "run", "--size", "4x4", "--save");
        thenCli().writesToStdOut(
                         """
                         Saved puzzle.

                         Identifier: 1
                         Revision: 1
                         Title:\s
                         Author:\s
                         Editor:\s
                         Copyright:\s
                         Date: $TODAY
                         Grid:

                         Across:
                         Down:

                         Result: SUCCESS

                         |F|T|P|S|
                         |L|O|L|A|
                         |O|U|E|N|
                         |C|R|A|G|

                         Saved puzzle.

                         Identifier: 1
                         Revision: 2
                         Title:\s
                         Author:\s
                         Editor:\s
                         Copyright:\s
                         Date: $TODAY
                         Grid:
                         |F|T|P|S|
                         |L|O|L|A|
                         |O|U|E|N|
                         |C|R|A|G|
                         Across:
                         Down:

                         """.replace("$TODAY", LocalDate.now().toString()))
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
                 // TODO improve error - printing an exception is not great
                 .and().writes(toStdErr().startingWith(
                         "java.lang.IllegalArgumentException: Conflict in prefilled boxes"))
                 .and().exitsWithCode(APPLICATIVE_ERROR);
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
                 .and().exitsWithCode(NO_SOLUTION_FOUND);
    }

    @Test
    void solverRunAndSaveSize4x4NoSolution() {
        whenOneRunsCli(
                "solver", "run",
                "--size", "4x4",
                "--down", "((0,0),BBBB)", // "BBBB" not in dictionary
                "--shaded-boxes", "(1,0)", "(2,1)",
                "--save"
        );
        thenCli().writesToStdOut(
                         """
                         Saved puzzle.

                         Identifier: 1
                         Revision: 1
                         Title:\s
                         Author:\s
                         Editor:\s
                         Copyright:\s
                         Date: $TODAY
                         Grid:
                         |B|#| |
                         |B| |#|
                         |B| | |
                         |B| | |
                         Across:
                         Down:

                         Result: IMPOSSIBLE

                         |B|#| |
                         |B| |#|
                         |B| | |
                         |B| | |

                         """.replace("$TODAY", LocalDate.now().toString()))
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(NO_SOLUTION_FOUND);
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

    @Test
    void solverRunSatSize3x3() {
        whenOneRunsCli("solver", "run", "SAT", "--size", "3x3")
                .thenCli().writesToStdOut(
                        """
                        Result: SUCCESS

                        |O|D|D|
                        |L|B|J|
                        |A|I|D|

                        """)
                .and().doesNotWriteToStdErr()
                .and().exitsWithCode(SUCCESS);
    }

    @AfterEach
    void cleanRepository() {
        drainErr();
        cli("puzzle", "delete-all");
        assertEquals("", err());
        assertEquals(SUCCESS, exitCode());
    }
}
