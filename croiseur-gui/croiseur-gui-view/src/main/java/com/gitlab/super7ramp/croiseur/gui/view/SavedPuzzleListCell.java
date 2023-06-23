/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.stream.Stream;

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
            // TODO thumbnail
            title.setText(item.details().title());
            author.setText(item.details().author());
            editor.setText(item.details().editor());
            copyright.setText(item.details().copyright());
            item.details().date().map(LocalDate::toString).ifPresent(date::setText);
            setGraphic(container);
            setText(null);
        }
    }
}
