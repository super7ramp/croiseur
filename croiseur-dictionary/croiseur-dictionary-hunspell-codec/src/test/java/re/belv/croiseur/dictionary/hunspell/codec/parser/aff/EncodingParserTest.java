/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.parser.aff;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link EncodingParser}.
 */
final class EncodingParserTest {

    @Test
    void utf8() {
        final String line = "SET UTF-8";
        final Charset charset = EncodingParser.parse(line);
        assertEquals(StandardCharsets.UTF_8, charset);
    }

    @Test
    void iso8859_1() {
        final String line = "SET ISO8859-1";
        final Charset charset = EncodingParser.parse(line);
        assertEquals(StandardCharsets.ISO_8859_1, charset);
    }

    @Test
    void iso8859_1_variant() {
        final String line = "SET ISO-8859-1";
        final Charset charset = EncodingParser.parse(line);
        assertEquals(StandardCharsets.ISO_8859_1, charset);
    }

    @Test
    @Disabled("seems unsupported, to investigate")
    void iso8859_10() {
        final String line = "SET ISO-8859-10";
        EncodingParser.parse(line);
    }

    @Test
    void iso8859_13() {
        final String line = "SET ISO8859-13";
        System.out.println(Charset.availableCharsets());
        final Charset charset = EncodingParser.parse(line);
        assertEquals(Charset.forName("ISO-8859-13"), charset);
    }

    @Test
    void iso8859_15() {
        final String line = "SET ISO8859-15";
        final Charset charset = EncodingParser.parse(line);
        assertEquals(Charset.forName("ISO-8859-15"), charset);
    }

    @Test
    void koi8R() {
        final String line = "SET KOI8-R";
        final Charset charset = EncodingParser.parse(line);
        assertEquals(Charset.forName("KOI8-R"), charset);
    }

    @Test
    void microsoftCp1251() {
        final String line = "SET microsoft-cp1251";
        final Charset charset = EncodingParser.parse(line);
        assertEquals(Charset.forName("cp1251"), charset);
    }

    @Test
    void isciiDevanagari() {
        final String line = "SET ISCII-DEVANAGARI";
        final Charset charset = EncodingParser.parse(line);
        assertEquals(Charset.forName("ISCII"), charset);
    }
}
