/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.sat;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.sat4j.pb.IPBSolver;
import org.sat4j.pb.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IVecInt;

/**
 * A SAT (actually, pseudo-boolean) solver configured to solve crossword problems.
 *
 * <p>It is a basic definition of the problem, without any optimization attempt. As such, it is quite slow. The problem
 * definition follows.
 *
 * <h2>Variables</h2>
 *
 * <ul>
 *   <li>Cell variables: For each pair (cell,letter) is associated a variable.
 *   <li>Slot variables: For each pair (slot,word) is associated a variable. They are placed "after" the cell variables
 *       in the model.
 * </ul>
 *
 * <h2>Constraints</h2>
 *
 * <ol>
 *   <li>Each cell must contain one and only one letter from the alphabet or a block.
 *   <li>Each slot must contain one and only one word from the input word list. This is the tricky part, as there must
 *       be a correspondence between cell variables and slot variables. Basically, each slot variable - i.e. a
 *       representation of a (slot,word) pair - is equivalent to a conjunction (= and) of cell variables - i.e.
 *       (cell,letter) pairs.
 *   <li>Prefilled cells must be kept as is.
 * </ol>
 *
 * @see <a href="https://codingnest.com/modern-sat-solvers-fast-neat-underused-part-1-of-n/">Martin Hořeňovský's
 *     introduction to SAT solvers</a>. It very clearly explains the basics with the example of the sudoku problem.
 *     Associated code is in C++.
 * @see <a href="https://gitlab.com/super7ramp/sudoku4j">Sudoku4j</a>, which is an example sudoku solver in Java. (It is
 *     a translation in Java of Martin Hořeňovský's example sudoku C++ solver.)
 */
public final class Solver {

    /** The result of the solver. */
    public sealed interface Result {

        /**
         * Result returned when the problem is satisfiable.
         *
         * @param grid a solution grid¬
         */
        record Sat(char[][] grid) implements Result {
            @Override
            public boolean equals(Object other) {
                if (other == this) return true;
                if (!(other instanceof Sat(char[][] otherGrid))) return false;
                return Arrays.deepEquals(this.grid, otherGrid);
            }

            @Override
            public int hashCode() {
                return Arrays.deepHashCode(grid);
            }

            @Override
            public String toString() {
                return "Sat{" +
                        "grid=" + Arrays.deepToString(grid) +
                        '}';
            }
        }

        /**
         * Result returned when the problem is unsatisfiable.
         *
         * @param nonAssignablePositions the positions that cannot be assigned
         */
        record Unsat(Set<Pos> nonAssignablePositions) implements Result {}

        /**
         * Returns a new {@link Sat} instance.
         *
         * @param grid the solution grid
         * @return a new {@link Sat} instance
         */
        static Sat sat(char[][] grid) {
            return new Sat(grid);
        }

        /**
         * Returns a new {@link Unsat} instance.
         *
         * @return a new {@link Unsat} instance
         */
        static Unsat unsat() {
            return new Unsat(Set.of());
        }

        /**
         * Returns a new {@link Unsat} instance.
         *
         * @param nonAssignablePositions the positions that cannot be assigned
         * @return a new {@link Unsat} instance
         */
        static Unsat unsat(final Set<Pos> nonAssignablePositions) {
            return new Unsat(nonAssignablePositions);
        }
    }

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
     * @throws NullPointerException if any of the given parameters is {@code null}
     * @throws IllegalArgumentException if grid is invalid (e.g. inconsistent number of rows or columns)
     */
    public Solver(final char[][] cells, final String[] words) {
        Objects.requireNonNull(words);
        final var grid = new Grid(cells);
        variables = new Variables(grid, words.length);
        constraints = new Constraints(grid, words, variables);
        satSolver = SolverFactory.newDefault();
    }

    /**
     * Runs the solver.
     *
     * <p>Method must be called at most once. Behavior upon a second call is undefined.
     *
     * @return a {@link Result} encapsulating either the solved grid or the non-assignable positions
     * @throws InterruptedException if interrupted while solving
     */
    public Result solve() throws InterruptedException {
        try {
            allocateVariables();
            addClauses();
            return findSolution();
        } catch (final ContradictionException e) {
            return Result.unsat();
        }
    }

    /** Allocates variables. Optional but Sat4j javadoc advises to do it for performance. */
    private void allocateVariables() {
        final int numberOfVariables = variables.count();
        satSolver.newVar(numberOfVariables);
    }

    /**
     * Adds clauses to the solver.
     *
     * @throws ContradictionException if grid is trivially unsatisfiable
     * @throws InterruptedException if interrupted while adding constraints to the solver
     */
    private void addClauses() throws ContradictionException, InterruptedException {
        constraints.addOneLetterOrBlockPerCellClausesTo(satSolver);
        constraints.addOneWordPerSlotClausesTo(satSolver);
        // The input grid constraints are not added to the solver here as clauses but later in solve() as assumptions.
        // Using them as assumptions allows to get an explanation when the problem is unsatisfiable.
    }

    /**
     * Runs the solver.
     *
     * @return a {@link Result} encapsulating either the solved grid or the non-assignable positions
     * @throws InterruptedException if interrupted while solving
     */
    private Result findSolution() throws InterruptedException {
        if (!problemIsSatisfiable()) {
            final IVecInt unsatExplanation = satSolver.unsatExplanation();
            final Set<Pos> nonAssignablePositions = variables.backToPositions(unsatExplanation);
            return Result.unsat(nonAssignablePositions);
        }
        final char[][] solution = variables.backToGrid(satSolver::model);
        return Result.sat(solution);
    }

    /**
     * Evaluates whether the problem is satisfiable.
     *
     * @implNote Sat4j solver does not respond to thread interruption. In order to respond to thread
     * interruption, this method launches the solver in a dedicated thread and makes the caller thread waits
     * for the result. If this wait is interrupted, then the solver is stopped from caller thread using
     * {@link IPBSolver#expireTimeout} which seems the only way to stop a running Sat4j solver.
     */
    private boolean problemIsSatisfiable() throws InterruptedException {
        try (final ExecutorService executor = Executors.newSingleThreadExecutor()) {
            final IVecInt inputGridConstraints = constraints.inputGridConstraintsAreSatisfied();
            return executor.submit(() -> satSolver.isSatisfiable(inputGridConstraints))
                    .get();
        } catch (final ExecutionException e) {
            // Should not happen.
            throw new IllegalStateException(e);
        } catch (final InterruptedException e) {
            // Will stop the solver.
            satSolver.expireTimeout();
            throw e;
        }
    }
}
