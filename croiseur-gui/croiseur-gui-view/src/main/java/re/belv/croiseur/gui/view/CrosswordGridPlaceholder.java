/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view;

import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/** A placeholder for the crossword grid. */
public final class CrosswordGridPlaceholder extends VBox {

    /** The wrapping width. */
    private final DoubleProperty wrappingWidth;

    /** The error text. */
    @FXML
    private Text errorText;

    /** The description text. */
    @FXML
    private Text descriptionText;

    /** The advice text. */
    @FXML
    private Text adviceText;

    /** Constructs an instance. */
    public CrosswordGridPlaceholder() {
        wrappingWidth = new SimpleDoubleProperty(this, "wrappingWidth", 0.0);
        FxmlLoaderHelper.load(this, ResourceBundle.getBundle(getClass().getName()));
    }

    /**
     * Returns the wrapping width property.
     *
     * @return the wrapping width property
     */
    public DoubleProperty wrappingWidthProperty() {
        return wrappingWidth;
    }

    /** Initializes the widget. */
    @FXML
    private void initialize() {
        errorText.wrappingWidthProperty().bind(wrappingWidth);
        descriptionText.wrappingWidthProperty().bind(wrappingWidth);
        adviceText.wrappingWidthProperty().bind(wrappingWidth);
    }
}
