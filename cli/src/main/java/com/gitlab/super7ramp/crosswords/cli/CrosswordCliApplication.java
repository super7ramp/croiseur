package com.gitlab.super7ramp.crosswords.cli;

import com.gitlab.super7ramp.crosswords.cli.core.api.CrosswordService;
import com.gitlab.super7ramp.crosswords.cli.dictionary.DictionaryCommand;
import com.gitlab.super7ramp.crosswords.cli.dictionary.parsed.DictionaryIdentifier;
import com.gitlab.super7ramp.crosswords.cli.publish.TextPublisher;
import com.gitlab.super7ramp.crosswords.cli.solve.SolveCommand;
import com.gitlab.super7ramp.crosswords.cli.solve.parsed.GridSize;
import com.gitlab.super7ramp.crosswords.cli.solve.parsed.PrefilledBox;
import com.gitlab.super7ramp.crosswords.cli.solve.parsed.PrefilledSlot;
import com.gitlab.super7ramp.crosswords.cli.toplevel.TopLevelCommand;
import com.gitlab.super7ramp.crosswords.dictionary.api.DictionaryLoader;
import com.gitlab.super7ramp.crosswords.dictionary.spi.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.solver.api.CrosswordSolverLoader;
import com.gitlab.super7ramp.crosswords.solver.api.GridPosition;
import com.gitlab.super7ramp.crosswords.solver.spi.CrosswordSolverProvider;
import picocli.CommandLine;
import picocli.CommandLine.HelpCommand;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.ServiceLoader;
import java.util.logging.LogManager;

/**
 * The CLI Application.
 */
final class CrosswordCliApplication {

    /** The command line interpreter. */
    private final CommandLine command;

    /**
     * Constructor.
     */
    CrosswordCliApplication() {
        loadLoggingConfiguration();

        command = new CommandLine(new TopLevelCommand());

        final CrosswordService crosswordService = loadCrosswordService();
        command.addSubcommand(HelpCommand.class)
               .addSubcommand(new SolveCommand(crosswordService.solverService()))
               .addSubcommand(new DictionaryCommand(crosswordService.dictionaryService()));

        command.registerConverter(DictionaryIdentifier.class,
                       TypeConverter.wrap(DictionaryIdentifier::valueOf))
               .registerConverter(GridPosition.class, TypeConverter.wrap(GridPosition::valueOf))
               .registerConverter(GridSize.class, TypeConverter.wrap(GridSize::valueOf))
               .registerConverter(Locale.class, TypeConverter.wrap(Locale::forLanguageTag))
               .registerConverter(PrefilledBox.class, TypeConverter.wrap(PrefilledBox::valueOf))
               .registerConverter(PrefilledSlot.class, TypeConverter.wrap(PrefilledSlot::valueOf));
    }

    /**
     * Load the crossword services.
     *
     * @return the crossword services
     */
    private static CrosswordService loadCrosswordService() {
        final DictionaryLoader dictionary =
                new DictionaryLoader(ServiceLoader.load(DictionaryProvider.class));
        final CrosswordSolverLoader solver =
                new CrosswordSolverLoader(ServiceLoader.load(CrosswordSolverProvider.class));
        return CrosswordService.create(solver, dictionary, new TextPublisher());
    }

    /**
     * Load the logging configuration.
     */
    private static void loadLoggingConfiguration() {
        try (final InputStream is = CrosswordCliApplication.class.getClassLoader()
                                                                 .getResourceAsStream("logging" + ".properties")) {
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
    int run(final String[] args) {
        return command.execute(args);
    }
}
