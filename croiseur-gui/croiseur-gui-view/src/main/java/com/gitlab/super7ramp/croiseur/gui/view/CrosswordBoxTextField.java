/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordBoxViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringExpression;
import javafx.css.PseudoClass;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;

import java.util.function.UnaryOperator;

/**
 * A crossword box control.
 * <p>
 * It features:
 * <ul>
 *     <li>Binding to {@link CrosswordBoxViewModel} model</li>
 *     <li>Formatting that limits the input to a single character</li>
 *     <li>A shaded version, which can be toggled using right-click or the space key</li>
 *     <li>Additional selected and unsolvable states</li>
 *     <li>Font auto-sizing</li>
 *     <li>A customizable appearance the {@value #CSS_CLASS} CSS class</li>
 * </ul>
 */
public final class CrosswordBoxTextField extends TextField {

    /** CSS class. */
    private static final String CSS_CLASS = "crossword-box-text";

    /** The CSS pseudo-class for shaded state. */
    private static final PseudoClass SHADED = PseudoClass.getPseudoClass("shaded");

    /** The CSS pseudo-class for unsolvable state. */
    private static final PseudoClass UNSOLVABLE = PseudoClass.getPseudoClass("unsolvable");

    /** The CSS pseudo-class for selected state. */
    private static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");

    /** The key to toggle shading of the box. */
    private static final String SHADE_KEY = " ";

    /** Filters input so that text field contains only the last character typed, in upper case. */
    private static final UnaryOperator<TextFormatter.Change> LAST_CHARACTER_TO_UPPER_CASE =
            change -> {
                final String newText = change.getControlNewText();
                final int newTextLength = newText.length();
                if (newTextLength > 1) {
                    final String lastCharacter = newText.substring(newTextLength - 1,
                                                                   newTextLength);
                    change.setText(lastCharacter);
                    change.setRange(0, 1);
                }
                change.setText(change.getText().toUpperCase());
                return change;
            };

    /** The box model. */
    private final CrosswordBoxViewModel model;

    /**
     * Constructs an instance.
     */
    CrosswordBoxTextField() {
        this(new CrosswordBoxViewModel());
    }

    /**
     * Constructs an instance.
     *
     * @param modelArg the model
     */
    CrosswordBoxTextField(final CrosswordBoxViewModel modelArg) {
        model = modelArg;

        // Configure shaded/unsolvable states
        getStyleClass().add(CSS_CLASS);
        pseudoClassStateChanged(SHADED, model.isShaded());
        pseudoClassStateChanged(UNSOLVABLE, model.isUnsolvable());
        pseudoClassStateChanged(SELECTED, model.isSelected());
        model.shadedProperty()
             .addListener(e -> pseudoClassStateChanged(SHADED, model.isShaded()));
        model.unsolvableProperty()
             .addListener(e -> pseudoClassStateChanged(UNSOLVABLE, model.isUnsolvable()));
        model.selectedProperty()
             .addListener(e -> pseudoClassStateChanged(SELECTED, model.isSelected()));
        editableProperty().bind(model.shadedProperty().not());

        // Configure text content
        setTextFormatter(new TextFormatter<>(LAST_CHARACTER_TO_UPPER_CASE));
        textProperty().bindBidirectional(model.contentProperty());

        // Enable auto-sized font
        // TODO use a better computation of the font size
        // TODO compute only once for all the boxes since they share the same class (do it in grid?)
        final DoubleBinding desiredFontSize = Bindings.min(1000.0, heightProperty().divide(2.2));
        final StringExpression desiredFontSizeCss = Bindings.concat("-fx-font-size: ",
                                                                    desiredFontSize.asString(),
                                                                    "px;");
        styleProperty().bind(desiredFontSizeCss);

        /*
        // Alternative implementation; Should be faster but:
        // * Somehow text is half the size of the cursor
        // * Does not CSS style (node is not styled yet)
        final ObjectBinding<Font> font = Bindings.createObjectBinding(() -> Font.font("Serif",
                desiredFontSize.getValue()), desiredFontSize);
        fontProperty().bind(font);
         */

        // Listen to user inputs
        setOnContextMenuRequested(event -> toggleShading());
        addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (event.getCharacter().equals(SHADE_KEY)) {
                toggleShading();
                event.consume();
            }
        });
    }

    /**
     * Toggle shading of the box.
     */
    private void toggleShading() {
        if (!model.isShaded()) {
            shade();
        } else {
            lighten();
        }
    }

    /**
     * Shades the box.
     * <p>
     * Note that this method removes the content of the field: The character will not re-appear on a
     * subsequent call to {@link #lighten()}.
     */
    private void shade() {
        clear();
        model.solvable();
        model.shade();
    }

    /**
     * Removes the shading of the box.
     */
    private void lighten() {
        model.lighten();
    }
}
