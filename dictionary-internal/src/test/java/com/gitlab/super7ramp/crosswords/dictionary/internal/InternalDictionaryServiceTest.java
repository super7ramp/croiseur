package com.gitlab.super7ramp.crosswords.dictionary.internal;

import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class InternalDictionaryServiceTest {

    @Test
    void validSerializedFr() {
        final URL serialized = InternalDictionaryServiceTest.class.getResource("/fr.obj");

        final InternalDictionaryService dictionaryService =
                new InternalDictionaryService(serialized);

        assertEquals(Locale.FRENCH, dictionaryService.locale());
        assertEquals(482_297, dictionaryService.lookup(e -> true).size());
    }

    @Test
    void dn() {
        final URL serialized = InternalDictionaryServiceTest.class.getResource("/fr.obj");

        final InternalDictionaryService dictionaryService =
                new InternalDictionaryService(serialized);

        assertEquals(Locale.FRENCH, dictionaryService.locale());
        final Set<String> words = dictionaryService.lookup(w -> w.endsWith("LATERE"));
        System.out.println(words);
    }

}
