package com.gitlab.super7ramp.crosswords.gui.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

import java.util.stream.Stream;

public final class CustomGridCreationController {

    /** The maximum number of rows or columns. */
    private static final int MAX_ROW_COLUMN_COUNT = 20;

    /** The stack enclosing the different elements (grid, add/remove buttons). */
    @FXML
    private StackPane stack;

    /** The grid. */
    @FXML
    private GridPane grid;

    /**
     * Constructor.
     */
    public CustomGridCreationController() {
        // Nothing to do.
    }

    /**
     * Creates an array of cells.
     *
     * @return an array of cells
     */
    private static Node[] createCellArray(final int length) {
        return Stream.generate(CustomGridCreationController::createCell)
                     .limit(length)
                     .toArray(Node[]::new);
    }

    /**
     * Creates a cell.
     *
     * @return a cell
     */
    private static TextField createCell() {

        final TextField textField = new TextField();
        textField.getStyleClass().add("crossword-box-text");
        textField.setOnContextMenuRequested(event -> {
            if (textField.isEditable()) {
                textField.clear();
                textField.getStyleClass().add("crossword-box-text-shaded");
                textField.setEditable(false);
            } else {
                textField.getStyleClass().remove("crossword-box-text-shaded");
                textField.setEditable(true);
            }
        });

        return textField;
    }

    /**
     * Initializes grid: Populates empty grid pane with cells.
     */
    @FXML
    void initialize() {

        // Populate the empty grid with cells
        for (int columnIndex = 0; columnIndex < grid.getColumnCount(); columnIndex++) {
            for (int rowIndex = 0; rowIndex < grid.getRowCount(); rowIndex++) {
                final TextField cell = createCell();
                grid.add(cell, columnIndex, rowIndex);
            }
        }

        // Binding black magic so that grid cells remain visible squares
        final NumberBinding stackSmallerSize = Bindings.min(stack.widthProperty(),
                stack.heightProperty());
        final DoubleBinding columnPerRowRatio =
                Bindings.createDoubleBinding(() -> ((double) grid.getColumnCount()) / ((double) grid.getRowCount()), grid.getChildren());
        grid.maxHeightProperty()
            .bind(Bindings.min(stackSmallerSize, stackSmallerSize.divide(columnPerRowRatio)));
        grid.maxWidthProperty()
            .bind(Bindings.min(stackSmallerSize, stackSmallerSize.multiply(columnPerRowRatio)));
    }

    @FXML
    void onContextMenuRequested(ContextMenuEvent contextMenuEvent) {
        System.out.println("Right click");
        addRow();
        addColumn();
    }

    /**
     * Adds a row to the grid.
     */
    private void addRow() {
        final int columnCount = grid.getColumnCount();
        if (columnCount > MAX_ROW_COLUMN_COUNT) {
            return;
        }
        final Node[] cells = createCellArray(columnCount);
        grid.addRow(grid.getRowCount(), cells);
        final RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.minHeightProperty().set(1);
        rowConstraint.prefHeightProperty().set(30);
        rowConstraint.vgrowProperty().set(Priority.SOMETIMES);
        grid.getRowConstraints().add(rowConstraint);
    }

    /**
     * Adds a column to the grid.
     */
    private void addColumn() {
        final int rowCount = grid.getRowCount();
        if (rowCount >= MAX_ROW_COLUMN_COUNT) {
            return;
        }
        final Node[] cells = createCellArray(rowCount);
        grid.addColumn(grid.getColumnCount(), cells);
        final ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.minWidthProperty().set(10);
        columnConstraints.prefWidthProperty().set(30);
        columnConstraints.hgrowProperty().set(Priority.SOMETIMES);
        grid.getColumnConstraints().add(columnConstraints);
    }

}