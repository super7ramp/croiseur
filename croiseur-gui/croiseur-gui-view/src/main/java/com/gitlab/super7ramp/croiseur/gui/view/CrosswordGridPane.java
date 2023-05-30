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

/**
 * A standalone crossword grid control.
 * <p>
 * It is basically a {@link StackPane} encapsulating a {@link GridPane} and a placeholder with some
 * bindings defined with code to constrain the grid pane so that it always presents nice square
 * cells. See {@link #initializeGridConstraints()} to see how these constraints are built.
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
     * tab key. Otherwise, navigation follows node insertion order, which may be erratic.
     */
    private static final Comparator<Node> BOX_COMPARATOR =
            Comparator.comparingInt(GridPane::getRowIndex)
                      .thenComparingInt(GridPane::getColumnIndex);

    /** The boxes of the view. */
    private final MapProperty<GridPosition, CrosswordBoxViewModel> boxModels;

    /** Box nodes indexed by coordinate. Not a property, just a cache used internally. */
    private final Map<GridPosition, Node> boxNodes;

    /** The position of the focused box. */
    private final ObjectProperty<GridPosition> currentBoxPosition;

    /** The orientation of the current slot, i.e. the slot the current box belongs to. */
    private final BooleanProperty currentSlotVertical;

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
        currentSlotVertical = new SimpleBooleanProperty(this, "isCurrentSlotVertical", false);

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
     * <strong>The map contains a given ({@link GridPosition}, {@link CrosswordBoxViewModel})
     * entry if and only if the view contains a {@link CrosswordBoxTextField} at the corresponding
     * grid coordinates with the corresponding content.</strong>
     * <p>
     * Writer is responsible for ensuring data consistency, i.e. ensuring that rows and columns are
     * added or removed incrementally (i.e. no missing row or column) and that inconsistent states
     * (incomplete rows and columns) are only transient. Otherwise, display may be chaotic.
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
    public BooleanProperty currentSlotVerticalProperty() {
        return currentSlotVertical;
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
                throw new UnsupportedOperationException("Replacing box models is not supported");
            }
            onBoxAdded(change.getKey(), change.getValueAdded());
        } else {
            onBoxRemoved(change.getKey());
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
         * a column has been removed in the middle (i.e. a row/column still exists below/at its
         * right), then the row/column won't be removed from the grid. View model writer is
         * responsible to ensure that it doesn't happen.
         */
        maybeRemoveColumnConstraint(removedCoordinate);
        maybeRemoveRowConstraint(removedCoordinate);

        /*
         * Remove leftover row/column constraint if grid is completely cleared (deleting very last
         * row implicitly deletes all columns, and vice-versa).
         */
        maybeClearConstraints();
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
        if (boxNodes.keySet().stream().noneMatch(coord -> coord.y() >= removedCoordinate.y())) {
            grid.getRowConstraints().remove(removedCoordinate.y());
        }
    }

    /**
     * Removes a column constraint, if given removed coordinate was the last on the column.
     *
     * @param removedCoordinate coordinate of the removed box
     */
    private void maybeRemoveColumnConstraint(final GridPosition removedCoordinate) {
        if (boxNodes.keySet().stream().noneMatch(coord -> coord.x() >= removedCoordinate.x())) {
            grid.getColumnConstraints().remove(removedCoordinate.x());
        }
    }

    /**
     * Removes leftover constraints if all boxes have been removed.
     */
    private void maybeClearConstraints() {
        if (boxNodes.isEmpty()) {
            if (!grid.getRowConstraints().isEmpty()) {
                grid.getRowConstraints().clear();
            }
            if (!grid.getColumnConstraints().isEmpty()) {
                grid.getColumnConstraints().clear();
            }
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
        node.focusedProperty().addListener((observable, wasFocused, nowFocused) -> {
            if (nowFocused) {
                currentBoxPosition.set(coordinate);
            } else {
                // Do nothing, keep last focused box/slot highlighted
            }
        });
        node.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                currentSlotVertical.set(!currentSlotVertical.get());
            }
        });
        node.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                currentSlotVertical.set(!currentSlotVertical.get());
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
     * Adds a column constraint for the given coordinate, if first box added of a column.
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

        final DoubleBinding paddingWidth =
                Bindings.createDoubleBinding(() -> getPadding().getLeft() + getPadding().getRight(),
                                             paddingProperty());
        final DoubleBinding paddingHeight =
                Bindings.createDoubleBinding(() -> getPadding().getTop() + getPadding().getBottom(),
                                             paddingProperty());

        final NumberBinding smallerSideContentSize =
                Bindings.min(widthProperty().subtract(paddingWidth),
                             heightProperty().subtract(paddingHeight));

        final DoubleBinding columnPerRowRatio =
                Bindings.createDoubleBinding(this::columnPerRowRatio, grid.getColumnConstraints(),
                                             grid.getRowConstraints());

        grid.maxHeightProperty()
            .bind(Bindings.min(smallerSideContentSize,
                               smallerSideContentSize.divide(columnPerRowRatio)));
        grid.maxWidthProperty()
            .bind(Bindings.min(smallerSideContentSize,
                               smallerSideContentSize.multiply(columnPerRowRatio)));
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
