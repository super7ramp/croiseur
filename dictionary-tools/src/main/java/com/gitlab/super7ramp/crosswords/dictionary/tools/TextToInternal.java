package com.gitlab.super7ramp.crosswords.dictionary.tools;

import com.gitlab.super7ramp.crosswords.dictionary.internal.InternalDictionary;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Just build an {@link InternalDictionary} from a text file and serialize the object in another
 * file.
 */
public final class TextToInternal {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(TextToInternal.class.getName());

    /**
     * Entry point.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            LOGGER.severe("Syntax: program_name in.txt out.obj");
            return;
        }

        final String output = args[1];
        final Path inputPath = Path.of(args[0]);
        final Locale locale = localeOf(inputPath);
        LOGGER.info("Locale is: " + locale.toLanguageTag());

        try (final ObjectOutputStream serializer =
                     new ObjectOutputStream(new FileOutputStream(output))) {

            final List<String> entries = Files.readAllLines(inputPath);
            final InternalDictionary dictionary = new InternalDictionary(locale, entries);

            serializer.writeObject(dictionary);
        } catch (final IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    private static Locale localeOf(final Path inputPath) {
        final String fileName = inputPath.getFileName().toString();
        return Locale.forLanguageTag(fileName.replace(".txt", "").replace("_", "-"));
    }
}
