/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.common.GridPosition;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordBoxViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import static java.util.function.Predicate.not;

/**
 * A standalone crossword grid control.
 * <p>
 * It is basically a {@link StackPane} encapsulating a {@link GridPane} with some bindings defined
 * with code to constrain the grid pane so that it always presents nice square cells. See
 * {@link #initializeGridConstraints()} to see how these constraints are built.
 */
public final class CrosswordGridPane extends StackPane {

    /**
     * Allows to navigate the grid using arrow keys.
     */
    private final class ArrowKeyNavigator implements EventHandler<KeyEvent> {

        /**
         * Constructs an instance.
         */
        ArrowKeyNavigator() {
            // Nothing to do.
        }

        @Override
        public void handle(final KeyEvent event) {
            final Node source = (Node) event.getSource(); // the GridPane
            final Node focused = source.getScene().getFocusOwner();
            if (event.getCode().isArrowKey()) {
                final int col = GridPane.getColumnIndex(focused);
                final int row = GridPane.getRowIndex(focused);
                final GridPosition currentCoordinate = new GridPosition(col, row);
                final GridPosition nextCoordinate = switch (event.getCode()) {
                    case LEFT -> currentCoordinate.left();
                    case RIGHT -> currentCoordinate.right();
                    case UP -> currentCoordinate.up();
                    case DOWN -> currentCoordinate.down();
                    default -> currentCoordinate;
                };
                final Node nextNode = boxNodes.get(nextCoordinate);
                if (nextNode != null) {
                    nextNode.requestFocus();
                } else {
                    /*
                     * Node may not exist, when trying to go outside the grid (e.g. up on first
                     * row) or if grid is not fully filled (i.e. incomplete row or column). Don't
                     * move focus in this case.
                     */
                }
                event.consume();
            }
        }
    }

    /**
     * A comparator for the grid child boxes.
     * <p>
     * Children are sorted using this comparator in order to maintain a consistent navigation with
     * tab key. Otherwise, navigation follows node insertion order, which may be erratic - nodes can
     * be added in columns using {@link #addColumn()}, in rows using {@link #addRow()} or in a
     * completely custom order using {@link #boxesProperty()}).
     */
    private static final Comparator<Node> BOX_COMPARATOR =
            Comparator.comparingInt(GridPane::getRowIndex)
                      .thenComparingInt(GridPane::getColumnIndex);

    /** The maximum number of rows or columns. */
    private static final int MAX_ROW_COLUMN_COUNT = 20;

    /** The boxes of the view. */
    private final MapProperty<GridPosition, CrosswordBoxViewModel> boxModels;

    /** Box nodes indexed by coordinate. Not a property, just a cache used internally. */
    private final Map<GridPosition, Node> boxNodes;

    /** The position of the focused box. */
    private final ObjectProperty<GridPosition> currentBoxPosition;

    /** The orientation of the current slot, i.e. the slot the current box belongs to. */
    private final BooleanProperty isCurrentSlotVertical;

    /** The grid. */
    @FXML
    private GridPane grid;

    /** The placeholder (displayed when the grid is empty). */
    @FXML
    private CrosswordGridPlaceholder placeholder;

