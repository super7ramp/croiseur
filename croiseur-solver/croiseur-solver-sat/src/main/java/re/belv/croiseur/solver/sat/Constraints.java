/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.sat;

import org.sat4j.core.VecInt;
import org.sat4j.pb.IPBSolver;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.IVecInt;
import org.sat4j.tools.GateTranslator;

import java.util.List;

/**
 * Where crossword problem constraints are built.
 * <p>
 * The constraints are:
 * <ol>
 *     <li>Each cell must contain one and only one letter from the alphabet or a block. See
 *     {@link #addOneLetterOrBlockPerCellClausesTo}.</li>
 *     <li>Each slot must contain one and only one word from the input word list. This is the tricky
 *     part, as there must be a correspondence between cell variables and slot variables. Basically,
 *     each slot variable - i.e. a representation of a (slot,word) pair - is equivalent to a
 *     conjunction (= and) of cell variables - i.e. (cell,letter) pairs. See
 *     {@link #addOneWordPerSlotClausesTo}.</li>
 *     <li>Prefilled cells must be kept as is. See {@link #addInputGridConstraintsAreSatisfiedClausesTo}.</li>
 * </ol>
 * Implementation note: Methods here add rules to the solver passed as parameter. Although having
 * just a factory of constraints here, to be applied separately, would be nice, it does not scale
 * in terms of memory: There are too many literals and clauses. Hence, the choice to add the clauses
 * progressively to the solver.
 */
final class Constraints {

    /**
     * The length of the buffer used to store cell literals corresponding to a word in a slot. Most
     * words/slots should be smaller than this size.
     */
    private static final int CELL_LITERALS_BUFFER_LENGTH = 20;

    /** The crossword grid. */
    private final Grid grid;

    /** The dictionary words. */
    private final String[] words;

    /** The problem variables. */
    private final Variables variables;

    /**
     * Constructs an instance.
     *
     * @param gridArg      the grid
     * @param wordsArg     the words
     * @param variablesArg the variables
     */
    Constraints(final Grid gridArg, final String[] wordsArg, final Variables variablesArg) {
        grid = gridArg;
        words = wordsArg;
        variables = variablesArg;
    }

    /**
     * Adds the clauses ensuring that each cell must contain exactly one letter from the alphabet -
     * or a block - to the given solver.
     *
     * @param solver the solver to which to add the clauses
     * @throws ContradictionException should not happen
     * @throws InterruptedException   if interrupted while adding constraints to the solver
     */
    void addOneLetterOrBlockPerCellClausesTo(final IPBSolver solver)
            throws ContradictionException, InterruptedException {
        final IVecInt literalsBuffer = new VecInt(Variables.NUMBER_OF_CELL_VALUES);
        for (int row = 0; row < grid.numberOfRows(); row++) {
            for (int column = 0; column < grid.numberOfColumns(); column++) {
                checkForInterruption();
                for (int letterIndex = 0; letterIndex < Alphabet.numberOfLetters();
                     letterIndex++) {
                    final int letterVariable = variables.cell(row, column, letterIndex);
                    literalsBuffer.push(letterVariable);
                }
                final int blockVariable = variables.cell(row, column, Variables.BLOCK_INDEX);
                literalsBuffer.push(blockVariable);
                addExactlyOne(solver, literalsBuffer);
                literalsBuffer.clear();
            }
        }
    }

    /**
     * Adds the clauses ensuring that each slot must contain exactly one word from the word list to
     * the given solver.
     *
     * @param solver the solver to which to add the clauses
     * @throws ContradictionException if a slot has no word candidate
     * @throws InterruptedException   if interrupted while adding constraints to the solver
     */
    void addOneWordPerSlotClausesTo(final IPBSolver solver)
            throws ContradictionException, InterruptedException {
        final GateTranslator gator = new GateTranslator(solver);
        final IVecInt slotLiteralsBuffer = new VecInt(words.length);
        final IVecInt cellLiteralsBuffer = new VecInt(CELL_LITERALS_BUFFER_LENGTH);
        for (final Slot slot : grid.slots()) {
            for (int wordIndex = 0; wordIndex < words.length; wordIndex++) {
                checkForInterruption();
                final String word = words[wordIndex];
                if (word.length() == slot.length()) {
                    final int slotVariable = variables.slot(slot.index(), wordIndex);
                    slotLiteralsBuffer.push(slotVariable);
                    fillCellLiteralsConjunction(cellLiteralsBuffer, slot, word);
                    gator.and(slotVariable, cellLiteralsBuffer);
                    cellLiteralsBuffer.clear();
                } // else skip this word since it obviously doesn't match the slot
            }
            // The following instruction may raise a ContradictionException if literals is empty,
            // i.e. if a slot has no valid candidate (which means the problem is trivially
            // unsatisfiable).
            addExactlyOne(solver, slotLiteralsBuffer);
            slotLiteralsBuffer.clear();
        }
    }

