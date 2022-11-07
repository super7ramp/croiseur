package com.gitlab.super7ramp.crosswords.gui.view;

import com.gitlab.super7ramp.crosswords.common.GridPosition;
import com.gitlab.super7ramp.crosswords.gui.view.model.CrosswordBox;
import com.gitlab.super7ramp.crosswords.gui.view.model.DictionaryListViewEntry;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Objects;

/**
 * An entire crossword solver view, based on {@link BorderPane}.
 */
public final class CrosswordSolver extends BorderPane {

    @FXML
    private CrosswordGrid grid;

    @FXML
    private Button solveButton;

    @FXML
    private DictionaryPane dictionaryPane;

    @FXML
    private CrosswordGridEditionToolbar gridEditionToolbar;

    /**
     * Constructs an instance.
     */
    public CrosswordSolver() {
        final String fxmlName = CrosswordSolver.class.getSimpleName() + ".fxml";
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
        // Bind the grid editor buttons to the grid
        gridEditionToolbar.onAddColumnActionProperty().set(event -> grid.addColumn());
        gridEditionToolbar.onAddRowActionProperty().set(event -> grid.addRow());
        gridEditionToolbar.onDeleteColumnActionProperty().set(event -> grid.deleteLastColumn());
        gridEditionToolbar.onDeleteRowActionProperty().set(event -> grid.deleteLastRow());
    }

    /**
     * Returns the solve button action property.
     *
     * @return the solve button action property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onSolveButtonActionProperty() {
        return solveButton.onActionProperty();
    }

    /**
     * Returns the solve button text property.
     *
     * @return the solve button text property
     */
    public StringProperty solveButtonTextProperty() {
        return solveButton.textProperty();
    }

    /**
     * Returns the crossword grid map property.
     *
     * @return he crossword grid map property
     * @see CrosswordGrid#boxes()
     */
    public MapProperty<GridPosition, CrosswordBox> gridBoxesProperty() {
        return grid.boxes();
    }

    /**
     * Sets the dictionaries to display.
     *
     * @param dictionaries the dictionaries to display
     */
    public void setDictionaries(final ObservableList<DictionaryListViewEntry> dictionaries) {
        dictionaryPane.setDictionaries(dictionaries);
    }

    /**
     * Sets the dictionary entries to display.
     *
     * @param dictionaryEntries the dictionary entries to display
     */
    public void setDictionaryEntries(final ObservableList<String> dictionaryEntries) {
        dictionaryPane.setDictionaryEntries(dictionaryEntries);
    }
}
