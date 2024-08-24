/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        final String dictionaryName = "A Dictionary Name";
        final Locale locale = Locale.ENGLISH;
        final String description = "A dictionary description";
        dictionary = new DictionaryViewModel(provider, dictionaryName, locale, description);
        dictionaries.dictionariesProperty().add(dictionary);
    }

    /**
     * Verifies that deselecting dictionary removes the words from the words property.
     */
    @Test
    void deselectDictionary() {
        dictionary.select();
        dictionaries.addWords(dictionary.key(), List.of("HELLO", "WORLD"));

        dictionary.deselect();

        assertTrue(dictionaries.selectedDictionariesProperty().isEmpty());
        assertTrue(dictionaries.wordsProperty().isEmpty());
    }

    /**
     * Verifies that deselecting dictionary with no words does not fail.
     */
    @Test
    void deselectDictionary_noWords() {
        dictionary.select();

        dictionary.deselect();

        assertTrue(dictionaries.selectedDictionariesProperty().isEmpty());
        assertTrue(dictionaries.wordsProperty().isEmpty());
    }

    /**
     * Verifies that adding words for a selected dictionary adds them to the words property.
     */
    @Test
    void addWords() {
        dictionary.select();
        dictionaries.addWords(dictionary.key(), List.of("HELLO", "WORLD"));
        assertEquals(List.of("HELLO", "WORLD"), dictionaries.wordsProperty());
    }

    /**
     * Verifies that added dictionary words are sorted.
     */
    @Test
    void addWords_sorted() {
        dictionary.select();
        dictionaries.addWords(dictionary.key(), List.of("WORLD", "HELLO"));
        assertEquals(List.of("HELLO", "WORLD"), dictionaries.wordsProperty());
    }

    /**
     * Verifies that words for unselected dictionaries are not added to the selected dictionaries
     * word list.
     */
    @Test
    void addWords_discardWordsForUnselectedDictionary() {
        dictionary.deselect();
        dictionaries.addWords(dictionary.key(), List.of("HELLO", "WORLD"));
        assertTrue(dictionaries.wordsProperty().isEmpty());
    }

    @Test
    void suggestionFilter_none() {
        dictionary.select();
        dictionaries.addWords(dictionary.key(), List.of("HELLO", "WORLD"));

        dictionaries.suggestionFilterProperty().set("");

        assertEquals(Collections.emptyList(), dictionaries.suggestionsProperty());
    }

    @Test
    void suggestionFilter_all() {
        dictionary.select();
        dictionaries.addWords(dictionary.key(), List.of("HELLO", "WORLD"));

        dictionaries.suggestionFilterProperty().set(".....");

        assertEquals(List.of("HELLO", "WORLD"), dictionaries.suggestionsProperty());
    }

    @Test
    void suggestionFilter_some() {
        dictionary.select();
        dictionaries.addWords(dictionary.key(), List.of("HELLO", "WORLD"));

        dictionaries.suggestionFilterProperty().set(".O...");

        assertEquals(List.of("WORLD"), dictionaries.suggestionsProperty());
    }
}
