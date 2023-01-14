/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.gui.view.model;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryKey;
import com.gitlab.super7ramp.crosswords.gui.view.model.util.MoreFXCollections;
import com.gitlab.super7ramp.crosswords.gui.view.model.util.ObservableAggregateList;
import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The dictionary view model.
 */
public final class DictionariesViewModel {

    /** The available dictionaries. */
    private final ListProperty<DictionaryViewModel> dictionaries;

    /** The selected dictionaries. */
    private final ListProperty<DictionaryViewModel> selectedDictionaries;

    /** The words of the selected dictionaries. */
    private final ListProperty<String> selectedDictionariesWords;

    /**
     * Associates a selected dictionary with the identifier of its word list inside
     * {@link #backingAggregateWordList}.
     */
    private final Map<DictionaryKey, Integer> dictionaryToWordAggregateIndex;

    /** An {@link ObservableAggregateList} backing {@link #selectedDictionariesWords}. */
    private final ObservableAggregateList<String> backingAggregateWordList;

    /**
     * Constructs an instance.
     */
    public DictionariesViewModel() {
        dictionaries = new SimpleListProperty<>(this, "dictionaries",
                FXCollections.observableArrayList(entry -> new Observable[]{entry.selectedProperty()}));
        selectedDictionaries = new SimpleListProperty<>(this, "selected dictionaries",
                new FilteredList<>(dictionaries, DictionaryViewModel::isSelected));
        backingAggregateWordList = MoreFXCollections.observableAggregateList();
        selectedDictionariesWords = new SimpleListProperty<>(this, "selected dictionary entries",
                backingAggregateWordList);
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
    public ListProperty<DictionaryViewModel> selectedDictionariesProperty() {
        return selectedDictionaries;
    }

    /**
     * Returns the words of the selected dictionaries.
     *
     * @return the words of the selected dictionaries
     */
    public ReadOnlyListProperty<String> wordsProperty() {
        return selectedDictionariesWords;
    }

    /**
     * Adds words for a dictionary.
     *
     * @param key   the dictionary key
     * @param words the words
     */
    public void addWords(final DictionaryKey key, final List<String> words) {
        final int aggregateNumber = backingAggregateWordList.aggregateCount();
        backingAggregateWordList.aggregate(words);
        dictionaryToWordAggregateIndex.put(key, aggregateNumber);
    }

    /**
     * Processes dictionary selection change.
     * <p>
     * It removes words when dictionary is un-selected.
     *
     * @param change the dictionary selection change
     */
    private void onSelectedDictionaryChange(ListChangeListener.Change<?
            extends DictionaryViewModel> change) {
        while (change.next()) {
            if (change.wasRemoved()) {
                final List<? extends DictionaryViewModel> removedDictionaries = change.getRemoved();
                for (final DictionaryViewModel removedDictionaryViewModel : removedDictionaries) {
                    final Integer removedAggregateIndex =
                            dictionaryToWordAggregateIndex.remove(removedDictionaryViewModel.key());
                    backingAggregateWordList.disaggregate(removedAggregateIndex);
                    refreshAggregateIndexes(removedAggregateIndex);
                }
            }
        }
    }

    /**
     * Refresh aggregate word list indexes after aggregate list has been modified.
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
