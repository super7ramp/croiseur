/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.javafx.scene.control;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.skin.TextAreaSkin;

/**
 * A {@link TextArea} whose {@link #prefRowCountProperty()} follows the computed number of text
 * lines. Height is supposed to follow pref row count.
 *
 * @see <a href="https://github.com/HanSolo/expandabletextarea">Gerrit Grunwald's
 * ExpandableTextArea implementation</a>
 */
public final class ExpandableTextArea extends TextArea {

    /** Height for one line. Meh. */
    private static final double LINE_HEIGHT = 14.0;

    /**
     * Constructs an instance.
     */
    public ExpandableTextArea() {
        final var computedNumberOfLines =
                Bindings.createIntegerBinding(this::computeNumberOfLines, textProperty());
        prefRowCountProperty().bind(computedNumberOfLines);
        disableVerticalScrollbarOnFirstShow();
    }

    /**
     * Computes the current number of text lines.
     *
     * @return the current number of text lines
     */
    private int computeNumberOfLines() {
        final TextAreaSkin textAreaSkin = (TextAreaSkin) getSkin();
        // TODO and what about prompt text?
        final int textLength = getText().length();
        final int numberOfLines;
        if (textLength < 1) {
            numberOfLines = 1;
        } else {
            final Rectangle2D startBounds = textAreaSkin.getCharacterBounds(1);
            final Rectangle2D endBounds = textAreaSkin.getCharacterBounds(textLength);
            final int computedNoOfLines =
                    (int) ((endBounds.getMaxY() - startBounds.getMinY()) / LINE_HEIGHT);
            numberOfLines = Math.max(computedNoOfLines, 1);
        }
        return numberOfLines;
    }

    private void disableVerticalScrollbarOnFirstShow() {
        final InvalidationListener listener = new InvalidationListener() {
            @Override
            public void invalidated(final Observable observable) {
                final ScrollPane scrollPane = (ScrollPane) lookup(".scroll-pane");
                if (scrollPane != null) {
                    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                    observable.removeListener(this);
                }
            }
        };
        layoutBoundsProperty().addListener(listener);
    }
}