    /**
     * Constructs an instance.
     */
    public CrosswordGridPane() {
        boxModels = new SimpleMapProperty<>(this, "boxModels", FXCollections.observableHashMap());
        boxNodes = new HashMap<>();
        currentBoxPosition = new SimpleObjectProperty<>(this, "currentBoxPosition", null);
        isCurrentSlotVertical = new SimpleBooleanProperty(this, "isCurrentSlotVertical", false);

        final Class<CrosswordGridPane> clazz = CrosswordGridPane.class;
        final String fxmlName = clazz.getSimpleName() + ".fxml";
        final URL location =
                Objects.requireNonNull(clazz.getResource(fxmlName), "Failed to locate " + fxmlName);
        final FXMLLoader fxmlLoader = new FXMLLoader(location);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        // Only to help SceneBuilder find other custom controls shipped in the same jar
        fxmlLoader.setClassLoader(clazz.getClassLoader());
        try {
            fxmlLoader.load();
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    /**
     * Returns an observable map of boxes, i.e. the crossword grid view model.
     * <p>
     * It is meant to be the principal mean of communication with the application view model.
     * <p>
     * <strong>The map contains a given ({@link GridPosition}, {@link CrosswordBoxViewModel})
     * entry if and only if the view contains a {@link CrosswordBoxTextField} at the corresponding
     * grid coordinates with the corresponding content.</strong>
     * <p>
     * Note that a map containing a given {@link GridPosition} key without any value (i.e. a
     * {@code null} value) is equivalent to a map not containing the given coordinates, i.e. in both
     * cases no text field is visible on the view.
     *
     * <h4>Adding new boxes</h4>
     * <p>
     * Just add entries to the map. Entries with default {@link CrosswordBoxViewModel}s can also be
     * added via {@link #addColumn()} and {@link #addRow()}.
     *
     * <h4>Modifying boxes</h4>
     * <p>
     * The straightforward way to modify existing {@link CrosswordBoxViewModel}es is to simply set
     * their properties to new values, either via the view or the application model.
     * <p>
     * Replacing a {@link CrosswordBoxViewModel} object by a new instance will have the same effect
     * though. It is deemed to be the desired behaviour. Implementation may try to avoid
     * instantiating a new {@link CrosswordBoxTextField} in this situation but unlink the old model
     * and reuse the existing text field with the new model object.
     *
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
    public MapProperty<GridPosition, CrosswordBoxViewModel> boxesProperty() {
        return boxModels;
    }

    /**
     * Returns the position of the currently selected box position.
     *
     * @return the position of the currently selected box position
     */
    public ObjectProperty<GridPosition> currentBoxPositionProperty() {
        return currentBoxPosition;
    }

    /**
     * Returns the orientation of the current slot.
     *
     * @return the orientation of the current slot
     */
    public BooleanProperty isCurrentSlotVerticalProperty() {
        return isCurrentSlotVertical;
    }

    /**
     * Creates an empty row at the bottom of the grid.
     */
    public void addRow() {
        final int newRowIndex = getRowCount();
        if (newRowIndex >= MAX_ROW_COLUMN_COUNT) {
            return;
        }
        final int columnCount = getColumnCount();
        for (int column = 0; (column < columnCount) || (column == 0); column++) {
            final GridPosition coordinate = new GridPosition(column, newRowIndex);
            // Just add the box to the model: Model update listener will synchronize the view.
            boxModels.put(coordinate, new CrosswordBoxViewModel());
        }
    }

    /**
     * Creates an empty column at the right of the grid.
     */
    public void addColumn() {
        final int newColumnIndex = getColumnCount();
        if (newColumnIndex >= MAX_ROW_COLUMN_COUNT) {
            return;
        }
        final int rowCount = getRowCount();
        for (int row = 0; (row < rowCount) || (row == 0); row++) {
            final GridPosition coordinate = new GridPosition(newColumnIndex, row);
            // Just add the box to the model: Model update listener will synchronize the view.
            boxModels.put(coordinate, new CrosswordBoxViewModel());
        }
    }

    /**
     * Deletes the last row (reading top to bottom, so the row at the bottom of the grid).
     */
    public void deleteLastRow() {
        final int oldRowCount = getRowCount();
        if (oldRowCount == 0) {
            return;
        }
        final int deletedRowIndex = oldRowCount - 1;
        for (int column = 0; column < getColumnCount(); column++) {
            final GridPosition coordinate = new GridPosition(column, deletedRowIndex);
            // Just remove the box from the model: Model update listener will synchronize the view.
            boxModels.remove(coordinate);
        }
    }

    /**
     * Deletes the last column (reading left to right, so the column on the right of the grid).
     */
    public void deleteLastColumn() {
        final int oldColumnCount = getColumnCount();
        if (oldColumnCount == 0) {
            return;
        }
        final int deletedColumnIndex = oldColumnCount - 1;
        for (int row = 0; row < getRowCount(); row++) {
            final GridPosition coordinate = new GridPosition(deletedColumnIndex, row);
            // Just remove the box from the model: Model update listener will synchronize the view.
            boxModels.remove(coordinate);
        }
    }

    /**
     * Clears the entire grid, including its structure, i.e. box nodes are removed from the grid.
     */
    public void clear() {
        boxModels.clear();
    }

    /**
     * Resets the grid content (both shaded and non-shaded boxes).
     * <p>
     * This method preserves the structure of the grid, box nodes are not removed.
     */
    public void resetContentAll() {
        resetContent(box -> true);
    }

    /**
     * Resets the grid content (only non-shaded boxes).
     * <p>
     * This method preserves the structure of the grid, box nodes are not removed.
     */
    public void resetContentLettersOnly() {
        resetContent(not(CrosswordBoxViewModel::isShaded));
    }

    /**
     * Initializes the widget.
     */
    @FXML
    private void initialize() {
        initializeGridConstraints();
        boxModels.addListener(this::onModelUpdate);
        grid.addEventFilter(KeyEvent.KEY_PRESSED, new ArrowKeyNavigator());
        placeholder.visibleProperty().bind(boxModels.emptyProperty());
        placeholder.managedProperty().bind(boxModels.emptyProperty());
        placeholder.wrappingWidthProperty().bind(grid.widthProperty().subtract(10));
    }

    /**
     * Aligns the view on model update.
     *
     * @param change the model change
     */
    private void onModelUpdate(final MapChangeListener.Change<? extends GridPosition, ?
            extends CrosswordBoxViewModel> change) {
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
    private void onBoxRemoved(final GridPosition removedCoordinate) {
        removeBoxNode(removedCoordinate);

        /*
         * Remove column/row constraint if last box of column/row removed. Note that if a row or
         * a column has been removed in the middle, then the row/column won't be removed from
         * the grid.
         */
        maybeRemoveColumnConstraint(removedCoordinate);
        maybeRemoveRowConstraint(removedCoordinate);

        // Remove all leftover columns/rows if all boxes have been removed
        // FIXME why is that necessary?
        maybeClearRowConstraints();
        maybeClearColumnConstraints();
    }

    /**
     * Removes all leftover rows if all boxes have been removed.
     */
    private void maybeClearRowConstraints() {
        if (boxModels.isEmpty() && !grid.getRowConstraints().isEmpty()) {
            grid.getRowConstraints().clear();
        }
    }

    /**
     * Removes all leftover columns if all boxes have been removed.
     */
    private void maybeClearColumnConstraints() {
        if (boxModels.isEmpty() && !grid.getRowConstraints().isEmpty()) {
            grid.getRowConstraints().clear();
        }
    }

    /**
     * Removes box node at given coordinate.
     *
     * @param coordinate coordinate of the box to remove
     */
    private void removeBoxNode(final GridPosition coordinate) {
        final Node removedNode = boxNodes.remove(coordinate);
        grid.getChildren().remove(removedNode);
    }

    /**
     * Removes a row constraint, if given removed coordinate was the last on the row.
     *
     * @param removedCoordinate coordinate of the removed box
     */
    private void maybeRemoveRowConstraint(final GridPosition removedCoordinate) {
        if (boxNodes.keySet()
                    .stream()
                    .noneMatch(coordinate -> coordinate.y() >= removedCoordinate.y())) {
            grid.getRowConstraints().remove(removedCoordinate.y());
        }
    }

    /**
     * Removes a column constraint, if given removed coordinate was the last on the column.
     *
     * @param removedCoordinate coordinate of the removed box
     */
    private void maybeRemoveColumnConstraint(final GridPosition removedCoordinate) {
        if (boxNodes.keySet()
                    .stream()
                    .noneMatch(coordinate -> coordinate.x() >= removedCoordinate.x())) {
            grid.getColumnConstraints().remove(removedCoordinate.x());
        }
    }

    /**
     * Handles a model update: Box added case.
     *
     * @param coordinate where the box is added
     * @param boxModel   what the box contains
     */
    private void onBoxAdded(final GridPosition coordinate, final CrosswordBoxViewModel boxModel) {
        addBoxNode(coordinate, boxModel);
        maybeAddColumnConstraint(coordinate);
        maybeAddRowConstraint(coordinate);
    }

    /**
     * Adds a new box node to the grid corresponding to the given new box model.
     *
     * @param coordinate where the box is added
     * @param boxModel   what the box contains
     */
    private void addBoxNode(final GridPosition coordinate, final CrosswordBoxViewModel boxModel) {
        final CrosswordBoxTextField node = new CrosswordBoxTextField(boxModel);
        grid.add(node, coordinate.x(), coordinate.y());
        boxNodes.put(coordinate, node);
        // Grid child nodes must be sorted for the navigation with tab key to be consistent
        FXCollections.sort(grid.getChildren(), BOX_COMPARATOR);
        // Add listeners to update the working area
        boxModel.shadedProperty().addListener(
                (observable, wasShaded, isShaded) -> currentBoxPosition.set(
                        isShaded ? null : coordinate));
        node.focusedProperty().addListener((observable, wasFocused, nowFocused) -> {
            if (nowFocused) {
                currentBoxPosition.set(boxModel.isShaded() ? null : coordinate);
            } else {
                // Do nothing, keep last focused box/slot highlighted
            }
        });
        node.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER && currentBoxPosition.get() != null) {
                isCurrentSlotVertical.set(!isCurrentSlotVertical.get());
            }
        });
        node.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 &&
                currentBoxPosition.get() != null) {
                isCurrentSlotVertical.set(!isCurrentSlotVertical.get());
            }
        });
    }

    /**
     * Adds a row constraint for the given coordinate, if first box added of a row.
     *
     * @param coordinate where a box has just been added
     */
    private void maybeAddRowConstraint(final GridPosition coordinate) {
        final int oldRowCount = getRowCount();
        for (int row = oldRowCount; row <= coordinate.y(); row++) {
            addRowConstraint();
        }
    }

    /**
     * Adds a column constraint for the given coordinate, if box added of a column.
     *
     * @param coordinate where a box has just been added
     */
    private void maybeAddColumnConstraint(final GridPosition coordinate) {
        final int oldColumnCount = getColumnCount();
        for (int column = oldColumnCount; column <= coordinate.x(); column++) {
            addColumnConstraint();
        }
    }

    /**
     * Adds a row constraint.
     */
    private void addRowConstraint() {
        final RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setPercentHeight(100);
        grid.getRowConstraints().add(rowConstraint);
    }

    /**
     * Adds a column constraint.
     */
    private void addColumnConstraint() {
        final ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100);
        grid.getColumnConstraints().add(columnConstraints);
    }

    /**
     * Binds grid pane dimensions to this enclosing stack pane dimensions in a way which ensures
     * that grid cells remain visible squares.
     */
    private void initializeGridConstraints() {
        final NumberBinding smallerSideSize = Bindings.min(widthProperty(), heightProperty());
        final DoubleBinding columnPerRowRatio =
                Bindings.createDoubleBinding(this::columnPerRowRatio, grid.getColumnConstraints(),
                                             grid.getRowConstraints());
        grid.maxHeightProperty()
            .bind(Bindings.min(smallerSideSize, smallerSideSize.divide(columnPerRowRatio)));
        grid.maxWidthProperty()
            .bind(Bindings.min(smallerSideSize, smallerSideSize.multiply(columnPerRowRatio)));
    }

    /**
     * Computes the column / row ratio.
     *
     * @return the column / row ratio when row count is > 0; 1.0 when row or column count is 0
     */
    private double columnPerRowRatio() {
        final int columnCount = getColumnCount();
        final int rowCount = getRowCount();
        final double ratio;
        if (columnCount == 0 || rowCount == 0) {
            ratio = 1.0;
        } else {
            ratio = ((double) columnCount / ((double) rowCount));
        }
        return ratio;
    }

    /**
     * Resets the boxes matching the given predicate.
     *
     * @param predicate filters the boxes to be reset
     * @see CrosswordBoxViewModel#reset()
     */
    private void resetContent(final Predicate<CrosswordBoxViewModel> predicate) {
        boxModels.values().stream().filter(predicate).forEach(CrosswordBoxViewModel::reset);
    }

    /**
     * Gets the grid row count.
     *
     * @return the grid row count
     */
    private int getRowCount() {
        /*
         * Avoid grid#getRowCount(): It loops on all nodes to get the max row index. We don't
         * need it since we add constraints for each row (no more, no less). Hence, the number of
         * row constraints is the number of rows.
         */
        return grid.getRowConstraints().size();
    }

    /**
     * Gets the grid column count.
     *
     * @return the grid column count
     */
    private int getColumnCount() {
        /*
         * Avoid grid#getColumnCount(): It loops on all nodes to get the max column index. We don't
         * need it since we add constraints for each column (no more, no less). Hence, the number of
         * column constraints is the number of columns.
         */
        return grid.getColumnConstraints().size();
    }
}
