/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Hunspell to XML dictionary format converter.
 */
public final class HunspellToXml {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(HunspellToXml.class.getName());

    /**
     * Entry point.
     *
     * @param args arguments
     * @throws IOException if temporary text file cannot be deleted
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            LOGGER.severe("Syntax: program_name in.dic out.xml");
            return;
        }

        final String tmpTxtFile = args[0].replace(".dic", ".txt");
        HunspellToText.main(new String[]{args[0], tmpTxtFile});
        TextToXml.main(new String[]{tmpTxtFile, args[1]});

        Files.delete(Path.of(tmpTxtFile));
    }
}
