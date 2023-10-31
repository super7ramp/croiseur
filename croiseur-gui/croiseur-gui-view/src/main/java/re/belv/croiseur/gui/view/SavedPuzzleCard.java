/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view;

import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import re.belv.croiseur.gui.view.model.GridCoord;
import re.belv.croiseur.gui.view.model.SavedPuzzleViewModel;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.Math.min;

/**
 * A puzzle identity card.
 */
public final class SavedPuzzleCard extends HBox {

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

        /** The grid filled boxes. */
        private final Map<GridCoord, Character> filledBoxes;

        /** The grid shaded box positions. */
        private final Set<GridCoord> shadedBoxes;

        /** The grid dimensions. */
        private final int numberOfColumns, numberOfRows;

        /** Grid/box drawing sizes. */
        private final double gridWidth, gridHeight, boxWidth, boxHeight;

        /** Offsets to center the drawing. Relevant when the grid is not a square. */
        private final double verticalOffset, horizontalOffset;

        /**
         * Constructs an instance.
         *
         * @param width  the desired image width
         * @param height the desired image height
         * @param grid   the grid model
         */
        GridDrawer(final double width, final double height, final SavedPuzzleViewModel grid) {
            canvas = new Canvas(width, height);
            filledBoxes = grid.filledBoxes();
            shadedBoxes = grid.shadedBoxes();
            numberOfColumns = grid.columnCount();
            numberOfRows = grid.rowCount();
            final double columnPerRowRatio = ((double) numberOfColumns / (double) numberOfRows);
            gridWidth = min(width, width * columnPerRowRatio);
            gridHeight = min(height, height / columnPerRowRatio);
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
            fillBackground();
            drawColumns();
            drawRows();
            drawShadedBoxes();
            drawFilledBoxes();
            return toImage();
        }

        /** Fills the background of the grid with white. */
        private void fillBackground() {
            final GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.WHITE);
            gc.fillRect(x(0), y(0), gridWidth, gridHeight);
            // Reset fill color to default black for subsequent fills
            gc.setFill(Color.BLACK);
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
                    position -> gc.fillRect(x(position.column()), y(position.row()), boxWidth,
                                            boxHeight));
        }

        /** Draws the filled boxes. Does nothing if no filled box. */
        private void drawFilledBoxes() {
            final GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.setFont(Font.font(FONT_FAMILY, boxHeight / FONT_SIZE_MAGIC));
            filledBoxes.forEach((position, letter) ->
                                        gc.fillText(String.valueOf(letter),
                                                    x(position.column()) + boxWidth / 2,
                                                    y(position.row()) + boxHeight / 2));
        }

        /** Snapshots the canvas into an image. */
        private Image toImage() {
            final var image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            final var params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
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

    /** The date. May be empty. */
    @FXML
    private Text date;

    /**
     * Constructs an instance.
     */
    public SavedPuzzleCard() {
        FxmlLoaderHelper.load(this, ResourceBundle.getBundle(getClass().getName()));
    }

    /**
     * Sets the content of this card
     *
     * @param model the model to display
     */
    public void set(final SavedPuzzleViewModel model) {
        title.setText(model.title());
        author.setText(model.author());
        editor.setText(model.editor());
        copyright.setText(model.copyright());
        date.setText(model.date());
        final Image image = drawGridImage(model);
        thumbnail.setImage(image);
    }

    /**
     * Resets all content of this card.
     */
    public void reset() {
        thumbnail.setImage(null);
        Stream.of(title, author, editor, copyright, date).forEach(t -> t.setText(null));
    }

    /**
     * Draws a grid image.
     *
     * @param grid the grid model
     * @return a grid image
     */
    private Image drawGridImage(final SavedPuzzleViewModel grid) {
        final double imageWidth = thumbnail.getFitWidth();
        final double imageHeight = thumbnail.getFitHeight();
        final GridDrawer gridDrawer = new GridDrawer(imageWidth, imageHeight, grid);
        return gridDrawer.draw();
    }

}
