/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Random;
import java.util.logging.LogManager;
import picocli.CommandLine;
import picocli.CommandLine.HelpCommand;
import re.belv.croiseur.api.CrosswordService;
import re.belv.croiseur.api.dictionary.DictionaryIdentifier;
import re.belv.croiseur.cli.controller.TopLevelCommand;
import re.belv.croiseur.cli.controller.clue.ClueCommand;
import re.belv.croiseur.cli.controller.dictionary.DictionaryCommand;
import re.belv.croiseur.cli.controller.dictionary.parser.DictionaryIdentifierParser;
import re.belv.croiseur.cli.controller.puzzle.PuzzleCommand;
import re.belv.croiseur.cli.controller.puzzle.parser.Clue;
import re.belv.croiseur.cli.controller.solver.SolverCommand;
import re.belv.croiseur.cli.controller.solver.SolverRunCommand;
import re.belv.croiseur.cli.controller.solver.parser.GridPositionParser;
import re.belv.croiseur.cli.controller.solver.parser.GridSize;
import re.belv.croiseur.cli.controller.solver.parser.PrefilledBox;
import re.belv.croiseur.cli.controller.solver.parser.PrefilledSlot;
import re.belv.croiseur.cli.controller.solver.parser.RandomParser;
import re.belv.croiseur.cli.l10n.ResourceBundles;
import re.belv.croiseur.common.puzzle.GridPosition;

/** The CLI Application. */
public final class CroiseurCliApplication {

    /** The command line interpreter. */
    private final CommandLine command;

    /** Constructor. */
    public CroiseurCliApplication() {
        loadLoggingConfiguration();

        command = new CommandLine(new TopLevelCommand());

        final CrosswordService crosswordService = CrosswordService.create();

        command.addSubcommand(new HelpCommand())
                .addSubcommand(new DictionaryCommand(crosswordService.dictionaryService()))
                .addSubcommand(new CommandLine(new SolverCommand(crosswordService.solverService()))
                        .addSubcommand(new SolverRunCommand(crosswordService.solverService())))
                .addSubcommand(new ClueCommand(crosswordService.clueService()))
                .addSubcommand(new PuzzleCommand(crosswordService.puzzleService()))
                .setResourceBundle(ResourceBundles.messages());

        command.registerConverter(DictionaryIdentifier.class, TypeConverter.wrap(DictionaryIdentifierParser::parse))
                .registerConverter(GridPosition.class, TypeConverter.wrap(GridPositionParser::parse))
                .registerConverter(GridSize.class, TypeConverter.wrap(GridSize::valueOf))
                .registerConverter(Locale.class, TypeConverter.wrap(Locale::forLanguageTag))
                .registerConverter(PrefilledBox.class, TypeConverter.wrap(PrefilledBox::valueOf))
                .registerConverter(PrefilledSlot.class, TypeConverter.wrap(PrefilledSlot::valueOf))
                .registerConverter(Random.class, TypeConverter.wrap(RandomParser::parse))
                .registerConverter(Clue.class, TypeConverter.wrap(Clue::valueOf));
    }

    /** Load the logging configuration. */
    private static void loadLoggingConfiguration() {
        try (final InputStream is =
                CroiseurCliApplication.class.getClassLoader().getResourceAsStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(is);
        } catch (final IOException e) {
            System.err.println("Failed to load logging parameters: " + e.getMessage());
        }
    }

    /**
     * Run the application.
     *
     * @param args arguments
     * @return exit code
     */
    public int run(final String[] args) {
        return command.execute(args);
    }
}
