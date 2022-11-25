package com.gitlab.super7ramp.crosswords.gui.view;

import com.gitlab.super7ramp.crosswords.common.GridPosition;
import com.gitlab.super7ramp.crosswords.gui.view.model.CrosswordBoxViewModel;
import com.gitlab.super7ramp.crosswords.gui.view.model.DictionaryViewModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Objects;

/**
 * An entire crossword solver view, based on {@link BorderPane}.
 */
public final class CrosswordSolverPane extends BorderPane {

    /** The grid. */
    @FXML
    private CrosswordGridPane grid;

    /** The dictionary pane. */
    @FXML
    private DictionariesPane dictionariesPane;

    /** The toolbar. */
    @FXML
    private CrosswordEditionToolbar toolbar;

    /**
     * Constructs an instance.
     */
    public CrosswordSolverPane() {
        final String fxmlName = CrosswordSolverPane.class.getSimpleName() + ".fxml";
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
        toolbar.onAddColumnActionButtonProperty().set(event -> grid.addColumn());
        toolbar.onAddRowActionButtonProperty().set(event -> grid.addRow());
        toolbar.onDeleteColumnActionButtonProperty().set(event -> grid.deleteLastColumn());
        toolbar.onDeleteRowActionButtonProperty().set(event -> grid.deleteLastRow());

        // Display the dictionary pane only when the dictionaries toggle button is selected
        final BooleanProperty dictionariesToggleButtonSelectedProperty =
                toolbar.dictionariesToggleButtonSelectedProperty();
        dictionariesPane.visibleProperty().bind(dictionariesToggleButtonSelectedProperty);
        dictionariesPane.managedProperty().bind(dictionariesToggleButtonSelectedProperty);
    }

    /**
     * Returns the solve button action property.
     *
     * @return the solve button action property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onSolveButtonActionProperty() {
        return toolbar.onSolveButtonActionProperty();
    }

    /**
     * Returns the grid edition controls disable property.
     * <p>
     * The controls are the 'add column', 'delete column', 'add row','delete row' and the crossword
     * grid pane itself.
     *
     * @return the grid edition controls disable property
     */
    public BooleanProperty gridEditionDisableProperty() {
        return toolbar.gridEditionButtonsDisableProperty();
    }

    /**
     * Returns the solve button disable property.
     *
     * @return the solve button disable property
     */
    public BooleanProperty solveButtonDisableProperty() {
        return toolbar.solveButtonDisableProperty();
    }

    /**
     * Returns the solve button text property.
     *
     * @return the solve button text property
     */
    public StringProperty solveButtonTextProperty() {
        return toolbar.solveButtonTextProperty();
    }

    /**
     * Returns the crossword grid map property.
     *
     * @return the crossword grid map property
     * @see CrosswordGridPane#boxes()
     */
    public MapProperty<GridPosition, CrosswordBoxViewModel> gridBoxesProperty() {
        return grid.boxes();
    }

    /**
     * Returns the displayed dictionaries.
     *
     * @return the displayed dictionaries
     */
    public ObservableList<DictionaryViewModel> dictionaries() {
        return dictionariesPane.dictionaries();
    }

    /**
     * Returns the displayed dictionary words.
     *
     * @return the displayed dictionary words
     */
    public ListProperty<String> wordsProperty() {
        return dictionariesPane.wordsProperty();
    }
}
