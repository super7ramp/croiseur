package com.gitlab.super7ramp.crosswords.spi.dictionary.tools;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.HunspellDictionary;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A tool to deflate a Hunspell dictionary into a big text file containing all valid forms;
 */
public final class HunspellToText {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(HunspellToText.class.getName());

    private static final Predicate<String> ALL_ENTRIES = entry -> true;

    /**
     * Entry point.
     *
     * @param args command arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            LOGGER.severe("Syntax: program_name in.dic out.txt");
            return;
        }

        try {
            final URL dic = Path.of(args[0]).toUri().toURL();
            final HunspellDictionary dictionary = new HunspellDictionary(dic);

            LOGGER.info(() -> "Generating all forms for dictionary " + args[0]);
            final Set<String> words = dictionary.lookup(ALL_ENTRIES);

            LOGGER.info(() -> "Writing " + words.size() + " words to " + args[1]);
            writeFile(words, args[1]);
        } catch (final MalformedURLException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    private static void writeFile(Set<String> words, String path) {
        try (final FileWriter fw = new FileWriter(path)) {
            for (String word : words) {
                fw.write(word);
                fw.write(System.lineSeparator());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to write output file. ", e);
        }
    }
}