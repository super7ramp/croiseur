package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.FlagType;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.ParserException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * Tests for {@link AffParser}.
 */
final class AffParserTest {

    @Test
    void parseSimpleAff() throws URISyntaxException, IOException, ParserException {
        final Path affFile = Path.of(AffParserTest.class.getResource("/simple.aff").toURI());
        final Iterator<String> lines = Files.readAllLines(affFile).iterator();

        final Aff parsed = new AffParser().parse(lines);
        Assertions.assertEquals(FlagType.SINGLE_ASCII, parsed.flagType());
    }

    @Test
    void parseFrAff() throws URISyntaxException, IOException, ParserException {
        final Path affFile = Path.of(AffParserTest.class.getResource("/fr.aff").toURI());
        final Iterator<String> lines = Files.readAllLines(affFile).iterator();

        final Aff parsed = new AffParser().parse(lines);
        Assertions.assertEquals(FlagType.LONG_ASCII, parsed.flagType());
    }

}