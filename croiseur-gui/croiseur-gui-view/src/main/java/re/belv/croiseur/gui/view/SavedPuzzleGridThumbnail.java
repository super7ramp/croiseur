/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view;

import static java.lang.Math.min;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import re.belv.croiseur.gui.view.javafx.scene.canvas.CanvasUtil;
import re.belv.croiseur.gui.view.model.GridCoord;
import re.belv.croiseur.gui.view.model.SavedPuzzleGridViewModel;

/** A canvas displaying a thumbnail of a saved puzzle grid. */
public final class SavedPuzzleGridThumbnail extends Canvas {

    /** Grid image drawer. */
    private static final class GridDrawer {

        /** Font family used for the grid letters. */
        private static final String FONT_FAMILY = "Serif";

        /** Empirical value to divide the box height with to get an acceptable font size. */
        private static final double FONT_SIZE_MAGIC = 2.2;

        /** Where the drawing is made. */
        private final Canvas canvas;

        /** The colors used to draw the grid. */
        private final Color foregroundColor, backgroundColor;

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
         * @param canvas the canvas to draw on
         * @param foregroundColor the foreground color (color of text, lines, shaded boxes)
         * @param backgroundColor the background color
         * @param grid the grid grid
         */
        private GridDrawer(
                final Canvas canvas,
                final Color foregroundColor,
                final Color backgroundColor,
                final SavedPuzzleGridViewModel grid) {
            this.canvas = canvas;
            this.foregroundColor = foregroundColor;
            this.backgroundColor = backgroundColor;

            filledBoxes = grid.filled();
            shadedBoxes = grid.shaded();
            numberOfColumns = grid.columnCount();
            numberOfRows = grid.rowCount();

            // Reserve one pixel for pixel snapping, otherwise last strokes could be outside canvas
            final double exploitableWidth = canvas.getWidth() - CanvasUtil.pixelSize();
            final double exploitableHeight = canvas.getHeight() - CanvasUtil.pixelSize();
            final double columnPerRowRatio = ((double) numberOfColumns / (double) numberOfRows);
            gridWidth = min(exploitableWidth, exploitableWidth * columnPerRowRatio);
            gridHeight = min(exploitableHeight, exploitableHeight / columnPerRowRatio);
            boxWidth = gridWidth / numberOfColumns;
            boxHeight = gridHeight / numberOfRows;
            horizontalOffset = (exploitableWidth - gridWidth) / 2;
            verticalOffset = (exploitableHeight - gridHeight) / 2;
        }

        /**
         * Draws the given grid on the given canvas.
         *
         * @param canvas the canvas to draw on
         * @param foregroundColor the foreground color (color of text, lines, shaded boxes)
         * @param backgroundColor the background color
         * @param grid the grid grid
         */
        static void draw(
                final Canvas canvas,
                final Color foregroundColor,
                final Color backgroundColor,
                final SavedPuzzleGridViewModel grid) {
            new GridDrawer(canvas, foregroundColor, backgroundColor, grid).draw();
        }

        /**
         * Clears the given canvas.
         *
         * @param canvas the canvas to reset
         */
        static void clear(final Canvas canvas) {
            canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }

        /** Draws the grid image on the canvas. */
        private void draw() {
            clear(canvas);
            fillBackground();
            drawColumns();
            drawRows();
            drawShadedBoxes();
            drawFilledBoxes();
        }

        /** Fills the background of the grid with white. */
        private void fillBackground() {
            final GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(backgroundColor);
            gc.fillRect(x(0), y(0), gridWidth, gridHeight);
        }

        /** Draws the columns of the grid. */
        private void drawColumns() {
            final GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setStroke(foregroundColor);
            final double yStart = y(0);
            final double yEnd = y(numberOfRows);
            for (int column = 0; column < numberOfColumns + 1; column++) {
                gc.strokeLine(x(column), yStart, x(column), yEnd);
            }
        }

