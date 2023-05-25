/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.gui.view.model.DictionaryViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.util.SortedByCopyList;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TitledPane;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;
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

    /** The dictionaries list view. */
    @FXML
    private ListView<DictionaryViewModel> dictionariesListView;

    /** The search text field. */
    @FXML
    private TextField searchTextField;

    /** The words list view. */
    @FXML
    private ListView<String> wordsListView;

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

        final Class<DictionariesPane> clazz = DictionariesPane.class;
        final String fxmlName = clazz.getSimpleName() + ".fxml";
        final URL location = Objects.requireNonNull(clazz.getResource(fxmlName),
                                                    "Failed to locate " + fxmlName);
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(clazz.getName());
        final FXMLLoader fxmlLoader = new FXMLLoader(location, resourceBundle);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    @FXML
    private void initialize() {
        initializeTitledPanes();
        initializeDictionariesList();
        initializeSearchBox();
        initializeWordsList();
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
     * Initialises dictionaries list: Sets custom cell factory (adds checkboxes and customises
     * string representation).
     */
    private void initializeDictionariesList() {
        dictionariesListView.setCellFactory(list -> new DictionaryListCell());
        dictionariesListView.setItems(dictionaries);
    }

    /**
     * Initializes search box: Adds a text formatter to search box so that text field contains only
     * upper case characters.
     */
    private void initializeSearchBox() {
        searchTextField.setTextFormatter(new TextFormatter<>(change -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));
    }

    /**
     * Initialises words list: Binds to property, adds filter from search box.
     */
    private void initializeWordsList() {
        // TODO uniq
        // TODO move sort in view model in order not to do a second sort for suggestions
        final ObservableList<String> sortedWords = new SortedByCopyList<>(words,
                                                                          Comparator.naturalOrder());
        final Predicate<String> matchesSearch = word -> word.startsWith(searchTextField.getText());
        final ObservableValue<Predicate<String>> searchPredicate =
                Bindings.createObjectBinding(() -> matchesSearch,
                                             searchTextField.textProperty(),
                                             dictionariesListView.itemsProperty());
        final FilteredList<String> searchedWords = new FilteredList<>(sortedWords);
        searchedWords.predicateProperty().bind(searchPredicate);
        wordsListView.setItems(searchedWords);
    }

    /**
     * Initializes the suggestions list view.
     */
    private void initializeSuggestionsListView() {
        suggestionsListView.setItems(suggestions);
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
}
