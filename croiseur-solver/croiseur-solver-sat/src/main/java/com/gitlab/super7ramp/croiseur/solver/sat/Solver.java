/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.sat;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.GateTranslator;

import java.util.List;

/**
 * A SAT solver configured to solve crossword problems.
 * <p>
 * It is a very basic definition of the problem, without any optimization attempt. As such, it is
 * quite slow. The problem definition follows.
 * <h2>Variables</h2>
 * <ul>
 *     <li>Cell variables: For each pair (cell,letter) is associated a variable. See
 *     {@link #toCellVariable} for the translation.
 *     <li>Slot variables: For each pair (slot,word) is associated a variable. They are placed
 *     "after" the cell variables in the model. See {@link #toSlotVariable} for the translation.
 * </ul>
 * <h2>Constraints</h2>
 * <ol>
 *     <li>Each cell must contain one and only one letter from the alphabet or a block. See
 *     {@link #oneLetterPerCell()}.</li>
 *     <li>Each slot must contain one and only one word from the input word list. This is the tricky
 *     part, as there must be a correspondence between cell variables and slot variables. Basically,
 *     each slot variable - i.e. a representation of a (slot,word) pair - is equivalent to a
 *     conjunction (= and) of cell variables - i.e. (cell,letter) pairs. See
 *     {@link #oneWordPerSlot()}.</li>
 *     <li>Prefilled cells must be kept as is. See {@link #inputGridConstraintsAreSatisfied()}.</li>
 * </ol>
 *
 * @see <a href="https://codingnest.com/modern-sat-solvers-fast-neat-underused-part-1-of-n/">Martin
 * Hořeňovský's introduction to SAT solvers</a>. It very clearly explains the basics with the
 * example of the sudoku problem. Associated code is in C++.
 * @see <a href="https://gitlab.com/super7ramp/sudoku4j">Sudoku4j</a>, which is an example sudoku
 * solver in Java (basically just a translation in Java of Martin Hořeňovský's example sudoku C++
 * solver).
 */
// TODO reduce VecInt allocations (they can be reused once their content has been added to a clause)
public final class Solver {

    /** The number of values that a cell of a solved grid can take. */
    private static final int NUMBER_OF_VALUES = Alphabet.numberOfLetters() + 1 /* block */;

    /** The numerical representation of a block (the value of a shaded cell). */
    private static final int BLOCK_INDEX = Alphabet.numberOfLetters();

    /** The input grid. */
    private final Grid grid;

    /** The word list. */
    private final String[] words;

    /** The actual solver. */
    private final GateTranslator satSolver;

    /**
     * Constructs a solver for the given grid and word list.
     *
     * @param gridArg  the grid
     * @param wordsArg the word list
     */
    public Solver(final char[][] gridArg, final String[] wordsArg) {
        grid = new Grid(gridArg);
        words = wordsArg;
        satSolver = new GateTranslator(SolverFactory.newDefault());
    }

    /**
     * Runs the solver.
     *
     * @return the solved grid or an empty grid if no solution is found
     */
    public char[][] solve() {
        try {
            allocateVariables();
            addRules();
            return findSolution();
        } catch (final ContradictionException | TimeoutException e) {
            return noSolution();
        }
    }

    /**
     * Allocates variables. Optional but Sat4j javadoc advises to do it for performance.
     */
    private void allocateVariables() {
        final int numberOfVariables =
                // cell variables
                grid.numberOfColumns() * grid.numberOfRows() * NUMBER_OF_VALUES +
                // slot variables
                grid.numberOfSlots() * words.length;
        satSolver.newVar(numberOfVariables);
    }

    /**
     * Add rules to the solver.
     *
     * @throws ContradictionException if grid is trivially unsatisfiable
     */
    private void addRules() throws ContradictionException {
        oneLetterPerCell();
        oneWordPerSlot();
        inputGridConstraintsAreSatisfied();
        // TODO ideally no word should be duplicated
    }

    /**
     * Rule 1: Each cell must contain exactly one letter from the alphabet - or a block.
     *
     * @throws ContradictionException should not happen
     */
    private void oneLetterPerCell() throws ContradictionException {
        for (int row = 0; row < grid.numberOfRows(); row++) {
            for (int column = 0; column < grid.numberOfColumns(); column++) {
                final IVecInt literals = new VecInt(NUMBER_OF_VALUES);
                for (int letterIndex = 0; letterIndex < Alphabet.numberOfLetters();
                     letterIndex++) {
                    final int letterVariable = toCellVariable(row, column, letterIndex);
                    literals.push(letterVariable);
                }
                final int blockVariable = toCellVariable(row, column, BLOCK_INDEX);
                literals.push(blockVariable);
                satSolver.addExactly(literals, 1);
            }
        }
    }

