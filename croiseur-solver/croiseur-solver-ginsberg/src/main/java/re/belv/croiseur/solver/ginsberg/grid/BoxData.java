/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.grid;

/** Access to the content of a box. */
interface BoxData {

    /**
     * Returns the value contained in the box.
     *
     * @return the value contained in the box
     * @throws java.util.NoSuchElementException if box is empty
     */
    char value();

    /**
     * Returns whether the box is shaded.
     *
     * <p>Implies {@link #isEmpty()}.
     *
     * @return {@code true} iff the box is shaded
     */
    boolean isShaded();

    /**
     * Returns whether the box is empty.
     *
     * <p>Note that a shaded box is considered as empty.
     *
     * @return {@code true} iff the box is empty
     */
    boolean isEmpty();

    /**
     * Sets the value to the box.
     *
     * @param aCharacter the new value
     */
    void set(final char aCharacter);

    /**
     * Resets the box.
     *
     * <p>No effect on empty or shaded boxes.
     */
    void reset();

    /**
     * Copies the box.
     *
     * @return a deep copy of the box
     */
    BoxData copy();
}
