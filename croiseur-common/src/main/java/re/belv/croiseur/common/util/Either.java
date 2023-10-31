/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.common.util;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A class of objects which can contain two forms of values: Either a Left value, or (exclusive) a
 * Right value.
 *
 * @param <L> the left form type
 * @param <R> the right form type
 * @see <a
 * href="https://github.com/vavr-io/vavr/blob/master/src/main/java/io/vavr/control/Either
 * .java">Vavr's
 * Either</a>
 */
public final class Either<L, R> {

    /** The left value, or {@code null} if right. */
    private final L left;

    /** The right value, or {@code null} if left. */
    private final R right;

    /**
     * Constructs an instance.
     *
     * @param leftArg  the left value
     * @param rightArg the right value
     */
    private Either(final L leftArg, final R rightArg) {
        left = leftArg;
        right = rightArg;
    }

    /**
     * Creates an instance containing a left value.
     *
     * @param left the left value
     * @param <L>  the left value type
     * @param <R>  the right value type
     * @return an instance containing a left value
     * @throws NullPointerException if left value is {@code null}
     */
    public static <L, R> Either<L, R> leftOf(final L left) {
        Objects.requireNonNull(left);
        return new Either<>(left, null);
    }

    /**
     * Creates an instance containing a right value.
     *
     * @param right the right value
     * @param <L>   the left value type
     * @param <R>   the right value type
     * @return an instance containing a right value
     * @throws NullPointerException if right value is {@code null}
     */
    public static <L, R> Either<L, R> rightOf(final R right) {
        Objects.requireNonNull(right);
        return new Either<>(null, right);
    }

    /**
     * Whether this instance contains a left value.
     *
     * @return {@code true} iff this instance contains a left value
     */
    public boolean isLeft() {
        return left != null;
    }

    /**
     * Whether this instance contains a right value.
     *
     * @return {@code true} iff this instance contains a right value
     */
    public boolean isRight() {
        return !isLeft();
    }

    /**
     * Returns the left value.
     *
     * @return the left value
     * @throws NoSuchElementException if not {@link #isLeft()}
     */
    public L left() {
        if (!isLeft()) {
            throw new NoSuchElementException();
        }
        return left;
    }

    /**
     * Returns the right value.
     *
     * @return the right value
     * @throws NoSuchElementException if not {@link #isRight()}
     */
    public R right() {
        if (!isRight()) {
            throw new NoSuchElementException();
        }
        return right;
    }
}
