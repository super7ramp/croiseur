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
 * An {@link AutoCloseableExecutorService} which adds to the given {@link ExecutorService} the
 * same implementation of {@link AutoCloseable#close()} as in Java 19.
 */
final class DefaultAutoCloseableExecutorService extends AbstractExecutorService implements AutoCloseableExecutorService {

    /** The actual executor service. */
    private final ExecutorService executorService;

    /**
     * Constructs an instance.
     *
     * @param executorServiceArg the actual executor service
     */
    DefaultAutoCloseableExecutorService(final ExecutorService executorServiceArg) {
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
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        return executorService.awaitTermination(timeout, unit);
    }

    @Override
    public void execute(final Runnable command) {
        executorService.execute(command);
    }

    @Override
    public void close() {
        boolean terminated = isTerminated();
        if (!terminated) {
            shutdown();
            boolean interrupted = false;
            while (!terminated) {
                try {
                    terminated = awaitTermination(1L, TimeUnit.DAYS);
                } catch (final InterruptedException e) {
                    if (!interrupted) {
                        shutdownNow();
                        interrupted = true;
                    }
                }
            }
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
