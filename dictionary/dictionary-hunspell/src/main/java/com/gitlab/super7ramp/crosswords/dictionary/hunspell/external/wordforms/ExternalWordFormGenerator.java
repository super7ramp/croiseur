package com.gitlab.super7ramp.crosswords.dictionary.hunspell.external.wordforms;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.util.ProcessStreamer;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * {@link WordFormGenerator} relying on Hunspell applications spawned as external processes.
 */
final class ExternalWordFormGenerator implements WordFormGenerator {

    /**
     * Unmunch command name.
     */
    private static final String UNMUNCH = "unmunch";

    /**
     * The .aff file.
     */
    private final Path affFile;

    /**
     * The .dic file.
     */
    private final Path dicFile;

    /**
     * Constructor.
     *
     * @param anAffFile an .aff file
     * @param aDicFile  a .dic file
     */
    ExternalWordFormGenerator(final Path anAffFile, final Path aDicFile) {
        affFile = anAffFile;
        dicFile = aDicFile;
    }

    /**
     * Execute the given external command and returns its result.
     *
     * @param command the command and its arguments
     * @return the execution result
     */
    private static Stream<String> execute(final String... command) {
        return ProcessStreamer.inputOnly(command).inputStream();
    }

    @Override
    public Stream<String> generate() {
        return unmunch();
    }

    /**
     * Call the "unmunch" command.
     *
     * @return all the valid forms of the stems from dictionary as a {@link Stream}
     */
    private Stream<String> unmunch() {
        return execute(UNMUNCH, dicFile.toString(), affFile.toString());
    }
}
