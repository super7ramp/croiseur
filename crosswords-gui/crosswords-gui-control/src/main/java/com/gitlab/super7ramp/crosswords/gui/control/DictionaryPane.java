package com.gitlab.super7ramp.crosswords.gui.control;

import com.gitlab.super7ramp.crosswords.gui.control.model.DictionaryListViewEntry;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Objects;

/**
 * A pane to browse dictionary entries.
 */
public final class DictionaryPane extends VBox {

    @FXML
    private ListView<DictionaryListViewEntry> dictionariesListView;

    @FXML
    private ListView<String> wordsListView;

    @FXML
    private TextField searchTextField;

    /**
     * Constructs an instance.
     */
    public DictionaryPane() {
        final String fxmlName = DictionaryPane.class.getSimpleName() + ".fxml";
        final URL location = Objects.requireNonNull(getClass().getResource(fxmlName), "Failed to "
                + "locate " + fxmlName);
        final FXMLLoader fxmlLoader = new FXMLLoader(location);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }

        // TODO Adjust height of dictionaries list view dynamically
        dictionariesListView.setFixedCellSize(24.0);
        dictionariesListView.setMinHeight(5 * dictionariesListView.getFixedCellSize());
        dictionariesListView.setMaxHeight(5 * dictionariesListView.getFixedCellSize());

        // Add custom cell factory (adds checkboxes and customises string representation)
        dictionariesListView.setCellFactory(new DictionaryListCellFactory());
    }

    /**
     * Sets the dictionary entries to display.
     *
     * @param entries the dictionary entries to display
     */
    public void setDictionaryEntries(final ObservableList<String> entries) {
        wordsListView.setItems(entries);
    }

    /**
     * Sets the dictionaries to display.
     *
     * @param dictionaries the dictionaries to display
     */
    public void setDictionaries(final ObservableList<DictionaryListViewEntry> dictionaries) {
        dictionariesListView.setItems(dictionaries);
    }

}
