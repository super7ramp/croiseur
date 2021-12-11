package com.gitlab.super7ramp.crosswords.cli;

import com.gitlab.super7ramp.crosswords.solver.api.Coordinate;
import picocli.CommandLine;

import java.util.Locale;

/**
 * The CLI Application.
 */
final class CrosswordCliApplication {

    /**
     * Constructor.
     */
    CrosswordCliApplication() {
        // Nothing to do.
    }

    int run(final String[] args) {

        final CommandLine command = new CommandLine(new TopLevelCommand())
                .addSubcommand(new SolveCommand())
                .addSubcommand(new DictionaryCommand())
                .registerConverter(GridSize.class, TypeConverter.wrap(GridSize::valueOf))
                .registerConverter(Locale.class, TypeConverter.wrap(Locale::forLanguageTag))
                .registerConverter(Coordinate.class, TypeConverter.wrap(Coordinate::valueOf));

        return command.execute(args);
    }
}