        /** Draws the rows of the grid. */
        private void drawRows() {
            final GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setStroke(foregroundColor);
            final double xStart = x(0);
            final double xEnd = x(numberOfColumns);
            for (int row = 0; row < numberOfRows + 1; row++) {
                gc.strokeLine(xStart, y(row), xEnd, y(row));
            }
        }

        /** Draws the shaded boxes. Does nothing if no shaded box. */
        private void drawShadedBoxes() {
            final GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(foregroundColor);
            shadedBoxes.forEach(position -> gc.fillRect(x(position.column()), y(position.row()), boxWidth, boxHeight));
        }

        /** Draws the filled boxes. Does nothing if no filled box. */
        private void drawFilledBoxes() {
            final GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(foregroundColor);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.setFont(Font.font(FONT_FAMILY, boxHeight / FONT_SIZE_MAGIC));
            filledBoxes.forEach((position, letter) -> gc.fillText(
                    String.valueOf(letter), x(position.column()) + boxWidth / 2, y(position.row()) + boxHeight / 2));
        }

        /**
         * Returns the horizontal start position (i.e. left border) of the given column on the canvas.
         *
         * @param columnNumber the column number
         * @return the horizontal position of the given column on the canvas
         */
        private double x(final int columnNumber) {
            return CanvasUtil.snapToPixel(boxWidth * columnNumber + horizontalOffset);
        }

        /**
         * Returns the vertical position (i.e. top border) of the given row on the canvas.
         *
         * @param rowNumber the row number
         * @return the vertical position of the given row on the canvas
         */
        private double y(final int rowNumber) {
            return CanvasUtil.snapToPixel(boxHeight * rowNumber + verticalOffset);
        }
    }

    /** Stylable property factory. */
    private static final StyleablePropertyFactory<SavedPuzzleGridThumbnail> FACTORY =
            new StyleablePropertyFactory<>(Canvas.getClassCssMetaData());

    /** CSS meta-data for the thumbnail foreground color. */
    private static final CssMetaData<SavedPuzzleGridThumbnail, Color> FOREGROUND_COLOR_CSS_METADATA =
            FACTORY.createColorCssMetaData("-thumbnail-fg-color", t -> t.foregroundColor, Color.BLACK);

    /** CSS meta-data for the thumbnail background color. */
    private static final CssMetaData<SavedPuzzleGridThumbnail, Color> BACKGROUND_COLOR_CSS_METADATA =
            FACTORY.createColorCssMetaData("-thumbnail-bg-color", t -> t.backgroundColor, Color.WHITE);

    /** The thumbnail foreground and background colors, stylable via CSS. */
    private final StyleableObjectProperty<Color> foregroundColor, backgroundColor;

    /** The grid to display. */
    private final ObjectProperty<SavedPuzzleGridViewModel> grid;

    /** Constructs an instance. */
    public SavedPuzzleGridThumbnail() {
        foregroundColor = new SimpleStyleableObjectProperty<>(FOREGROUND_COLOR_CSS_METADATA, this, "foregroundColor");
        backgroundColor = new SimpleStyleableObjectProperty<>(BACKGROUND_COLOR_CSS_METADATA, this, "backgroundColor");
        grid = new SimpleObjectProperty<>(this, "grid");

        foregroundColor.addListener(this::redraw);
        backgroundColor.addListener(this::redraw);
        grid.addListener(this::redraw);
    }

    /**
     * Redraws the thumbnail when a relevant property is invalidated.
     *
     * @param ignored the ignored observable
     */
    private void redraw(final Observable ignored) {
        final SavedPuzzleGridViewModel currentGrid = grid.get();
        if (currentGrid == null) {
            GridDrawer.clear(this);
        } else {
            GridDrawer.draw(this, foregroundColor.get(), backgroundColor.get(), currentGrid);
        }
    }

    /**
     * Sets the grid to display, or {@code null} to clear the thumbnail.
     *
     * @param grid the grid to display
     */
    public void setGrid(final SavedPuzzleGridViewModel grid) {
        this.grid.set(grid);
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return FACTORY.getCssMetaData();
    }
}
