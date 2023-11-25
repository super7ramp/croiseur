/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.javafx.scene.canvas;

import static java.lang.Math.rint;

/**
 * Utility methods related to drawing on canvas.
 */
public final class CanvasUtil {

    /** The size of one pixel. */
    private static final double PIXEL_SIZE = 1.0;

    /** Private constructor to prevent instantiation, static methods only. */
    private CanvasUtil() {
        // Nothing to do.
    }

    /**
     * Snaps the given coordinate to the nearest pixel, so that strokes are crisp.
     *
     * @param coordinate the coordinate
     * @return the coordinate snapped to the nearest pixel
     * @see <a href="https://dlsc.com/2014/04/10/javafx-tip-2-sharp-drawing-with-canvas-api/">This
     * tip from DLSC</a>
     * @see javafx.scene.shape.Shape Shape's Javadoc paragraph "Interaction with coordinate systems"
     * for more explanations
     */
    public static double snapToPixel(final double coordinate) {
        /*
         * The coordinate system used by JavaFx makes that integer values fall on pixel
         * boundaries, not on pixel centers. So this function not only takes the integer value
         * of the given coordinate but also adds an offset of half a pixel size to exactly fall
         * on a pixel.
         */
        return rint(coordinate) + PIXEL_SIZE / 2;
    }

    /**
     * Returns the size of one pixel.
     *
     * @return the size of one pixel
     */
    public static double pixelSize() {
        return PIXEL_SIZE;
    }
}
