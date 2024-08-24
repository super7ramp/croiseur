/*
 * SPDX-FileCopyrightText: 2024 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.sat;

import java.util.function.IntPredicate;

/**
 * Where translation of problem data from/to integer variables occurs.
 * <p>
 * There are two kinds of variables:
 * <ul>
 *     <li>Variables representing cells: For each pair (cell,letter) is associated a variable. See
 *     {@link #representingCell(int, int, int)} for the translation.
 *     <li>Variables representing slots: For each pair (slot,word) is associated a variable. They are placed
 *     "after" the cell variables in the model. See {@link #representingSlot(int, int)} for the translation.
 * </ul>
 */
final class Variables {

    /** The number of values that a cell of a solved grid can take. */
    static final int CELL_VALUE_COUNT = Alphabet.letterCount() + 1 /* block */;

    /** The numerical representation of a block (the value of a shaded cell). */
    static final int BLOCK_INDEX = Alphabet.letterCount();

    /** The crossword grid. */
    private final Grid grid;

    /** The number of words in the dictionary. */
    private final int wordCount;

    /**
     * Constructs an instance.
     *
     * @param gridArg      the grid
     * @param wordCountArg the number of words in the dictionary
     */
    Variables(final Grid gridArg, final int wordCountArg) {
        grid = gridArg;
        wordCount = wordCountArg;
    }

    /**
     * The (maximum) number of variables.
     *
     * @return the number of variables
     */
    int count() {
        return representingCellCount() + representingSlotCount();
    }

    /**
     * The number of variables representing cells.
     *
     * @return the number of variables representing cells
     */
    int representingCellCount() {
        return grid.columnCount() * grid.rowCount() * CELL_VALUE_COUNT;
    }

    /**
     * The number of variables representing slots.
     *
     * @return the number of variables representing slots
     */
    int representingSlotCount() {
        return grid.slotCount() * wordCount;
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
     *     <td>53</td>
     *     <td>54</td>
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
    int representingCell(final int row, final int column, final int value) {
        return row * grid.columnCount() * CELL_VALUE_COUNT +
               column * CELL_VALUE_COUNT +
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
    int representingSlot(final int slotIndex, final int wordIndex) {
        return representingCellCount() // last cell variable
               + slotIndex * wordCount
               + wordIndex
               + 1;
    }

    /**
     * Translates solver model back to a crossword grid.
     *
     * @param model the solver model, as an {@link IntPredicate} providing truth values of tested variables
     * @return a crossword grid
     */
    char[][] backToDomain(final IntPredicate model) {
        final char[][] outGrid = new char[grid.rowCount()][grid.columnCount()];
        for (int row = 0; row < grid.rowCount(); row++) {
            for (int column = 0; column < grid.columnCount(); column++) {
                for (int value = 0; value < CELL_VALUE_COUNT; value++) {
                    final int variable = representingCell(row, column, value);
                    if (model.test(variable)) {
                        outGrid[row][column] = value == BLOCK_INDEX ? Grid.BLOCK : Alphabet.letterAt(value);
                        break;
                    }
                }
            }
        }
        return outGrid;
    }
}
