/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.Math.min;

/**
 * A specialized {@link ListCell} for displaying saved puzzles.
 */
public final class SavedPuzzleListCell extends ListCell<SavedPuzzle> {

    /**
     * Grid image drawer.
     */
    private static final class GridDrawer {

        /** Font family used for the grid letters. */
        private static final String FONT_FAMILY = "Serif";

        /** Empirical value to divide the box height with to get an acceptable font size. */
        private static final double FONT_SIZE_MAGIC = 2.2;

        /** Where the drawing is made. */
        private final Canvas canvas;

        /** Grid structure. */
        private final Map<GridPosition, Character> filledBoxes;
        private final Set<GridPosition> shadedBoxes;
        private final int numberOfColumns, numberOfRows;

        /** Box drawing sizes. */
        private final double boxWidth, boxHeight;

        /** Offsets to center the drawing. Relevant when the grid is not a square. */
        private final double verticalOffset, horizontalOffset;

        /**
         * Constructs an instance.
         *
         * @param width  the desired image width
         * @param height the desired image height
         * @param grid   the grid data
         */
        GridDrawer(final double width, final double height, final PuzzleGrid grid) {
            canvas = new Canvas(width, height);
            filledBoxes = grid.filled();
            shadedBoxes = grid.shaded();
            numberOfColumns = grid.width();
            numberOfRows = grid.height();
            final double columnPerRowRatio = ((double) numberOfColumns / (double) numberOfRows);
            final double gridWidth = min(width, width * columnPerRowRatio);
            final double gridHeight = min(height, height / columnPerRowRatio);
            boxWidth = gridWidth / numberOfColumns;
            boxHeight = gridHeight / numberOfRows;
            horizontalOffset = (canvas.getWidth() - gridWidth) / 2;
            verticalOffset = (canvas.getHeight() - gridHeight) / 2;
        }

        /**
         * Creates an image of the grid.
         *
         * @return an image of the grid
         */
        Image draw() {
            drawColumns();
            drawRows();
            drawShadedBoxes();
            drawFilledBoxes();
            return toImage();
        }

        /** Draws the columns of the grid. */
        private void drawColumns() {
            final GraphicsContext gc = canvas.getGraphicsContext2D();
            for (int column = 0; column < numberOfColumns + 1; column++) {
                gc.strokeLine(x(column), y(0), x(column), y(numberOfRows));
            }
        }

        /** Draws the rows of the grid. */
        private void drawRows() {
            final GraphicsContext gc = canvas.getGraphicsContext2D();
            for (int row = 0; row < numberOfRows + 1; row++) {
                gc.strokeLine(x(0), y(row), x(numberOfColumns), y(row));
            }
        }

        /** Draws the shaded boxes. Does nothing if no shaded box. */
        private void drawShadedBoxes() {
            final GraphicsContext gc = canvas.getGraphicsContext2D();
            shadedBoxes.forEach(
                    position -> gc.fillRect(x(position.x()), y(position.y()), boxWidth, boxHeight));
        }

        /** Draws the filled boxes. Does nothing if no filled box. */
        private void drawFilledBoxes() {
            final GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.setFont(Font.font(FONT_FAMILY, boxHeight / FONT_SIZE_MAGIC));
            filledBoxes.forEach((position, letter) ->
                                        gc.fillText(String.valueOf(letter),
                                                    x(position.x()) + boxWidth / 2,
                                                    y(position.y()) + boxHeight / 2));
        }

        /** Snapshots the canvas into an image. */
        private Image toImage() {
            final var image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            final var params = new SnapshotParameters();
            return canvas.snapshot(params, image);
        }

        /**
         * Returns the horizontal start position (i.e. left border) of the given column on the
         * canvas.
         *
         * @param columnNumber the column number
         * @return the horizontal position of the given column on the canvas
         */
        private double x(final int columnNumber) {
            return boxWidth * columnNumber + horizontalOffset;
        }

        /**
         * Returns the vertical position (i.e. top border) of the given row on the canvas.
         *
         * @param rowNumber the row number
         * @return the vertical position of the given row on the canvas
         */
        private double y(final int rowNumber) {
            return boxHeight * rowNumber + verticalOffset;
        }
    }

    /** The root node of puzzle information nodes. */
    @FXML
    private Node container;

    /** A preview of the puzzle. */
    @FXML
    private ImageView thumbnail;

    /** The title. May be empty. */
    @FXML
    private Text title;

    /** The author. May be empty. */
    @FXML
    private Text author;

    /** The editor. May be empty. */
    @FXML
    private Text editor;

    /** The copyright. May be empty. */
    @FXML
    private Text copyright;

    /** The date in YYYY-MM-DD format. May be empty. */
    @FXML
    private Text date;

    /**
     * Constructs an instance.
     */
    public SavedPuzzleListCell() {
        FxmlLoaderHelper.load(this, ResourceBundle.getBundle(getClass().getName()));
    }

    @Override
    protected void updateItem(final SavedPuzzle item, final boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            thumbnail.setImage(null);
            Stream.of(title, author, editor, copyright, date).forEach(t -> t.setText(null));
        } else {
            final Image gridImage = drawGridImage(item.grid());
            thumbnail.setImage(gridImage);
            title.setText(item.details().title());
            author.setText(item.details().author());
            editor.setText(item.details().editor());
            copyright.setText(item.details().copyright());
            item.details().date().map(LocalDate::toString).ifPresent(date::setText);
            setGraphic(container);
            setText(null);
        }
    }

    /**
     * Draws a grid image.
     *
     * @param grid the grid data
     * @return a grid image
     */
    private Image drawGridImage(final PuzzleGrid grid) {
        final double imageWidth = thumbnail.getFitWidth();
        final double imageHeight = thumbnail.getFitHeight();
        final GridDrawer gridDrawer = new GridDrawer(imageWidth, imageHeight, grid);
        return gridDrawer.draw();
    }
}
