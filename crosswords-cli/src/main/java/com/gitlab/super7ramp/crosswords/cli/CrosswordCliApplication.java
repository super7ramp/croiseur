/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.cli;

import com.gitlab.super7ramp.crosswords.api.CrosswordService;
import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.crosswords.cli.controller.TopLevelCommand;
import com.gitlab.super7ramp.crosswords.cli.controller.dictionary.DictionaryCommand;
import com.gitlab.super7ramp.crosswords.cli.controller.solver.SolverCommand;
import com.gitlab.super7ramp.crosswords.cli.controller.solver.SolverRunCommand;
import com.gitlab.super7ramp.crosswords.cli.controller.solver.parser.GridPositionParser;
import com.gitlab.super7ramp.crosswords.cli.controller.solver.parser.GridSize;
import com.gitlab.super7ramp.crosswords.cli.controller.solver.parser.PrefilledBox;
import com.gitlab.super7ramp.crosswords.cli.controller.solver.parser.PrefilledSlot;
import com.gitlab.super7ramp.crosswords.cli.l10n.ResourceBundles;
import com.gitlab.super7ramp.crosswords.common.GridPosition;
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

        command = new CommandLine(TopLevelCommand.class);

        final CrosswordService crosswordService = CrosswordService.create();

        command.addSubcommand(HelpCommand.class)
               .addSubcommand(new CommandLine(new SolverCommand(crosswordService.solverService()))
                       .addSubcommand(new SolverRunCommand(crosswordService.solverService())))
               .addSubcommand(new DictionaryCommand(crosswordService.dictionaryService()))
               .setResourceBundle(ResourceBundles.helpMessages());

        command.registerConverter(DictionaryIdentifier.class,
                       TypeConverter.wrap(DictionaryIdentifier::valueOf))
               .registerConverter(GridPosition.class,
                       TypeConverter.wrap(GridPositionParser::parse))
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
