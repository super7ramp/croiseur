/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.javafx.scene.control;

import java.util.Set;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

/**
 * A {@link TextArea} which auto-resizes its height according to its text content.
 *
 * @see <a href="https://stackoverflow.com/a/72007076">Sai Dandem's original implementation</a>,
 * which is the base for this implementation
 * @see <a href="https://github.com/HanSolo/expandabletextarea">Gerrit Grunwald's alternative
 * implementation</a>: A different approach, not used here but good to know.
 */
public final class ExpandableTextArea extends TextArea {

    /** Cached insets. */
    private Double insets;

    /** Cached text nodes. */
    private Set<Node> textNodes;

    /**
     * Constructs an instance.
     */
    public ExpandableTextArea() {
        // Nothing to do.
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        final double computedHeight = recomputeHeight();
        if (currentHeightDiffersFrom(computedHeight)) {
            setMinMaxPrefHeightsTo(computedHeight);
            Platform.runLater(this::requestLayout);
        }
    }

    /**
     * Computes the total height considering all the required insets.
     *
     * @return the computed height
     */
    private double recomputeHeight() {
        return insets() + text().getLayoutBounds().getHeight();
    }

    /**
     * Retrieves all insets. Do it only once, assuming skin never changes.
     *
     * @return the insets
     */
    private double insets() {
        if (insets == null) {
            final Region scrollPane = (Region) lookup(".scroll-pane");
            final Region content = (Region) lookup(".content");
            final double textAreaInsets = getInsets().getTop() + getInsets().getBottom();
            final double scrollInsets =
                    scrollPane.getInsets().getTop() + scrollPane.getInsets().getBottom();
            final double contentInsets =
                    content.getInsets().getTop() + content.getInsets().getBottom();
            insets = textAreaInsets + scrollInsets + contentInsets;
        }
        return insets;
    }

    /**
     * Retrieves the visible text node.
     *
     * @return the visible text node
     */
    private Node text() {
        if (textNodes == null) {
            textNodes = lookupAll(".text");
        }
        // There are two text nodes: The main one, and one for the prompt. Select the one visible.
        return textNodes.stream().filter(Node::isVisible).findFirst().orElseThrow();
    }

    /**
     * Whether current height differs from given height.
     *
     * @param newHeight the new height
     * @return {@code true} if the two heigts differ
     */
    private boolean currentHeightDiffersFrom(final double newHeight) {
        return Math.abs(getHeight() - newHeight) > 10E-1;
    }

    /**
     * Sets the min, max and pref heights to given value.
     *
     * @param height the value to set
     */
    private void setMinMaxPrefHeightsTo(final double height) {
        setMinHeight(height);
        setMaxHeight(height);
        setPrefHeight(height);
    }
}
