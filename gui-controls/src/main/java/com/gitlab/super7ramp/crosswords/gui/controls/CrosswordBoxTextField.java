package com.gitlab.super7ramp.crosswords.gui.controls;

import com.gitlab.super7ramp.crosswords.gui.controls.model.CrosswordBox;
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
 *     <li>Binding to {@link CrosswordBox} model</li>
 *     <li>Formatting that limits the input to a single character</li>
 *     <li>A shaded version, which can be toggled using right-click or the space key</li>
 *     <li>Font auto-sizing</li>
 *     <li>A customizable appearance the {@value #CSS_CLASS} CSS class</li>
 * </ul>
 */
public final class CrosswordBoxTextField extends TextField {

    /** CSS class. */
    private static final String CSS_CLASS = "crossword-box-text";

    /** The CSS pseudo-class for shaded state. */
    private static final PseudoClass SHADED = PseudoClass.getPseudoClass("shaded");

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
    private final CrosswordBox model;

    /**
     * Constructs an instance.
     */
    CrosswordBoxTextField() {
        this(new CrosswordBox());
    }

    /**
     * Constructs an instance.
     *
     * @param modelArg the model
     */
    CrosswordBoxTextField(final CrosswordBox modelArg) {
        model = modelArg;

        // Configure style
        getStyleClass().add(CSS_CLASS);
        pseudoClassStateChanged(SHADED, model.shadedProperty().get());

        // Configure text content
        setTextFormatter(new TextFormatter<>(LAST_CHARACTER_TO_UPPER_CASE));
        model.contentProperty().bindBidirectional(textProperty());

        // Enable auto-sized font
        // TODO use a better computation of the font size
        // TODO compute only once for all the boxes since they share the same class (do it in grid?)
        final DoubleBinding desiredFontSize = Bindings.min(1000.0, heightProperty().divide(3.0));
        final StringExpression desiredFontSizeCss = Bindings.concat("-fx-font-size: ",
                desiredFontSize.asString(), "px;");
        styleProperty().bind(desiredFontSizeCss);

        /*
        // Alternative implementation; Should be faster but:
        // * Somehow text is half the size of the cursor
        // * Does not CSS style (node is not styled yet)
        final ObjectBinding<Font> font = Bindings.createObjectBinding(() -> Font.font("Serif",
                desiredFontSize.getValue()), desiredFontSize);
        fontProperty().bind(font);
         */

        // Listen to shaded events
        model.shadedProperty()
             .addListener(e -> pseudoClassStateChanged(SHADED, model.shadedProperty().get()));
        setOnContextMenuRequested(event -> toggleShading());
        addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getText().equals(" ")) {
                toggleShading();
                event.consume();
            }
        });
    }

    /**
     * Toggle shading of the box.
     */
    private void toggleShading() {
        if (!model.shadedProperty().get()) {
            shade();
        } else {
            reveal();
        }
    }

    /**
     * Returns the box model.
     *
     * @return the box model
     */
    public CrosswordBox model() {
        return model;
    }

    /**
     * Shades the box.
     * <p>
     * Note that this method removes the content of the field: The character will not re-appear
     * on a subsequent call to {@link #reveal()}.
     */
    private void shade() {
        clear();
        setEditable(false);
        model.shadedProperty().set(true);
    }

    /**
     * Removes the shading of the box.
     */
    private void reveal() {
        model.shadedProperty().set(false);
        setEditable(true);
    }
}