    /**
     * Rule 2: Each slot must contain exactly one word from the word list.
     *
     * @throws ContradictionException if a slot has no word candidate
     */
    private void oneWordPerSlot() throws ContradictionException {
        for (final Slot slot : grid.slots()) {
            final IVecInt slotLiterals = new VecInt(words.length);
            for (int wordIndex = 0; wordIndex < words.length; wordIndex++) {
                final String word = words[wordIndex];
                if (word.length() == slot.length()) {
                    final int slotVariable = toSlotVariable(slot.index(), wordIndex);
                    addCellLiteralsConjunction(slotVariable, slot, word);
                    slotLiterals.push(slotVariable);
                } // else skip this word since it obviously doesn't match the slot
            }
            // The following instruction may raise a ContradictionException if literals is empty,
            // i.e. if a slot has no valid candidate (which means the problem is trivially
            // unsatisfiable).
            satSolver.addExactly(slotLiterals, 1);
        }
    }

    /**
     * Registers the link between a slot variable and cells variables. A slot variable corresponds
     * to a conjunction (= and) of cell variables.
     *
     * @param slot         the slot
     * @param word         the word
     * @param slotVariable the slot variable
     * @throws ContradictionException should not happen
     */
    private void addCellLiteralsConjunction(final int slotVariable, final Slot slot,
                                            final String word) throws ContradictionException {
        final List<Pos> slotPositions = slot.positions();
        final int wordLength = word.length();
        final IVecInt cellLiterals = new VecInt(wordLength);
        for (int i = 0; i < wordLength; i++) {
            final Pos slotPos = slotPositions.get(i);
            final int letterIndex = Alphabet.letterIndex(word.charAt(i));
            final int cellVar = toCellVariable(slotPos.row(), slotPos.column(), letterIndex);
            cellLiterals.push(cellVar);
        }
        satSolver.and(slotVariable, cellLiterals);
    }

    /**
     * Rule 3: Each prefilled letter/block must be preserved.
     *
     * @throws ContradictionException should not happen
     */
    private void inputGridConstraintsAreSatisfied() throws ContradictionException {
        for (int row = 0; row < grid.numberOfRows(); row++) {
            for (int column = 0; column < grid.numberOfColumns(); column++) {
                final char prefilledLetter = grid.letterAt(row, column);
                final IVecInt literals;
                if (prefilledLetter == Grid.EMPTY) {
                    // Disallow solver to create a block
                    final int blockVariable = toCellVariable(row, column, BLOCK_INDEX);
                    literals = new VecInt(new int[]{-blockVariable});
                } else if (prefilledLetter == Grid.BLOCK) {
                    final int blockVariable = toCellVariable(row, column, BLOCK_INDEX);
                    literals = new VecInt(new int[]{blockVariable});
                } else {
                    final int letterIndex = Alphabet.letterIndex(prefilledLetter);
                    final int letterVariable = toCellVariable(row, column, letterIndex);
                    literals = new VecInt(new int[]{letterVariable});
                }
                satSolver.addClause(literals);
            }
        }
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
    private int toCellVariable(final int row, final int column, final int value) {
        return row * grid.numberOfColumns() * NUMBER_OF_VALUES +
               column * NUMBER_OF_VALUES +
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
    private int toSlotVariable(final int slotIndex, final int wordIndex) {
        return slotVariableOffset() + slotIndex * words.length + wordIndex;
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
        return grid.numberOfColumns() * grid.numberOfRows() * NUMBER_OF_VALUES + 1;
    }

    /**
     * Runs the solver.
     *
     * @return the solution, if any, otherwise an empty array
     * @throws TimeoutException if no solution is found before timeout
     */
    private char[][] findSolution() throws TimeoutException {
        final IProblem problem = satSolver;
        if (!problem.isSatisfiable()) {
            return noSolution();
        }
        return toGrid(problem.model());
    }

    /**
     * Returns a new empty 2D array, denoting the absence of solution.
     *
     * @return a new empty 2D array
     */
    private static char[][] noSolution() {
        return new char[][]{};
    }

    /**
     * Translates solver model back to a crossword grid.
     *
     * @param model the solver model
     * @return a crossword grid
     */
    private char[][] toGrid(final int[] model) {
        final char[][] outGrid = new char[grid.numberOfRows()][grid.numberOfColumns()];
        for (int row = 0; row < grid.numberOfRows(); row++) {
            for (int column = 0; column < grid.numberOfColumns(); column++) {
                for (int value = 0; value < NUMBER_OF_VALUES; value++) {
                    final int variable = toCellVariable(row, column, value) - 1;
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
