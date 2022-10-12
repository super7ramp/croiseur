package com.gitlab.super7ramp.crosswords.gui.fx.view;

import com.gitlab.super7ramp.crosswords.gui.fx.model.CrosswordBox;
import com.gitlab.super7ramp.crosswords.gui.fx.model.IntCoordinate2D;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Objects;

/**
 * A standalone crossword grid control.
 * <p>
 * It is basically a {@link StackPane} encapsulating a {@link GridPane} with some bindings
 * defined with code to constrain the grid pane so that it always presents nice square cells. See
 * {@link #defineGridConstraints()} to see how these constraints are built.
 */
public final class CrosswordGrid extends StackPane {

    /** The maximum number of rows or columns. */
    private static final int MAX_ROW_COLUMN_COUNT = 20;

    /** The preferred size for cells. */
    private static final int CELL_PREF_SIZE = 30;

    /**
     * Boxes indexed by coordinate (because GridPane doesn't offer anything good to retrieve a
     * node from a position).
     */
    private final ObservableMap<IntCoordinate2D, CrosswordBox> boxes;

    /** The grid. */
    @FXML
    private GridPane grid;

    /**
     * Constructs an instance.
     */
    public CrosswordGrid() {
        final String fxmlName = CrosswordGrid.class.getSimpleName() + ".fxml";
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
        boxes = FXCollections.observableHashMap();
        boxes.addListener(this::handleUpdateFromModel);
    }

    /**
     * Returns an observable map of boxes, i.e. the crossword grid view model.
     * <p>
     * It is meant to be the principal mean of communication with the application view model.
     * <p>
     * <strong>The map contains a given ({@link IntCoordinate2D}, {@link CrosswordBox}) entry if
     * and only if the view contains a {@link CrosswordBoxTextField} at the corresponding
     * grid coordinates with the corresponding content.</strong>
     * <p>
     * Note that a map containing a given {@link IntCoordinate2D} key without any value (i.e. a
     * {@code null} value) is equivalent to a map not containing the given coordinates, i.e. in
     * both cases no text field is visible on the view.
     * <p>
     * <h4>Adding new boxes</h4>
     * <p>
     * Just add entries to the map. Entries with default {@link CrosswordBox}es can also be added
     * via {@link #addColumn()} and {@link #addRow()}.
     * <p>
     * <h4>Modifying boxes</h4>
     * <p>
     * The straightforward way to modify existing {@link CrosswordBox}es is to simply set their
     * properties to new values, either via the view or the application model.
     * <p>
     * Replacing a {@link CrosswordBox} object by a new instance will have the same effect
     * though. It is deemed to be the desired behaviour. Implementation may try to avoid
     * instantiating a new {@link CrosswordBoxTextField} in this situation but unlink the old
     * model and reuse the existing text field with the new model object.
     * <p>
     * <h4>Deleting boxes</h4>
     * <p>
     * Just remove entries from the map. As mentioned above, only null-ing the values should have
     * the same effect from the view perspective but this practice can only be discouraged as no
     * obvious intent can be associated to it.
     * <p>
     * Boxes can be also removed using {@link #deleteLastRow()} or {@link #deleteLastColumn()}.
     *
     * @return an observable map of boxes, i.e. the crossword grid view model
     */
    public ObservableMap<IntCoordinate2D, CrosswordBox> boxes() {
        return boxes;
    }

    /**
     * Creates an empty row at the bottom of the grid.
     */
    public void addRow() {
        // TODO implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Creates an empty column at the right of the grid.
     */
    public void addColumn() {
        // TODO implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Delete the last column (reading left to right, so the column on the right of the grid).
     */
    public void deleteLastColumn() {
        // TODO implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Delete the last row (reading top to bottom, so the row at the bottom of the grid).
     */
    public void deleteLastRow() {
        // TODO implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Initializes the widget.
     */
    @FXML
    private void initialize() {
        defineGridConstraints();
    }

    private void handleUpdateFromModel(MapChangeListener.Change<? extends IntCoordinate2D, ?
            extends CrosswordBox> change) {
        System.out.println("DEBUG: Received map change " + change);
        final IntCoordinate2D position = change.getKey();
        if (change.wasAdded()) {
            if (change.getValueRemoved() != null) {
                // TODO replaced case
            } else {
                // TODO added case
                final CrosswordBoxTextField textField =
                        new CrosswordBoxTextField(change.getValueAdded());
                grid.add(textField, position.x(), position.y());
                int oldColumnCount = grid.getColumnConstraints().size();
                for (int column = oldColumnCount; column <= position.x(); column++) {
                    final ColumnConstraints columnConstraints = new ColumnConstraints();
                    columnConstraints.prefWidthProperty().set(CELL_PREF_SIZE);
                    columnConstraints.hgrowProperty().set(Priority.ALWAYS);
                    grid.getColumnConstraints().add(columnConstraints);
                }
                int oldRowCount = grid.getRowConstraints().size();
                for (int row = oldRowCount; row <= position.y(); row++) {
                    final RowConstraints rowConstraint = new RowConstraints();
                    rowConstraint.prefHeightProperty().set(CELL_PREF_SIZE);
                    rowConstraint.vgrowProperty().set(Priority.ALWAYS);
                    grid.getRowConstraints().add(rowConstraint);
                }
            }
        } else if (change.wasRemoved()) {
            // TODO removed case
        } else {
            // TODO confirm that
            throw new IllegalStateException("Change must be either an addition or a deletion");
        }
    }

    /**
     * Binds grid pane dimensions to this enclosing stack pane dimensions in a way which ensures
     * that grid cells remain visible squares.
     */
    private void defineGridConstraints() {
        final NumberBinding smallerSideSize = Bindings.min(widthProperty(), heightProperty());
        final DoubleBinding columnPerRowRatio =
                Bindings.createDoubleBinding(() -> ((double) grid.getColumnCount()) / ((double) grid.getRowCount()), grid.getChildren());
        grid.maxHeightProperty()
            .bind(Bindings.min(smallerSideSize, smallerSideSize.divide(columnPerRowRatio)));
        grid.maxWidthProperty()
            .bind(Bindings.min(smallerSideSize, smallerSideSize.multiply(columnPerRowRatio)));
    }
}
