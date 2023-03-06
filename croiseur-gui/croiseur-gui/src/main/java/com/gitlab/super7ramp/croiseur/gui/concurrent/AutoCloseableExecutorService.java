/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * An {@link ExecutorService} which is also {@link AutoCloseable}.
 * <p>
 * Backport of Java 19.
 */
public interface AutoCloseableExecutorService extends ExecutorService, AutoCloseable {

    /**
     * Initiates an orderly shutdown in which previously submitted tasks are
     * executed, but no new tasks will be accepted. This method waits until all
     * tasks have completed execution and the executor has terminated.
     *
     * <p> If interrupted while waiting, this method stops all executing tasks as
     * if by invoking {@link #shutdownNow()}. It then continues to wait until all
     * actively executing tasks have completed. Tasks that were awaiting
     * execution are not executed. The interrupt status will be re-asserted
     * before this method returns.
     *
     * <p> If already terminated, invoking this method has no effect.
     *
     * @throws SecurityException if a security manager exists and
     *                           shutting down this ExecutorService may manipulate
     *                           threads that the caller is not permitted to modify
     *                           because it does not hold {@link
     *                           java.lang.RuntimePermission}{@code ("modifyThread")},
     *                           or the security manager's {@code checkAccess} method
     *                           denies access.
     * @implSpec The default implementation invokes {@code shutdown()} and waits for tasks
     * to complete execution with {@code awaitTermination}.
     */
    @Override
    default void close() {
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