    /**
     * Fills the given vector with the cell literals whose conjunction (= and) is equivalent to the
     * slot variable of the given slot and word.
     *
     * @param cellLiterals the vector to fill  with the cell literals equivalent to the slot
     *                     variable of the given slot and word
     * @param slot         the slot
     * @param word         the word
     * @throws IllegalStateException if the given word contains a letter which is not in the
     *                               {@link Alphabet}
     */
    private void fillCellLiteralsConjunction(final IVecInt cellLiterals, final Slot slot,
                                             final String word) {
        final List<Pos> slotPositions = slot.positions();
        final int wordLength = word.length();
        for (int i = 0; i < wordLength; i++) {
            final Pos slotPos = slotPositions.get(i);
            final char letter = word.charAt(i);
            final int letterIndex = Alphabet.letterIndex(letter);
            if (letterIndex < 0) {
                throw new IllegalStateException("Unsupported character: " + letter);
            }
            final int cellVar = variables.cell(slotPos.row(), slotPos.column(), letterIndex);
            cellLiterals.push(cellVar);
        }
    }

    /**
     * Adds the clauses ensuring that each prefilled letter/block must be preserved to the given
     * solver.
     *
     * @param solver the solver to which to add the clauses
     * @throws ContradictionException should not happen
     * @throws InterruptedException   if interrupted while adding constraints to the solver
     */
    void addInputGridConstraintsAreSatisfiedClausesTo(final ISolver solver)
            throws ContradictionException, InterruptedException {
        final IVecInt literalsBuffer = new VecInt(1);
        for (int row = 0; row < grid.numberOfRows(); row++) {
            for (int column = 0; column < grid.numberOfColumns(); column++) {
                checkForInterruption();
                final char prefilledLetter = grid.letterAt(row, column);
                if (prefilledLetter == Grid.EMPTY) {
                    // Disallow solver to create a block
                    final int blockVariable = variables.cell(row, column, Variables.BLOCK_INDEX);
                    literalsBuffer.push(-blockVariable);
                } else if (prefilledLetter == Grid.BLOCK) {
                    final int blockVariable = variables.cell(row, column, Variables.BLOCK_INDEX);
                    literalsBuffer.push(blockVariable);
                } else {
                    final int letterIndex = Alphabet.letterIndex(prefilledLetter);
                    final int letterVariable = variables.cell(row, column, letterIndex);
                    literalsBuffer.push(letterVariable);
                }
                solver.addClause(literalsBuffer);
                literalsBuffer.clear();
            }
        }
    }

    /**
     * Adds the given literal to the solver as <em>exactly-one</em> clauses.
     * <p>
     * Note in implementation the usage of {@link IPBSolver#addExactly(IVecInt, IVecInt, int)}
     * instead of {@link IPBSolver#addExactly(IVecInt, int)}: The first one uses the pseudo-boolean
     * solver implementation while the second one actually delegates to the original core solver
     * implementation. Note sure why {@link IPBSolver} doesn't override implementation of the second
     * method.
     *
     * @param solver   the solver to which to add the literals as an <em>exactly-one</em> clause
     * @param literals the literals to add
     * @throws ContradictionException should not happen
     */
    private void addExactlyOne(final IPBSolver solver, final IVecInt literals)
            throws ContradictionException {
        final IVecInt coefficients = new VecInt(literals.size());
        for (int i = 0; i < literals.size(); i++) {
            coefficients.push(1);
        }
        solver.addExactly(literals, coefficients, 1);
    }

    /**
     * Checks for current thread interruption.
     *
     * @throws InterruptedException if current thread is marked as interrupted
     */
    private static void checkForInterruption() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("Interrupted while adding constraints");
        }
    }
}
