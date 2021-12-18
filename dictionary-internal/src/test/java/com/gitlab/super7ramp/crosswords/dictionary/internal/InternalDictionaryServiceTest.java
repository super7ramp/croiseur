package com.gitlab.super7ramp.crosswords.dictionary.internal;

import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Locale;

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
}
