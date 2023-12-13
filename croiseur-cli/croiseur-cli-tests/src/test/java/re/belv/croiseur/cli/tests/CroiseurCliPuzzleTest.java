/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests on 'croiseur-cli puzzle *' commands.
 */
final class CroiseurCliPuzzleTest extends FluentTestHelper {

    @Test
    void puzzle() {
        whenOneRunsCli("puzzle");
        thenCli().doesNotWriteToStdOut()
                 .and().writesToStdErr(
                         """
                         Missing required subcommand
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
                 .and().exitsWithCode(INPUT_ERROR);
    }

    @Test
    void create() {
        whenOneRunsCli("puzzle", "create",
                       "--title", "Example Grid",
                       "--author", "Me",
                       "--editor", "Nobody",
                       "--copyright", "CC-0",
                       "--date", "2023-06-21",
                       "--rows", "...,ABC,#D.",
                       "--across-clue", "2,Some Very.",
                       "--down-clue", "1,Dummy.",
                       "--down-clue", "3,Clues.");
        thenCli().writesToStdOut("""
                                 Saved puzzle.
                                                                           
                                 Identifier: 1
                                 Revision: 1
                                 Title: Example Grid
                                 Author: Me
                                 Editor: Nobody
                                 Copyright: CC-0
                                 Date: 2023-06-21
                                 Grid:
                                 | | | |
                                 |A|B|C|
                                 |#|D| |
                                 Across:
                                 2. Some Very.
                                 Down:
                                 1. Dummy.
                                 3. Clues.

                                 """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void update() {
        givenOneHasRunCli("puzzle", "create",
                          "--author", "Me",
                          "--date", "2023-06-22",
                          "--rows", "...,ABC,#D.");
        whenOneRunsCli("puzzle", "update", "1",
                       "--title", "Example",
                       "--rows", "XYZ,ABC,#D.",
                       "--across-clue", "1,A clue.");
        thenCli().writesToStdOut("""
                                 Saved puzzle.
                                                                           
                                 Identifier: 1
                                 Revision: 2
                                 Title: Example
                                 Author: Me
                                 Editor:\s
                                 Copyright:\s
                                 Date: 2023-06-22
                                 Grid:
                                 |X|Y|Z|
                                 |A|B|C|
                                 |#|D| |
                                 Across:
                                 1. A clue.
                                 Down:

                                 """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void update_missing() {
        whenOneRunsCli("puzzle", "update", "1", "--title", "Example", "--rows", "XYZ,ABC,#D.");
        thenCli().doesNotWriteToStdOut()
                 .and()
                 .writesToStdErr("Failed to update puzzle: Cannot find saved puzzle with id 1\n")
                 .and().exitsWithCode(APPLICATIVE_ERROR);
    }

    @Test
    void delete() {
        givenOneHasRunCli("puzzle", "create", "--rows", "...,ABC,#D.");
        whenOneRunsCli("puzzle", "delete", "1");
        thenCli().writesToStdOut("Deleted puzzle #1.\n")
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void deleteAll() {
        givenOneHasRunCli("puzzle", "create", "--rows", "...,ABC,#D.");
        givenOneHasRunCli("puzzle", "create", "--rows", "...,ABC,#D.");

        whenOneRunsCli("puzzle", "delete-all");

        thenCli().writesToStdOut("Deleted all puzzles.\n")
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void cat() {
        givenOneHasRunCli("puzzle", "create", "--author", "Me", "--date", "2023-06-22", "--rows",
                          "...,ABC,#D.");
        whenOneRunsCli("puzzle", "cat", "1");
        thenCli().writesToStdOut(
                         """                                                                     
                         Identifier: 1
                         Revision: 1
                         Title:\s
                         Author: Me
                         Editor:\s
                         Copyright:\s
                         Date: 2023-06-22
                         Grid:
                         | | | |
                         |A|B|C|
                         |#|D| |
                         Across:
                         Down:
                                                                   
                         """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void list() {
        givenOneHasRunCli("puzzle", "create",
                          "--title", "First Example",
                          "--author", "Me",
                          "--date", "2023-06-21",
                          "--rows", "...,ABC,#D.");
        whenOneRunsCli("puzzle", "list");
        thenCli().writesToStdOut("""
                                 Id  	Rev 	Title           	Author          	Date           \s
                                 --  	--- 	-----           	------          	----           \s
                                 1   	1   	First Example   	Me              	2023-06-21     \s
                                 """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void list_empty() {
        whenOneRunsCli("puzzle", "list");
        thenCli().writesToStdOut("No saved puzzle.\n")
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void list_afterDeleteAll() {
        givenOneHasRunCli("puzzle", "create", "--rows", "...,ABC,#D.");
        givenOneHasRunCli("puzzle", "create", "--rows", "...,ABC,#D.");
        givenOneHasRunCli("puzzle", "delete-all");

        whenOneRunsCli("puzzle", "list");

        thenCli().writesToStdOut("No saved puzzle.\n")
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void listDecoders() {
        whenOneRunsCli("puzzle", "list-decoders");
        thenCli().writesToStdOut(
                         """
                         Name            	Description                     	Supported formats
                         ----            	-----------                     	-----------------
                         xd              	xd format decoder               	*.xd           \s
                         """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void importPuzzle(@TempDir final Path tempDir) throws IOException {
        final Path example = tempDir.resolve("example.xd");
        givenPuzzle(example, """
                             Title: Example Grid
                             Author: Jane Doe
                             Editor: John Doe
                             Date: 2023-06-19


                             ABC
                             DEF
                             GHI


                             A1. Start. ~ ABC
                             A2. Middle. ~ DEF
                             A3. End. ~ GHI

                             D1. Some Very. ~ ADG
                             D2. Dummy. ~ BEH
                             D3. Clues. ~ CFI
                             """);
        whenOneRunsCli("puzzle", "import", example.toString());
        thenCli().writesToStdOut(
                         """
                         Saved puzzle.

                         Identifier: 1
                         Revision: 1
                         Title: Example Grid
                         Author: Jane Doe
                         Editor: John Doe
                         Copyright:\s
                         Date: 2023-06-19
                         Grid:
                         |A|B|C|
                         |D|E|F|
                         |G|H|I|
                         Across:
                         1. Start.
                         2. Middle.
                         3. End.
                         Down:
                         1. Some Very.
                         2. Dummy.
                         3. Clues.

                         """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    /**
     * Writes the given puzzle content at given path.
     *
     * @param path    where to write the puzzle
     * @param content the content to write
     * @throws IOException if file couldn't be written
     */
    private void givenPuzzle(final Path path, final String content) throws IOException {
        Files.writeString(path, content);
    }

    @Test
    void importPuzzle_formatOption(@TempDir final Path tempDir) throws IOException {
        final Path example = tempDir.resolve("example");
        givenPuzzle(example, """
                             Title: Example Grid
                             Author: Jane Doe
                             Editor: John Doe
                             Date: 2023-06-19


                             ABC
                             DEF
                             GHI


                             A1. Start. ~ ABC
                             A2. Middle. ~ DEF
                             A3. End. ~ GHI

                             D1. Some Very. ~ ADG
                             D2. Dummy. ~ BEH
                             D3. Clues. ~ CFI
                             """);
        whenOneRunsCli("puzzle", "import", "--format", "*.xd", example.toString());
        thenCli().writesToStdOut(
                         """
                         Saved puzzle.

                         Identifier: 1
                         Revision: 1
                         Title: Example Grid
                         Author: Jane Doe
                         Editor: John Doe
                         Copyright:\s
                         Date: 2023-06-19
                         Grid:
                         |A|B|C|
                         |D|E|F|
                         |G|H|I|
                         Across:
                         1. Start.
                         2. Middle.
                         3. End.
                         Down:
                         1. Some Very.
                         2. Dummy.
                         3. Clues.

                         """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void importPuzzle_unknownFormatOption(@TempDir final Path tempDir) throws IOException {
        final Path example = tempDir.resolve("example");
        givenPuzzle(example, """
                             Title: Example Grid
                             Author: Jane Doe
                             Editor: John Doe
                             Date: 2023-06-19


                             ABC
                             DEF
                             GHI


                             A1. Start. ~ ABC
                             A2. Middle. ~ DEF
                             A3. End. ~ GHI

                             D1. Some Very. ~ ADG
                             D2. Dummy. ~ BEH
                             D3. Clues. ~ CFI
                             """);
        whenOneRunsCli("puzzle", "import", "--format", "unknown", example.toString());
        thenCli().doesNotWriteToStdOut()
                 .and().writesToStdErr("No suitable decoder found for format 'unknown'\n")
                 .and().exitsWithCode(APPLICATIVE_ERROR);
    }

    @Test
    void importPuzzle_noSuchFile() {
        whenOneRunsCli("puzzle", "import", "404.xd");
        thenCli().doesNotWriteToStdOut()
                 .and().writesToStdErr("File not found.\n")
                 .and().exitsWithCode(IO_ERROR);
    }

    @Test
    void listEncoders() {
        whenOneRunsCli("puzzle", "list-encoders");
        thenCli().writesToStdOut(
                         """
                         Name            	Description                     	Supported formats
                         ----            	-----------                     	-----------------
                         xd              	xd format encoder               	*.xd           \s
                         """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void exportPuzzle(@TempDir final Path tempDir) {
        final Path exampleXdPath = tempDir.resolve("example.xd");
        final String exampleXd = exampleXdPath.toString();

        givenOneHasRunCli("puzzle", "create",
                          "--title", "Example Grid",
                          "--author", "Me",
                          "--editor", "Myself",
                          "--copyright", "Public Domain",
                          "--date", "2023-07-19",
                          "--rows", "...,ABC,#D.");
        whenOneRunsCli("puzzle", "export", "1", exampleXd);
        thenCli().writesToFile(exampleXdPath, """
                                              Title: Example Grid
                                              Author: Me
                                              Editor: Myself
                                              Copyright: Public Domain
                                              Date: 2023-07-19


                                              ...
                                              ABC
                                              #D.
                                                                   
                                                                   
                                                                   
                                              """)
                 .and().doesNotWriteToStdOut()
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void exportPuzzle_unknownFormatOption(@TempDir final Path tempDir) {
        final Path examplePath = tempDir.resolve("example");
        final String example = examplePath.toString();

        givenOneHasRunCli("puzzle", "create",
                          "--title", "Example Grid",
                          "--author", "Me",
                          "--editor", "Myself",
                          "--copyright", "Public Domain",
                          "--date", "2023-07-19",
                          "--rows", "...,ABC,#D.");
        whenOneRunsCli("puzzle", "export", "--format", "unknown", "1", example);
        thenCli().doesNotWriteToStdOut()
                 .and().writesToStdErr("No suitable encoder found for format 'unknown'\n")
                 .and().exitsWithCode(APPLICATIVE_ERROR);
    }


    @AfterEach
    void cleanRepository() {
        drainErr();
        cli("puzzle", "delete-all");
        assertEquals("", err());
        assertEquals(SUCCESS, exitCode());
    }

}
