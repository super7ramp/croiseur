/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

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
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static java.lang.Math.min;

/**
 * A specialized {@link ListCell} for displaying saved puzzles.
 */
public final class SavedPuzzleListCell extends ListCell<SavedPuzzle> {

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
        final double canvasWidth = thumbnail.getFitWidth();
        final double canvasHeight = thumbnail.getFitHeight();
        final int numberOfColumns = grid.width();
        final int numberOfRows = grid.height();
        final double columnPerRowRatio = ((double) numberOfColumns / (double) numberOfRows);
        final double boxWidth = min(canvasWidth, canvasWidth * columnPerRowRatio) / numberOfColumns;
        final double boxHeight = min(canvasHeight, canvasHeight / columnPerRowRatio) / numberOfRows;

        final Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(Font.font("Serif", boxHeight / 2.2 /* empirical value. */));

        for (int column = 0; column < numberOfColumns + 1; column++) {
            gc.strokeLine(boxWidth * column, 0.0, boxWidth * column, boxHeight * numberOfRows);
        }
        for (int row = 0; row < numberOfRows + 1; row++) {
            gc.strokeLine(0.0, boxHeight * row, boxWidth * numberOfColumns, boxHeight * row);
        }

        grid.shaded().forEach(
                position -> gc.fillRect(boxWidth * position.x(), boxHeight * position.y(), boxWidth,
                                        boxHeight));
        grid.filled().forEach((position, letter) -> gc.fillText(String.valueOf(letter),
                                                                boxWidth * position.x() +
                                                                boxWidth / 2,
                                                                boxHeight * position.y() +
                                                                boxHeight / 2));

        // TODO center grid on canvas in case it's not a square
        final WritableImage image = new WritableImage((int) canvasWidth, (int) canvasHeight);
        final SnapshotParameters params = new SnapshotParameters();
        canvas.snapshot(params, image);

        return image;
    }
}
