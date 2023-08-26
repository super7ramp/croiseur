/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.dictionary;

import com.gitlab.super7ramp.croiseur.gui.view.javafx.fxml.FxmlLoaderHelper;
import com.gitlab.super7ramp.croiseur.gui.view.model.dictionary.DictionaryViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseButton;

import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A pane to browse dictionaries and dictionary entries.
 */
public final class DictionariesPane extends Accordion {

    /** The dictionaries. */
    private final ListProperty<DictionaryViewModel> dictionaries;

    /** All the words, optionally filtered manually by users. */
    private final ListProperty<String> words;

    /** All the words, filtered by a pattern. */
    private final ListProperty<String> suggestions;

    /** The consumer to call when a suggested word is selected by mouse click. */
    private final ObjectProperty<Consumer<String>> onSuggestionSelected;

    /** The dictionaries list view. */
    @FXML
    private ListView<DictionaryViewModel> dictionariesListView;

    /** The search text field. */
    @FXML
    private TextField searchTextField;

    /** The words list view. */
    @FXML
    private ListView<String> wordsListView;

    /** The suggestions list view. */
    @FXML
    private ListView<String> suggestionsListView;

    /**
     * Constructs an instance.
     */
    public DictionariesPane() {
        dictionaries = new SimpleListProperty<>(this, "dictionaries",
                                                FXCollections.observableArrayList());
        words = new SimpleListProperty<>(this, "words", FXCollections.observableArrayList());
        suggestions =
                new SimpleListProperty<>(this, "suggestions", FXCollections.observableArrayList());
        onSuggestionSelected = new SimpleObjectProperty<>(this, "onSuggestionSelected");
        FxmlLoaderHelper.load(this, ResourceBundle.getBundle(getClass().getName()));
    }

    @FXML
    private void initialize() {
        initializeTitledPanes();
        initializeDictionariesListView();
        initializeSearchTextField();
        initializeWordsListView();
        initializeSuggestionsListView();
    }

    /**
     * Initializes titled panes: Make sure always one titled pane is expanded.
     */
    private void initializeTitledPanes() {
        expandedPaneProperty().addListener((observable, oldValue, newValue) -> {
            final boolean hasExpanded = getPanes().stream().anyMatch(TitledPane::isExpanded);
            if (!hasExpanded && oldValue != null) {
                Platform.runLater(() -> setExpandedPane(oldValue));
            }
        });
        setExpandedPane(getPanes().get(0));
    }

    /**
     * Initializes dictionaries list: Sets custom cell factory (adds checkboxes and customises
     * string representation).
     */
    private void initializeDictionariesListView() {
        dictionariesListView.setCellFactory(list -> new DictionaryListCell());
        dictionariesListView.setItems(dictionaries);
    }

    /**
     * Initializes search box: Adds a text formatter to search box so that text field contains only
     * upper case characters.
     */
    private void initializeSearchTextField() {
        searchTextField.setTextFormatter(new TextFormatter<>(change -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));
    }

    /**
     * Initializes words list: Binds to property, adds filter from search box.
     */
    private void initializeWordsListView() {
        final Predicate<String> matchesSearch = word -> word.startsWith(searchTextField.getText());
        final ObservableValue<Predicate<String>> searchPredicate =
                Bindings.createObjectBinding(() -> matchesSearch, searchTextField.textProperty(),
                                             dictionariesListView.itemsProperty());
        final FilteredList<String> searchedWords = new FilteredList<>(words);
        searchedWords.predicateProperty().bind(searchPredicate);
        wordsListView.setItems(searchedWords);
    }

    /**
     * Initializes the suggestions list view.
     */
    private void initializeSuggestionsListView() {
        suggestionsListView.setItems(suggestions);
        suggestionsListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && onSuggestionSelected.get() != null) {
                final String selectedSuggestion =
                        suggestionsListView.getSelectionModel().getSelectedItem();
                if (selectedSuggestion != null) {
                    onSuggestionSelected.get().accept(selectedSuggestion);
                }
            }
        });
    }

    /**
     * Returns the words to display.
     * <p>
     * Note that this list contains all the words unfiltered; The words that will be actually
     * displayed will be the given words which contain the
     * {@link #searchTextField searched substring}.
     *
     * @return the words
     */
    public ListProperty<String> wordsProperty() {
        return words;
    }

    /**
     * Returns the dictionaries.
     *
     * @return the dictionaries
     */
    public ListProperty<DictionaryViewModel> dictionariesProperty() {
        return dictionaries;
    }

    /**
     * Returns the suggestions.
     *
     * @return the suggestions
     */
    public ListProperty<String> suggestionsProperty() {
        return suggestions;
    }

    /**
     * Returns the "on suggestion selected" property.
     * <p>
     * The consumer will be given the selected suggested word, for every selection.
     *
     * @return the "on suggestion selected" property
     */
    public ObjectProperty<Consumer<String>> onSuggestionSelected() {
        return onSuggestionSelected;
    }

}
