package com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class HunspellDictionaryTest {

    @Test
    @Disabled("filter to fix")
    void lookup() {
        final URL dicFile = HunspellDictionaryTest.class.getResource("/fr.dic");

        final Set<String> actual = new HunspellDictionary(dicFile).lookup(e -> e.length() == 3);

        assertEquals(Collections.emptySet(), actual);
    }

    @Test
    void localeFr() {
        final URL dicFile = HunspellDictionaryTest.class.getResource("/fr.dic");

        final Locale actual = new HunspellDictionary(dicFile).description().locale();

        assertEquals(Locale.FRENCH, actual);
    }
}
