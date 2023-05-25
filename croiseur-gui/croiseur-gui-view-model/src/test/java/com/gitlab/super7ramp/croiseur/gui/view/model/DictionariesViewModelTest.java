/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryDetails;
import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link DictionariesViewModel}.
 */
final class DictionariesViewModelTest {

    private DictionariesViewModel dictionaries;

    private DictionaryViewModel dictionary;

    /**
     * For each test, creates a fresh {@link #dictionaries} populated with a fresh unselected
     * {@link #dictionary}.
     */
    @BeforeEach
    void beforeEach() {
        dictionaries = new DictionariesViewModel();
        final String provider = "A Dictionary Provider";
        final DictionaryDetails dictionaryDetails = new DictionaryDetails("A Dictionary Name",
                                                                          Locale.ENGLISH,
                                                                          "A dictionary description");
        final ProvidedDictionaryDetails providedDictionaryDetails =
                new ProvidedDictionaryDetails(provider, dictionaryDetails);
        dictionary = new DictionaryViewModel(providedDictionaryDetails);
        dictionaries.dictionariesProperty().add(dictionary);
    }

    /**
     * Verifies that removing dictionary removes the words from the words property.
     */
    @Test
    void unselectDictionary() {
        dictionary.setSelected(true);
        dictionaries.addWords(dictionary.key(), List.of("HELLO", "WORLD"));

        dictionary.setSelected(false);

        assertTrue(dictionaries.selectedDictionariesProperty().isEmpty());
        assertTrue(dictionaries.wordsProperty().isEmpty());
    }

    /**
     * Verifies that unselecting dictionary with no words does not fail.
     */
    @Test
    void unselectDictionary_NoWords() {
        dictionary.setSelected(true);

        dictionary.setSelected(false);

        assertTrue(dictionaries.selectedDictionariesProperty().isEmpty());
        assertTrue(dictionaries.wordsProperty().isEmpty());
    }

    /**
     * Verifies that adding words for a selected dictionary adds them to the words property.
     */
    @Test
    void addWords() {
        dictionary.setSelected(true);
        dictionaries.addWords(dictionary.key(), List.of("HELLO", "WORLD"));
        assertEquals(List.of("HELLO", "WORLD"), dictionaries.wordsProperty());
    }

    /**
     * Verifies that words for unselected dictionaries are not added to the selected dictionaries
     * word list.
     */
    @Test
    void addWords_DiscardWordsForUnselectedDictionary() {
        dictionary.setSelected(false);
        dictionaries.addWords(dictionary.key(), List.of("HELLO", "WORLD"));
        assertTrue(dictionaries.wordsProperty().isEmpty());
    }

    @Test
    void suggestionFilter_none() {
        dictionary.setSelected(true);
        dictionaries.addWords(dictionary.key(), List.of("HELLO", "WORLD"));

        dictionaries.suggestionFilterProperty().set("");

        assertEquals(Collections.emptyList(), dictionaries.suggestionsProperty());
    }

    @Test
    void suggestionFilter_all() {
        dictionary.setSelected(true);
        dictionaries.addWords(dictionary.key(), List.of("HELLO", "WORLD"));

        dictionaries.suggestionFilterProperty().set(".....");

        assertEquals(List.of("HELLO", "WORLD"), dictionaries.suggestionsProperty());
    }

    @Test
    void suggestionFilter_some() {
        dictionary.setSelected(true);
        dictionaries.addWords(dictionary.key(), List.of("HELLO", "WORLD"));

        dictionaries.suggestionFilterProperty().set(".O...");

        assertEquals(List.of("WORLD"), dictionaries.suggestionsProperty());
    }
}
