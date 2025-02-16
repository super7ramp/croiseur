/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.parser.aff;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import re.belv.croiseur.dictionary.hunspell.codec.model.aff.Aff;
import re.belv.croiseur.dictionary.hunspell.codec.parser.common.FlagType;
import re.belv.croiseur.dictionary.hunspell.codec.parser.common.ParserException;

/** Tests for {@link AffParser}. */
final class AffParserTest {

    @Test
    void parseSimpleAff() throws URISyntaxException, IOException, ParserException {
        final Path affFile =
                Path.of(AffParserTest.class.getResource("/simple.aff").toURI());
        final Iterator<String> lines = Files.readAllLines(affFile).iterator();

        final Aff parsed = new AffParser().parse(lines);
        Assertions.assertEquals(FlagType.SINGLE_ASCII, parsed.flagType());
    }
}
