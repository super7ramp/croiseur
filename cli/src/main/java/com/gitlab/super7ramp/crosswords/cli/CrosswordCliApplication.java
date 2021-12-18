package com.gitlab.super7ramp.crosswords.cli;

import com.gitlab.super7ramp.crosswords.solver.api.Coordinate;
import picocli.CommandLine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.logging.LogManager;

/**
 * The CLI Application.
 */
final class CrosswordCliApplication {

    /**
     * Constructor.
     */
    CrosswordCliApplication() {
        loadLoggingConfiguration();
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

        final CommandLine command = new CommandLine(new TopLevelCommand());

        command.addSubcommand(new SolveCommand())
               .addSubcommand(new DictionaryCommand())
               .registerConverter(GridSize.class, TypeConverter.wrap(GridSize::valueOf))
               .registerConverter(Locale.class, TypeConverter.wrap(Locale::forLanguageTag))
               .registerConverter(Coordinate.class, TypeConverter.wrap(Coordinate::valueOf));

        return command.execute(args);
    }
}
