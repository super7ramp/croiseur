package com.gitlab.super7ramp.crosswords.cli;

import com.gitlab.super7ramp.crosswords.api.CrosswordService;
import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.crosswords.cli.controller.dictionary.DictionaryCommand;
import com.gitlab.super7ramp.crosswords.cli.controller.solve.SolveCommand;
import com.gitlab.super7ramp.crosswords.cli.controller.solve.parsed.GridSize;
import com.gitlab.super7ramp.crosswords.cli.controller.solve.parsed.PrefilledBox;
import com.gitlab.super7ramp.crosswords.cli.controller.solve.parsed.PrefilledSlot;
import com.gitlab.super7ramp.crosswords.cli.controller.toplevel.TopLevelCommand;
import com.gitlab.super7ramp.crosswords.spi.solver.GridPosition;
import picocli.CommandLine;
import picocli.CommandLine.HelpCommand;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
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

        final CrosswordService crosswordService = CrosswordService.create();
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
