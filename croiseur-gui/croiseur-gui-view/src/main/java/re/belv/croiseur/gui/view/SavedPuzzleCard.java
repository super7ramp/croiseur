/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view;

import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import re.belv.croiseur.gui.view.model.SavedPuzzleViewModel;

/** A puzzle identity card. */
public final class SavedPuzzleCard extends HBox {

    /** A preview of the puzzle. */
    @FXML
    private SavedPuzzleGridThumbnail thumbnail;

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

    /** Constructs an instance. */
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
        thumbnail.setGrid(model.grid());
    }

    /** Resets all content of this card. */
    public void reset() {
        Stream.of(title, author, editor, copyright, date).forEach(t -> t.setText(null));
        thumbnail.setGrid(null);
    }
}
