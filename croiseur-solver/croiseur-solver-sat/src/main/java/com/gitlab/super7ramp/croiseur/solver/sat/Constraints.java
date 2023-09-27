/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.sat;

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
     */
    void addOneLetterOrBlockPerCellClausesTo(final IPBSolver solver) throws ContradictionException {
        for (int row = 0; row < grid.numberOfRows(); row++) {
            for (int column = 0; column < grid.numberOfColumns(); column++) {
                final IVecInt literals = new VecInt(Variables.NUMBER_OF_CELL_VALUES);
                for (int letterIndex = 0; letterIndex < Alphabet.numberOfLetters();
                     letterIndex++) {
                    final int letterVariable = variables.cell(row, column, letterIndex);
                    literals.push(letterVariable);
                }
                final int blockVariable = variables.cell(row, column, Variables.BLOCK_INDEX);
                literals.push(blockVariable);
                addExactlyOne(solver, literals);
            }
        }
    }

    /**
     * Adds the clauses ensuring that each slot must contain exactly one word from the word list to
     * the given solver.
     *
     * @param solver the solver to which to add the clauses
     * @throws ContradictionException if a slot has no word candidate
     */
    void addOneWordPerSlotClausesTo(final IPBSolver solver) throws ContradictionException {
        final GateTranslator gator = new GateTranslator(solver);
        for (final Slot slot : grid.slots()) {
            final IVecInt slotLiterals = new VecInt(words.length);
            for (int wordIndex = 0; wordIndex < words.length; wordIndex++) {
                final String word = words[wordIndex];
                if (word.length() == slot.length()) {
                    final int slotVariable = variables.slot(slot.index(), wordIndex);
                    slotLiterals.push(slotVariable);
                    final IVecInt cellLiteralsConjunction = cellLiteralsConjunctionOf(slot, word);
                    gator.and(slotVariable, cellLiteralsConjunction);
                } // else skip this word since it obviously doesn't match the slot
            }
            // The following instruction may raise a ContradictionException if literals is empty,
            // i.e. if a slot has no valid candidate (which means the problem is trivially
            // unsatisfiable).
            addExactlyOne(solver, slotLiterals);
        }
    }

    /**
     * Returns the cell literals whose conjunction (= and) is equivalent to the slot variable of the
     * given slot and word.
     *
     * @param slot the slot
     * @param word the word
     * @return the cell literals equivalent to the slot variable of the given slot and word
     * @throws IllegalStateException if the given word contains a letter which is not in the
     *                               {@link Alphabet}
     */
    private IVecInt cellLiteralsConjunctionOf(final Slot slot, final String word) {
        final List<Pos> slotPositions = slot.positions();
        final int wordLength = word.length();
        final IVecInt cellLiterals = new VecInt(wordLength);
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
        return cellLiterals;
    }

    /**
     * Adds the clauses ensuring that each prefilled letter/block must be preserved to the given
     * solver.
     *
     * @param solver the solver to which to add the clauses
     * @throws ContradictionException should not happen
     */
    void addInputGridConstraintsAreSatisfiedClausesTo(final ISolver solver)
            throws ContradictionException {
        for (int row = 0; row < grid.numberOfRows(); row++) {
            for (int column = 0; column < grid.numberOfColumns(); column++) {
                final char prefilledLetter = grid.letterAt(row, column);
                final IVecInt literals;
                if (prefilledLetter == Grid.EMPTY) {
                    // Disallow solver to create a block
                    final int blockVariable = variables.cell(row, column, Variables.BLOCK_INDEX);
                    literals = new VecInt(new int[]{-blockVariable});
                } else if (prefilledLetter == Grid.BLOCK) {
                    final int blockVariable = variables.cell(row, column, Variables.BLOCK_INDEX);
                    literals = new VecInt(new int[]{blockVariable});
                } else {
                    final int letterIndex = Alphabet.letterIndex(prefilledLetter);
                    final int letterVariable = variables.cell(row, column, letterIndex);
                    literals = new VecInt(new int[]{letterVariable});
                }
                solver.addClause(literals);
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
}
