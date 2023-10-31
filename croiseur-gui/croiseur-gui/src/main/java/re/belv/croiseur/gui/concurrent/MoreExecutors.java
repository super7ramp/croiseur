/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Factory of {@link ExecutorService}.
 */
public final class MoreExecutors {

    /** Prevents instantiation. */
    private MoreExecutors() {
        // Nothing to do.
    }

    /**
     * Creates a new {@link ExecutorService} backed by a fixed number of threads operating off an
     * unbounded queue. Close strategy is more aggressive than default one, it will try to forcibly
     * shutdown executor if task does not complete in a few hundreds milliseconds.
     *
     * @param nThreads the number of threads in the pool
     * @return a new {@link ExecutorService}
     * @see Executors#newFixedThreadPool(int)
     */
    public static ExecutorService newQuickClosureFixedThreadPool(final int nThreads) {
        final ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        return new QuickClosureExecutorService(executor);
    }
}
