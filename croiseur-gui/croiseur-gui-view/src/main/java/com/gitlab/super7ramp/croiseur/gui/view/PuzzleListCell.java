/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;

/**
 * A specialized {@link ListCell} for displaying puzzles.
 */
public final class PuzzleListCell extends ListCell<PuzzleViewModel> {

    /** The root node of puzzle information nodes. */
    @FXML
    private PuzzleCardPane card;

    /**
     * Constructs an instance.
     */
    public PuzzleListCell() {
        FxmlLoaderHelper.load(this);
    }

    @Override
    protected void updateItem(final PuzzleViewModel item, final boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            card.reset();
        } else {
            card.grid(item.grid());
            card.title(item.title());
            card.author(item.author());
            card.editor(item.editor());
            card.copyright(item.copyright());
            card.date(item.date());
            setGraphic(card);
            setText(null);
        }
    }

}
