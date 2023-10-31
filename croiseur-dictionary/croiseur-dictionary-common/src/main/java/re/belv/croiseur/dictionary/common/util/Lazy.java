/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.common.util;

import java.lang.ref.SoftReference;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A lazily evaluated value.
 * <p>
 * Difference with other Lazy implementations such as Vavr's is that retrieved value is stored in a
 * {@link SoftReference}, allowing the value to be garbage-collected under memory pressure, if no
 * strong reference on the value exists outside the Lazy instance.
 * <p>
 * This means that the value may be evaluated more than once, and thus that the given value supplier
 * will not be eligible to garbage-collection before the Lazy object is.
 * <p>
 * This class is not thread-safe. Internal synchronisation should be added in the future if
 * thread-safety becomes a concern.
 *
 * @param <T> the value type
 * @see <a href="https://github.com/vavr-io/vavr/blob/master/src/main/java/io/vavr/Lazy.java">Vavr's
 * Lazy</a>
 */
public final class Lazy<T> implements Supplier<T> {

    /** The value supplier. */
    private final Supplier<T> supplier;

    /** The cached soft reference; {@code null} until value is retrieved for the first time. */
    private SoftReference<T> cached;

    /**
     * Constructs an instance.
     *
     * @param supplierArg the supplier
     */
    private Lazy(final Supplier<T> supplierArg) {
        supplier = Objects.requireNonNull(supplierArg);
        cached = null;
    }

    /**
     * Creates a new {@link Lazy} value.
     *
     * @param supplierArg the value supplier
     * @param <T>         the value type
     * @return a new {@link Lazy} value
     */
    public static <T> Lazy<T> of(final Supplier<T> supplierArg) {
        return new Lazy<>(supplierArg);
    }

    @Override
    public T get() {
        final T cachedValue = cachedValue();
        return cachedValue != null ? cachedValue : retrieveValue();
    }

    /**
     * Retrieves the cached value.
     *
     * @return the cached value; {@code null} if value has never been retrieved or has been
     * garbage-collected since last retrieval
     */
    private T cachedValue() {
        return cached != null ? cached.get() : null;
    }

    /**
     * Retrieves and caches the value.
     *
     * @return the retrieved value
     */
    private T retrieveValue() {
        final T value = supplier.get();
        cached = new SoftReference<>(value);
        return value;
    }
}
