package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.grid.VariableIdentifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class WordVariableTest {

    private static final int DEFAULT_UID = 1;

    @Test
    void valueWhenEmpty() {
        final WordVariable variable = new WordVariable(new VariableIdentifier(DEFAULT_UID), 5);
        assertTrue(variable.value().isEmpty());
    }

    @Test
    void valueWhenPartiallyFilled() {
        final WordVariable variable = new WordVariable(new VariableIdentifier(DEFAULT_UID), 5).withPart('a', 3);
        assertTrue(variable.value().isEmpty());
    }

    @Test
    void valueWhenFilled() {
        WordVariable variable = new WordVariable(new VariableIdentifier(DEFAULT_UID), 7).withValue("example");
        assertTrue(variable.value().isPresent());
        assertEquals("example", variable.value().get());
    }
}
