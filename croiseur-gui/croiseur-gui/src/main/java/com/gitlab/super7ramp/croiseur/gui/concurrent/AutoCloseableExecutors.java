/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Factory of {@link AutoCloseableExecutorService}.
 */
public final class AutoCloseableExecutors {

    /** Prevents instantiation. */
    private AutoCloseableExecutors() {
        // Nothing to do.
    }

    /**
     * Creates a new {@link AutoCloseableExecutorService} backed by a fixed number of threads
     * operating off an unbounded queue.
     *
     * @param nThreads the number of threads in the pool
     * @return a new {@link AutoCloseableExecutorService}
     * @see Executors#newFixedThreadPool(int)
     */
    public static AutoCloseableExecutorService newFixedThreadPool(final int nThreads) {
        final ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        return new QuickClosureExecutorService(executor);
    }
}
