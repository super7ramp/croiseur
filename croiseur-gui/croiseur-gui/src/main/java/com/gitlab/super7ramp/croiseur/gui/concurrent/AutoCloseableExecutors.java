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
     * Creates a new {@link AutoCloseableExecutorService} backed by a single worker thread
     * operating off an unbounded queue.
     *
     * @return a new {@link AutoCloseableExecutorService}
     * @see Executors#newSingleThreadExecutor()
     */
    public static AutoCloseableExecutorService newSingleThreadExecutor() {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        return new DefaultAutoCloseableExecutorService(executor);
    }
}
