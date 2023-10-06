/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.concurrent;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * An {@link ExecutorService} with a simple and quick {@link #close()} implementation - initiate
 * termination, await termination, then interrupt if still not terminated after
 * {@link #SHUTDOWN_TIMEOUT a little while}.
 */
final class QuickClosureExecutorService extends AbstractExecutorService {

    /** The shutdown timeout (in milliseconds). */
    private static final long SHUTDOWN_TIMEOUT = 250L;

    /** The actual executor service. */
    private final ExecutorService executorService;

    /**
     * Constructs an instance.
     *
     * @param executorServiceArg the actual executor service
     */
    QuickClosureExecutorService(final ExecutorService executorServiceArg) {
        executorService = executorServiceArg;
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return executorService.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executorService.isTerminated();
    }

    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit)
            throws InterruptedException {
        return executorService.awaitTermination(timeout, unit);
    }

    @Override
    public void execute(final Runnable command) {
        executorService.execute(command);
    }

    @Override
    public void close() {
        shutdown();
        try {
            if (!awaitTermination(SHUTDOWN_TIMEOUT, TimeUnit.MILLISECONDS)) {
                shutdownNow();
            }
        } catch (final InterruptedException ex) {
            shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
