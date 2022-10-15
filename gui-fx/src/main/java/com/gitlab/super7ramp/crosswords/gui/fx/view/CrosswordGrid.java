package com.gitlab.super7ramp.crosswords.gui.fx.view;

import com.gitlab.super7ramp.crosswords.gui.fx.model.CrosswordBox;
import com.gitlab.super7ramp.crosswords.gui.fx.model.IntCoordinate2D;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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
    private final MapProperty<IntCoordinate2D, CrosswordBox> boxModels;

    /** The number of columns. */
    private final IntegerProperty columnCount;

    /** The number of rows. */
    private final IntegerProperty rowCount;

    /**
     * Box nodes indexed by coordinate. Same rationale as {@link #boxModels}. Not a property,
     * only used internally.
     */
    private final Map<IntCoordinate2D, Node> boxNodes;

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

        boxModels = new SimpleMapProperty<>(this, "boxModels", FXCollections.observableHashMap());
        columnCount = new SimpleIntegerProperty(this, "columnCount", 0);
        rowCount = new SimpleIntegerProperty(this, "rowCount", 0);
        boxNodes = new HashMap<>();

        boxModels.addListener(this::onModelUpdate);
        final IntegerBinding columnCountBinding =
                Bindings.createIntegerBinding(() -> grid.getColumnCount(),
                        grid.getColumnConstraints());
        columnCount.bind(columnCountBinding);
        final IntegerBinding rowCountBinding =
                Bindings.createIntegerBinding(() -> grid.getRowCount(),
                        grid.getColumnConstraints());
        rowCount.bind(rowCountBinding);
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
    public MapProperty<IntCoordinate2D, CrosswordBox> boxes() {
        return boxModels;
    }

    /**
     * Returns the column count.
     * <p>
     * The property is read-only, see {@link #addColumn()} ()}, {@link #deleteLastColumn()} and
     * {@link #boxes()} to modify the columns.
     *
     * @return the column count
     */
    public ReadOnlyIntegerProperty columnCount() {
        return columnCount;
    }

    /**
     * Returns the row count.
     * <p>
     * The property is read-only, see {@link #addRow()}, {@link #deleteLastRow()} and
     * {@link #boxes()} to modify the rows.
     *
     * @return the row count
     */
    public ReadOnlyIntegerProperty rowCount() {
        return rowCount;
    }


    /**
     * Creates an empty row at the bottom of the grid.
     */
    public void addRow() {
        final int oldRowCount = grid.getRowCount();
        if (oldRowCount >= MAX_ROW_COLUMN_COUNT) {
            return;
        }
        final int newRowIndex = oldRowCount;
        final int oldColumnCount = grid.getColumnCount();
        for (int column = 0; (column < oldColumnCount) || (column == 0 && oldColumnCount == 0); column++) {
            final IntCoordinate2D coordinate = new IntCoordinate2D(column, newRowIndex);
            // Just add the box to the model: Model update listener will synchronize the view.
            boxModels.put(coordinate, new CrosswordBox());
        }
    }

    /**
     * Creates an empty column at the right of the grid.
     */
    public void addColumn() {
        final int oldColumnCount = grid.getColumnCount();
        if (oldColumnCount >= MAX_ROW_COLUMN_COUNT) {
            return;
        }
        final int newColumnIndex = oldColumnCount;
        final int oldRowCount = grid.getRowCount();
        for (int row = 0; (row < oldRowCount) || (row == 0 && oldRowCount == 0); row++) {
            final IntCoordinate2D coordinate = new IntCoordinate2D(newColumnIndex, row);
            // Just add the box to the model: Model update listener will synchronize the view.
            boxModels.put(coordinate, new CrosswordBox());
        }
    }

    /**
     * Deletes the last row (reading top to bottom, so the row at the bottom of the grid).
     */
    public void deleteLastRow() {
        final int oldRowCount = grid.getRowCount();
        if (oldRowCount == 0) {
            return;
        }
        final int deletedRowIndex = oldRowCount - 1;
        for (int column = 0; column < grid.getColumnCount(); column++) {
            final IntCoordinate2D coordinate = new IntCoordinate2D(column, deletedRowIndex);
            // Just remove the box from the model: Model update listener will synchronize the view.
            boxModels.remove(coordinate);
        }
    }

    /**
     * Deletes the last column (reading left to right, so the column on the right of the grid).
     */
    public void deleteLastColumn() {
        final int oldColumnCount = grid.getColumnCount();
        if (oldColumnCount == 0) {
            return;
        }
        final int deletedColumnIndex = oldColumnCount - 1;
        for (int row = 0; row < grid.getRowCount(); row++) {
            final IntCoordinate2D coordinate = new IntCoordinate2D(deletedColumnIndex, row);
            // Just remove the box from the model: Model update listener will synchronize the view.
            boxModels.remove(coordinate);
        }
    }

    /**
     * Initializes the widget.
     */
    @FXML
    private void initialize() {
        defineGridConstraints();
    }

    /**
     * Aligns the view on model update.
     *
     * @param change the model change
     */
    private void onModelUpdate(final MapChangeListener.Change<? extends IntCoordinate2D, ?
            extends CrosswordBox> change) {
        System.out.println("DEBUG: Received map change " + change);
        if (change.wasAdded()) {
            if (change.getValueRemoved() != null) {
                // TODO replaced case
                throw new UnsupportedOperationException("Not implemented yet");
            } else {
                onBoxAdded(change.getKey(), change.getValueAdded());
            }
        } else if (change.wasRemoved()) {
            onBoxRemoved(change.getKey());
        } else {
            // TODO confirm that
            throw new IllegalStateException("Change must be either an addition or a deletion");
        }
    }

    /**
     * Handles a model update: Box removed case.
     *
     * @param removedCoordinate the removed coordinate
     */
    private void onBoxRemoved(final IntCoordinate2D removedCoordinate) {
        final Node removedNode = boxNodes.remove(removedCoordinate);
        grid.getChildren().remove(removedNode);

        /*
         * Remove column/row constraint if last box of column/row removed. Note that if a row or
         * a column has been removed in the middle, then the row/column won't be removed from
         * the grid.
         */
        if (boxNodes.keySet()
                    .stream()
                    .noneMatch(coordinate -> coordinate.x() >= removedCoordinate.x())) {
            grid.getColumnConstraints().remove(removedCoordinate.x());
        }
        if (boxNodes.keySet()
                    .stream()
                    .noneMatch(coordinate -> coordinate.y() >= removedCoordinate.y())) {
            grid.getRowConstraints().remove(removedCoordinate.y());
        }

        // Remove all leftover columns/rows if all boxes have been removed
        final boolean emptyModel = boxModels.isEmpty();
        if (emptyModel && grid.getRowCount() != 0) {
            grid.getRowConstraints().clear();
        }
        if (emptyModel && grid.getColumnCount() != 0) {
            grid.getColumnConstraints().clear();
        }
    }

    /**
     * Handles a model update: Box added case.
     *
     * @param coordinate where the box is added
     * @param boxModel   what the box contains
     */
    private void onBoxAdded(final IntCoordinate2D coordinate, final CrosswordBox boxModel) {
        final CrosswordBoxTextField textField = new CrosswordBoxTextField(boxModel);
        grid.add(textField, coordinate.x(), coordinate.y());
        boxNodes.put(coordinate, textField);
        int oldColumnCount = grid.getColumnConstraints().size();
        for (int column = oldColumnCount; column <= coordinate.x(); column++) {
            addColumnConstraint();
        }
        int oldRowCount = grid.getRowConstraints().size();
        for (int row = oldRowCount; row <= coordinate.y(); row++) {
            addRowConstraint();
        }
    }

    private void addRowConstraint() {
        final RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.prefHeightProperty().set(CELL_PREF_SIZE);
        rowConstraint.vgrowProperty().set(Priority.ALWAYS);
        grid.getRowConstraints().add(rowConstraint);
    }

    private void addColumnConstraint() {
        final ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.prefWidthProperty().set(CELL_PREF_SIZE);
        columnConstraints.hgrowProperty().set(Priority.ALWAYS);
        grid.getColumnConstraints().add(columnConstraints);
    }

    /**
     * Binds grid pane dimensions to this enclosing stack pane dimensions in a way which ensures
     * that grid cells remain visible squares.
     */
    private void defineGridConstraints() {
        final NumberBinding smallerSideSize = Bindings.min(widthProperty(), heightProperty());
        final DoubleBinding columnPerRowRatio =
                Bindings.createDoubleBinding(() -> ((double) grid.getColumnConstraints().size()) / ((double) grid.getRowConstraints().size()), grid.getColumnConstraints());
        grid.maxHeightProperty()
            .bind(Bindings.min(smallerSideSize, smallerSideSize.divide(columnPerRowRatio)));
        grid.maxWidthProperty()
            .bind(Bindings.min(smallerSideSize, smallerSideSize.multiply(columnPerRowRatio)));
    }
}
