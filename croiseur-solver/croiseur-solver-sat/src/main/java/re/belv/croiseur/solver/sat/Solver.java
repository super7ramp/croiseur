/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.sat;

import org.sat4j.pb.IPBSolver;
import org.sat4j.pb.SolverFactory;
import org.sat4j.specs.ContradictionException;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A SAT (actually, pseudo-boolean) solver configured to solve crossword problems.
 * <p>
 * It is a basic definition of the problem, without any optimization attempt. As such, it is quite
 * slow. The problem definition follows.
 * <h2>Variables</h2>
 * <ul>
 *     <li>Cell variables: For each pair (cell,letter) is associated a variable.
 *     <li>Slot variables: For each pair (slot,word) is associated a variable. They are placed
 *     "after" the cell variables in the model.
 * </ul>
 * <h2>Constraints</h2>
 * <ol>
 *     <li>Each cell must contain one and only one letter from the alphabet or a block.
 *     <li>Each slot must contain one and only one word from the input word list. This is the tricky
 *     part, as there must be a correspondence between cell variables and slot variables. Basically,
 *     each slot variable - i.e. a representation of a (slot,word) pair - is equivalent to a
 *     conjunction (= and) of cell variables - i.e. (cell,letter) pairs.
 *     <li>Prefilled cells must be kept as is.</li>
 * </ol>
 *
 * @see <a href="https://codingnest.com/modern-sat-solvers-fast-neat-underused-part-1-of-n/">Martin
 * Hořeňovský's introduction to SAT solvers</a>. It very clearly explains the basics with the
 * example of the sudoku problem. Associated code is in C++.
 * @see <a href="https://gitlab.com/super7ramp/sudoku4j">Sudoku4j</a>, which is an example sudoku
 * solver in Java. (It is a translation in Java of Martin Hořeňovský's example sudoku C++ solver.)
 */
// TODO make it faster (no idea how yet except by playing with the different Sat4j solvers)
public final class Solver {

    /** The actual solver. */
    private final IPBSolver satSolver;

    /** The problem variables. */
    private final Variables variables;

    /** The problem constraints. */
    private final Constraints constraints;

    /**
     * Constructs a solver for the given grid and word list.
     *
     * @param cells the grid cells
     * @param words the word list; Only characters of the {@link Alphabet} are supported
     * @throws NullPointerException     if any of the given parameters is {@code null}
     * @throws IllegalArgumentException if grid is invalid (e.g. inconsistent number of rows or
     *                                  columns)
     */
    public Solver(final char[][] cells, final String[] words) {
        Objects.requireNonNull(words);
        final Grid grid = new Grid(cells);
        variables = new Variables(grid, words.length);
        constraints = new Constraints(grid, words, variables);
        satSolver = SolverFactory.newDefault();
    }

    /**
     * Runs the solver.
     * <p>
     * Method must be called at most once. Behavior upon a second call is undefined.
     *
     * @return the solved grid or an empty grid if no solution is found
     * @throws InterruptedException if interrupted while solving
     */
    public char[][] solve() throws InterruptedException {
        try {
            allocateVariables();
            addClauses();
            return findSolution();
        } catch (final ContradictionException e) {
            return noSolution();
        }
    }

    /**
     * Allocates variables. Optional but Sat4j javadoc advises to do it for performance.
     */
    private void allocateVariables() {
        final int numberOfVariables = variables.count();
        satSolver.newVar(numberOfVariables);
    }

    /**
     * Adds clauses to the solver.
     *
     * @throws ContradictionException if grid is trivially unsatisfiable
     * @throws InterruptedException   if interrupted while adding constraints to the solver
     */
    private void addClauses() throws ContradictionException, InterruptedException {
        constraints.addOneLetterOrBlockPerCellClausesTo(satSolver);
        constraints.addOneWordPerSlotClausesTo(satSolver);
        constraints.addInputGridConstraintsAreSatisfiedClausesTo(satSolver);
        // TODO ideally no word should be duplicated
    }

    /**
     * Runs the solver.
     *
     * @return the solution, if any, otherwise an empty array
     * @throws InterruptedException if interrupted while solving
     */
    private char[][] findSolution() throws InterruptedException {
        if (!problemIsSatisfiable()) {
            return noSolution();
        }
        return variables.backToDomain(satSolver.model());
    }

    /**
     * Evaluates whether the problem is satisfiable.
     * <p>
     * Implementation note: Sat4j solver does not respond to thread interruption. In order to
     * respond to thread interruption, this method launches the solver in a dedicated thread and
     * makes the caller thread waits (interruptibly) for a result.
     */
    private boolean problemIsSatisfiable() throws InterruptedException {
        try (final ExecutorService executor = Executors.newSingleThreadExecutor()) {
            final Future<Boolean> problemIsSatisfiable =
                    executor.submit(() -> satSolver.isSatisfiable());
            return problemIsSatisfiable.get();
        } catch (final ExecutionException e) {
            // Should not happen.
            throw new IllegalStateException(e);
        } catch (final InterruptedException e) {
            // Will stop the solver.
            satSolver.expireTimeout();
            throw e;
        }
    }

    /**
     * Returns a new empty 2D array, denoting the absence of solution.
     *
     * @return a new empty 2D array
     */
    private static char[][] noSolution() {
        return new char[][]{};
    }
}
