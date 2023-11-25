/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.web.concurrent;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * A {@link Runnable} wrapper providing, at execution time, access to the request scope defined at
 * creation time
 * <p>
 * Useful when:
 * <ul>
 *     <li>A request starts a new asynchronous task, and
 *     <li>Request waits for the task to be started before responding, and
 *     <li>Task start notification is performed via the task thread
 * </ul>
 */
public final class RequestAwareRunnable implements Runnable {

    /** The actual task. */
    private final Runnable actual;

    /** The request attributes. */
    private final RequestAttributes requestAttributes;

    /**
     * Constructs an instance.
     *
     * @param actualArg            the actual task
     * @param requestAttributesArg the request attributes
     */
    private RequestAwareRunnable(final Runnable actualArg,
                                 final RequestAttributes requestAttributesArg) {
        actual = actualArg;
        requestAttributes = requestAttributesArg;
    }

    /**
     * Wraps the given {@link Runnable}.
     *
     * @param actual the actual runnable
     * @return a new RequestAwareRunnable wrapping the given runnable and using current request
     * attributes, if any
     */
    public static RequestAwareRunnable wrap(final Runnable actual) {
        return new RequestAwareRunnable(actual, RequestContextHolder.currentRequestAttributes());
    }

    @Override
    public void run() {
        if (requestAttributes != null) {
            RequestContextHolder.setRequestAttributes(requestAttributes);
        }
        try {
            actual.run();
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }
}
