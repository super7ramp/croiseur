package com.gitlab.super7ramp.crosswords.dictionary.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Hunspell to internal dictionary format converter.
 */
public final class HunspellToInternal {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(HunspellToInternal.class.getName());

    /**
     * Entry point.
     *
     * @param args arguments
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            LOGGER.severe("Syntax: program_name in.dic out.obj");
            return;
        }

        final String tmpTxtFile = args[0].replace(".dic", ".txt");
        HunspellToText.main(new String[]{args[0], tmpTxtFile});
        TextToInternal.main(new String[]{tmpTxtFile, args[1]});

        Files.delete(Path.of(tmpTxtFile));
    }
}
