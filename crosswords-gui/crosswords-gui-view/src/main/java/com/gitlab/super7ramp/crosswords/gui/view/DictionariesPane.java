package com.gitlab.super7ramp.crosswords.gui.view;

import com.gitlab.super7ramp.crosswords.gui.view.model.DictionaryViewModel;
import com.gitlab.super7ramp.crosswords.gui.view.util.SortedByCopyList;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * A pane to browse dictionaries and dictionary entries.
 */
public final class DictionariesPane extends VBox {

    /** Filters input so that text field contains only upper case characters. */
    private static final UnaryOperator<TextFormatter.Change> TO_UPPER_CASE =
            change -> {
                change.setText(change.getText().toUpperCase());
                return change;
            };

    /** The dictionaries. */
    private final ListProperty<DictionaryViewModel> dictionaries;

    /** The known words. */
    private final ListProperty<String> words;

    /** The dictionaries list view. */
    @FXML
    private ListView<DictionaryViewModel> dictionariesListView;

    /** The words list view. */
    @FXML
    private ListView<String> wordsListView;

    /** The search text field. */
    @FXML
    private TextField searchTextField;

    /**
     * Constructs an instance.
     */
    public DictionariesPane() {
        dictionaries = new SimpleListProperty<>(this, "dictionaries",
                FXCollections.observableArrayList());
        words = new SimpleListProperty<>(this, "words", FXCollections.observableArrayList());

        final String fxmlName = DictionariesPane.class.getSimpleName() + ".fxml";
        final URL location = Objects.requireNonNull(getClass().getResource(fxmlName), "Failed to "
                + "locate " + fxmlName);
        final ResourceBundle resourceBundle =
                ResourceBundle.getBundle(DictionariesPane.class.getName());
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
        // Input text shall be transformed to upper case like dictionary words
        searchTextField.setTextFormatter(new TextFormatter<>(TO_UPPER_CASE));

        // Add custom cell factory (adds checkboxes and customises string representation)
        dictionariesListView.setCellFactory(new DictionaryListCellFactory());
        dictionariesListView.setItems(dictionaries);

        // Filter the displayed dictionary words
        // TODO uniq
        final ObservableList<String> sortedWords = new SortedByCopyList<>(words,
                Comparator.naturalOrder());
        final Predicate<String> matchesSearch = word -> word.startsWith(searchTextField.getText());
        final ObservableValue<Predicate<String>> searchPredicate =
                Bindings.createObjectBinding(() -> matchesSearch,
                        searchTextField.textProperty(), dictionariesListView.itemsProperty());
        final FilteredList<String> searchedWords = new FilteredList<>(sortedWords);
        searchedWords.predicateProperty().bind(searchPredicate);
        wordsListView.setItems(searchedWords);
    }

    /**
     * Returns the words to display.
     * <p>
     * Note that this list contains all the words unfiltered; The words that will be actually
     * displayed will be the given words which contain the {@link #searchTextField searched
     * substring}.
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
}
