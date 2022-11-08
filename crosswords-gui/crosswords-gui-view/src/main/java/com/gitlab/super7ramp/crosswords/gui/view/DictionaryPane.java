package com.gitlab.super7ramp.crosswords.gui.view;

import com.gitlab.super7ramp.crosswords.gui.view.model.DictionaryListViewEntry;
import javafx.beans.binding.Bindings;
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
 * A pane to browse dictionaries and dictionary entries.
 */
public final class DictionaryPane extends VBox {

    /** The fixed list cell height. */
    // TODO get from CSS/actual height.
    private static final int LIST_CELL_HEIGHT = 24;

    /** The dictionaries list view. */
    @FXML
    private ListView<DictionaryListViewEntry> dictionariesListView;

    /** The words list view. */
    @FXML
    private ListView<String> wordsListView;

    /** The search text field. */
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
    }

    @FXML
    private void initialize() {
        dictionariesListView.setFixedCellSize(LIST_CELL_HEIGHT);
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
        dictionariesListView.prefHeightProperty()
                            .bind(Bindings.size(dictionariesListView.getItems())
                                          .multiply(LIST_CELL_HEIGHT));
    }

}
