/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import re.belv.croiseur.gui.view.model.util.MoreFXCollections;
import re.belv.croiseur.gui.view.model.util.ObservableAggregateList;
import re.belv.croiseur.gui.view.model.util.SortedByCopyList;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The dictionary view model.
 */
public final class DictionariesViewModel {

    /** The available dictionaries. */
    private final ListProperty<DictionaryViewModel> dictionaries;

    /** The selected dictionaries. */
    private final ReadOnlyListWrapper<DictionaryViewModel> selectedDictionaries;

    /** The words of the selected dictionaries. */
    private final ReadOnlyListWrapper<String> words;

    /** The regular expression filtering the {@link #suggestions}. */
    private final StringProperty suggestionFilter;

    /** The words of the selected dictionaries matching {@link #suggestionFilter}. */
    private final ReadOnlyListWrapper<String> suggestions;

    /**
     * Associates a selected dictionary with the identifier of its word list inside
     * {@link #backingAggregateWordList}.
     */
    private final Map<DictionaryKey, Integer> dictionaryToWordAggregateIndex;

    /** An {@link ObservableAggregateList} backing {@link #words}. */
    private final ObservableAggregateList<String> backingAggregateWordList;

    /**
     * Constructs an instance.
     */
    public DictionariesViewModel() {
        dictionaries = new SimpleListProperty<>(this, "dictionaries",
                                                FXCollections.observableArrayList(
                                                        entry -> new Observable[]{entry.selectedProperty()}));

        selectedDictionaries = new ReadOnlyListWrapper<>(this, "selectedDictionaries",
                                                         new FilteredList<>(dictionaries,
                                                                            DictionaryViewModel::isSelected));

        backingAggregateWordList = MoreFXCollections.observableAggregateList();

        // TODO uniq
        words = new ReadOnlyListWrapper<>(this, "words",
                                          new SortedByCopyList<>(backingAggregateWordList,
                                                                 Comparator.naturalOrder()));

        suggestionFilter = new SimpleStringProperty(this, "suggestionFilter", "");

        final ObservableValue<Predicate<String>> suggestionPredicate =
                Bindings.createObjectBinding(this::createSuggestionPredicate, suggestionFilter);
        final FilteredList<String> filteredSuggestions = new FilteredList<>(words);
        filteredSuggestions.predicateProperty().bind(suggestionPredicate);
        suggestions = new ReadOnlyListWrapper<>(this, "suggestions", filteredSuggestions);

        dictionaryToWordAggregateIndex = new HashMap<>();
        selectedDictionaries.addListener(this::onSelectedDictionaryChange);
    }

    /**
     * Returns the available dictionaries.
     *
     * @return the available dictionaries
     */
    public ListProperty<DictionaryViewModel> dictionariesProperty() {
        return dictionaries;
    }

    /**
     * Returns the selected dictionaries.
     * <p>
     * Just a filtered view of {@link #dictionariesProperty()}.
     *
     * @return the selected dictionaries
     */
    public ReadOnlyListProperty<DictionaryViewModel> selectedDictionariesProperty() {
        return selectedDictionaries.getReadOnlyProperty();
    }

    /**
     * Returns the words of the selected dictionaries.
     *
     * @return the words of the selected dictionaries
     */
    public ReadOnlyListProperty<String> wordsProperty() {
        return words.getReadOnlyProperty();
    }

    /**
     * Returns the suggestion filter property.
     * <p>
     * Value is a primitive regular expression:
     * <ul>
     *     <li>Wildcard character is represented by a '.'</li>
     *     <li>No cardinality accepted, i.e. for filtering 5-character long words starting with the
     *     letter 'A', don't write "A.{4}" but write "A...."</li>
     * </ul>
     *
     * @return the suggestion filter
     */
    public StringProperty suggestionFilterProperty() {
        return suggestionFilter;
    }

    /**
     * Returns the suggestions of the selected dictionaries, i.e. the words matching the
     * {@link #suggestionFilterProperty()}.
     *
     * @return the suggestions of the selected dictionaries
     */
    public ReadOnlyListProperty<String> suggestionsProperty() {
        return suggestions.getReadOnlyProperty();
    }

    /**
     * Adds words for a selected dictionary.
     * <p>
     * If the dictionary is not selected, given words are ignored.
     *
     * @param key        the dictionary key
     * @param addedWords the words
     */
    public void addWords(final DictionaryKey key, final Collection<String> addedWords) {
        if (selectedDictionaries.stream().anyMatch(dictionary -> dictionary.key().equals(key))) {
            final int aggregateNumber = backingAggregateWordList.aggregateCount();
            backingAggregateWordList.aggregate(addedWords);
            dictionaryToWordAggregateIndex.put(key, aggregateNumber);
        } else {
            /*
             * Dictionary is not selected. It has probably been selected then unselected before the
             * words could be retrieved by the application. Discard the retrieved words which are
             * now irrelevant.
             */
        }
    }

    /**
     * Creates a new predicate when {@link #suggestionFilter} changes.
     *
     * @return a new predicate when {@link #suggestionFilter} changes
     */
    private Predicate<String> createSuggestionPredicate() {
        final String regex = suggestionFilter.get();
        final Predicate<String> predicate;
        if (regex.isEmpty()) {
            // Typically, a shaded box, predicate shall never match
            predicate = word -> false;
        } else {
            /*
             * Predicate is applied on every dictionary word, so for good performances, avoid:
             * - Pattern compilation: Do it only once
             * - Matcher creation: Do it only once too
             * - Matcher reset: Check if word obviously doesn't match first
             */
            final Matcher matcher = Pattern.compile(regex).matcher("");
            predicate = word -> word.length() == regex.length() && matcher.reset(word).matches();
        }
        return predicate;
    }

    /**
     * Processes dictionary selection change.
     * <p>
     * It removes words when dictionary is un-selected.
     *
     * @param change the dictionary selection change
     */
    private void onSelectedDictionaryChange(
            final ListChangeListener.Change<? extends DictionaryViewModel> change) {
        while (change.next()) {
            if (change.wasRemoved()) {
                final List<? extends DictionaryViewModel> removedDictionaries = change.getRemoved();
                for (final DictionaryViewModel removedDictionaryViewModel : removedDictionaries) {
                    final Integer removedAggregateIndex =
                            dictionaryToWordAggregateIndex.remove(removedDictionaryViewModel.key());
                    if (removedAggregateIndex != null) {
                        backingAggregateWordList.disaggregate(removedAggregateIndex);
                        refreshAggregateIndexes(removedAggregateIndex);
                    } else {
                        // No words for this dictionary, nothing to do.
                    }
                }
            }
        }
    }

    /**
     * Refreshes aggregate word list indexes after aggregate list has been modified.
     *
     * @param removedAggregateIndex the aggregate word list index that has been removed
     */
    private void refreshAggregateIndexes(final Integer removedAggregateIndex) {
        for (final Map.Entry<DictionaryKey, Integer> remainingEntry :
                dictionaryToWordAggregateIndex.entrySet()) {
            final Integer remainingAggregateIndex = remainingEntry.getValue();
            if (remainingAggregateIndex > removedAggregateIndex) {
                final int newRemainingAggregateIndex = remainingAggregateIndex - 1;
                remainingEntry.setValue(newRemainingAggregateIndex);
            }
        }
    }
}
