/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.common.util;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A lazily evaluated value.
 * <p>
 * This class is not thread-safe. Internal synchronisation should be added in the future if
 * thread-safety becomes a concern.
 *
 * @param <T> the value type
 * @see <a href="https://github.com/vavr-io/vavr/blob/master/src/main/java/io/vavr/Lazy.java">Vavr's Lazy</a>
 */
public final class Lazy<T> implements Supplier<T> {

    /** The value; {@code null} until it is lazily retrieved. */
    private T cached;

    /** The value supplier; {@code null} once the value has been retrieved. */
    private Supplier<T> supplier;

    /**
     * Constructs an instance.
     *
     * @param supplierArg the supplier
     */
    private Lazy(final Supplier<T> supplierArg) {
        supplier = Objects.requireNonNull(supplierArg);
    }

    /**
     * Creates a new {@link Lazy} value.
     *
     * @param supplierArg the value supplier
     * @return a new {@link Lazy} value
     * @param <T> the value type
     */
    public static <T> Lazy<T> of(final Supplier<T> supplierArg) {
        return new Lazy<>(supplierArg);
    }

    @Override
    public T get() {
        return cached != null ? cached : computeValue();
    }

    /**
     * Computes and cache the value.
     *
     * @return the computed value
     */
    private T computeValue() {
        cached = supplier.get();
        supplier = null;
        return cached;
    }
}
