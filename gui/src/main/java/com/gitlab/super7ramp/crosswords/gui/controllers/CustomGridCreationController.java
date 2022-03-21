package com.gitlab.super7ramp.crosswords.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.stream.Stream;

public final class CustomGridCreationController {

    /** Initial width/height. */
    private static final int INITIAL_SIZE = 5;

    /** The grid. */
    @FXML
    private GridPane grid;

    /**
     * Constructor.
     */
    public CustomGridCreationController() {
        // Nothing to do
    }


    private static Node[] createNodeArray(final int length) {
        return Stream.generate(() -> {
                         final TextField textField = new TextField();
                         textField.getStyleClass().add("crossword-box-text");
                         final AnchorPane pane = new AnchorPane();
                         pane.getChildren().add(textField);
                         AnchorPane.setTopAnchor(textField, 0.0);
                         AnchorPane.setRightAnchor(textField, 0.0);
                         AnchorPane.setBottomAnchor(textField, 0.0);
                         AnchorPane.setLeftAnchor(textField, 0.0);
                         return pane;
                     })
                     .limit(length)
                     .toArray(Node[]::new);
    }

    /**
     * Initializes grid: Populates cells.
     */
    @FXML
    void initialize() {
        for (int i = 0; i < INITIAL_SIZE; i++) {
            addRow();
            addColumn();
        }
    }

    @FXML
    void onContextMenuRequested(ContextMenuEvent contextMenuEvent) {
        System.out.println("Right click");
        addRow();
        //addColumn();
    }

    private void addRow() {
        final Node[] cells = createNodeArray(grid.getColumnCount());
        grid.addRow(grid.getRowCount(), cells);
        final RowConstraints constraint = new RowConstraints();
    }

    private void addColumn() {
        final Node[] cells = createNodeArray(grid.getRowCount());
        grid.addColumn(grid.getColumnCount(), cells);
    }
}