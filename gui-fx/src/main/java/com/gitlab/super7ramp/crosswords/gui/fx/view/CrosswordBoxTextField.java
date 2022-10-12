package com.gitlab.super7ramp.crosswords.gui.fx.view;

import com.gitlab.super7ramp.crosswords.gui.fx.model.CrosswordBox;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringExpression;
import javafx.css.PseudoClass;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

/**
 * A crossword box control.
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
        model.shadedProperty()
             .addListener(e -> pseudoClassStateChanged(SHADED, model.shadedProperty().get()));
        setOnContextMenuRequested(event -> toggleShading());

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
        // Alternative implementation; Somehow text is half the size of the cursor
        final ObjectBinding<Font> font = Bindings.createObjectBinding(() -> Font.font("Serif",
                desiredFontSize.getValue()), desiredFontSize);
        fontProperty().bind(font);
         */
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
