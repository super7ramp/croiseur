/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.sat;

/**
 * Where translation of problem data from/to integer variables occurs.
 * <p>
 * There are two kinds of variables:
 * <ul>
 *     <li>Cell variables: For each pair (cell,letter) is associated a variable. See
 *     {@link #cell(int, int, int)} for the translation.
 *     <li>Slot variables: For each pair (slot,word) is associated a variable. They are placed
 *     "after" the cell variables in the model. See {@link #slot(int, int)} for the translation.
 * </ul>
 */
final class Variables {

    /** The number of values that a cell of a solved grid can take. */
    static final int NUMBER_OF_CELL_VALUES = Alphabet.numberOfLetters() + 1 /* block */;

    /** The numerical representation of a block (the value of a shaded cell). */
    static final int BLOCK_INDEX = Alphabet.numberOfLetters();

    /** The crossword grid. */
    private final Grid grid;

    /** The number of words in the dictionary. */
    private final int numberOfWords;

    /**
     * Constructs an instance.
     *
     * @param gridArg          the grid
     * @param numberOfWordsArg the number of words in the dictionary
     */
    Variables(final Grid gridArg, final int numberOfWordsArg) {
        grid = gridArg;
        numberOfWords = numberOfWordsArg;
    }

    /**
     * The (maximum) number of variables.
     *
     * @return the number of variables
     */
    int count() {
        return cellCount() + slotCount();
    }

    /**
     * The number of cell variables.
     *
     * @return the number of cell variables
     */
    int cellCount() {
        return grid.numberOfColumns() * grid.numberOfRows() * NUMBER_OF_CELL_VALUES;
    }

    /**
     * The (maximum) number of slot variables.
     * <p>
     * Slot variables may be discarded at constraint construction time, when a dictionary word
     * obviously doesn't match a slot, hence the "maximum".
     *
     * @return the number of slot variables
     */
    int slotCount() {
        return grid.numberOfSlots() * numberOfWords;
    }

    /**
     * Returns the variable associated to the given value at the given cell.
     * <p>
     * Cell variables are put first in the model.
     * <table>
     *     <caption>Variable/letter association</caption>
     *   <tr>
     *     <th>Variable for (0,0)</th>
     *     <td>1</td>
     *     <td>2</td>
     *     <td>3</td>
     *     <td>...</td>
     *     <td>26</td>
     *     <td>27</td>
     *     <th>Variable for (0,1)</th>
     *     <td>28</td>
     *     <td>29</td>
     *     <td>30</td>
     *     <td>...</td>
     *     <td>54</td>
     *     <td>55</td>
     *     <th>etc.</th>
     *   </tr>
     *   <tr>
     *     <th>Represented value</th>
     *     <td>A</td>
     *     <td>B</td>
     *     <td>C</td>
     *     <td>..</td>
     *     <td>Z</td>
     *     <td>#</td>
     *     <th>Represented value</th>
     *     <td>A</td>
     *     <td>B</td>
     *     <td>C</td>
     *     <td>..</td>
     *     <td>Z</td>
     *     <td>#</td>
     *     <th>etc.</th>
     *   </tr>
     * </table>
     *
     * @param row    the cell row
     * @param column the cell column
     * @param value  the numerical representation of the cell value
     * @return the variable associated to the given value of the given cell
     */
    int cell(final int row, final int column, final int value) {
        return row * grid.numberOfColumns() * NUMBER_OF_CELL_VALUES +
               column * NUMBER_OF_CELL_VALUES +
               value +
               1; // variable must be strictly positive
    }

    /**
     * Returns the variable associated to the given word at the given slot.
     * <p>
     * Slot variables are put after cell variables in the model.
     *
     * @param slotIndex the slot index in the grid slot list
     * @param wordIndex the word index in the word list
     * @return the variable associated to the given word of the given slot
     */
    int slot(final int slotIndex, final int wordIndex) {
        return slotVariableOffset() + slotIndex * numberOfWords + wordIndex;
    }

    /**
     * The first slot variable.
     * <p>
     * Slot variable are put after cell variables, so first slot variable corresponds to the number
     * of cell variables (plus 1 because variables start at 1).
     *
     * @return the first slot variable
     */
    private int slotVariableOffset() {
        return cellCount() + 1;
    }

    /**
     * Translates solver model back to a crossword grid.
     *
     * @param model the solver model
     * @return a crossword grid
     */
    char[][] backToDomain(final int[] model) {
        final char[][] outGrid = new char[grid.numberOfRows()][grid.numberOfColumns()];
        for (int row = 0; row < grid.numberOfRows(); row++) {
            for (int column = 0; column < grid.numberOfColumns(); column++) {
                for (int value = 0; value < NUMBER_OF_CELL_VALUES; value++) {
                    final int variable = cell(row, column, value) - 1;
                    if (model[variable] > 0) {
                        outGrid[row][column] =
                                value == BLOCK_INDEX ? Grid.BLOCK : Alphabet.letterAt(value);
                        break;
                    }
                }
            }
        }
        return outGrid;
    }
}
