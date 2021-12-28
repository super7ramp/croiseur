package com.gitlab.super7ramp.crosswords.cli;

import com.gitlab.super7ramp.crosswords.dictionary.api.DictionaryLoader;
import com.gitlab.super7ramp.crosswords.dictionary.spi.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.solver.api.Coordinate;
import com.gitlab.super7ramp.crosswords.solver.api.CrosswordSolverLoader;
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

        final DictionaryLoader dictionary =
                new DictionaryLoader(ServiceLoader.load(DictionaryProvider.class));
        final CrosswordSolverLoader solver =
                new CrosswordSolverLoader(ServiceLoader.load(CrosswordSolverProvider.class));

        command.addSubcommand(HelpCommand.class)
               .addSubcommand(new SolveCommand(solver, dictionary))
               .addSubcommand(new DictionaryCommand(dictionary));

        command.registerConverter(GridSize.class, TypeConverter.wrap(GridSize::valueOf))
               .registerConverter(Locale.class, TypeConverter.wrap(Locale::forLanguageTag))
               .registerConverter(Coordinate.class, TypeConverter.wrap(Coordinate::valueOf))
               .registerConverter(PrefilledBox.class, TypeConverter.wrap(PrefilledBox::valueOf))
               .registerConverter(PrefilledSlot.class, TypeConverter.wrap(PrefilledSlot::valueOf));
    }

    /**
     * Load the logging configuration.
     */
    private static void loadLoggingConfiguration() {
        try (final InputStream is = CrosswordCliApplication.class.getClassLoader()
                                                                 .getResourceAsStream("logging" +
                                                                         ".properties")) {
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
