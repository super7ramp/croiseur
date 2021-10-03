package main.java.com.gitlab.super7ramp.crosswords.util.function;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Some partial applications functions.
 *
 * See <a href="https://en.wikipedia.org/wiki/Partial_application">Partial Applications on Wikipedia</a>
 */
public final class PartialApplications {

    /**
     * Private constructor, static methods only.
     */
    private PartialApplications() {
        // Nothing to do.
    }

    /**
     * Partial application on a {@link BiPredicate}.
     *
     * @param biPredicate the bi-predicate
     * @param left the value of the first argument of the bi-predicate
     * @param <L> type of the first argument - aka left - of the bi-predicate
     * @param <R> type of the second argument - aka right - of the bi-predicate
     * @return the corresponding {@link Predicate}
     */
    public static <L, R> Predicate<R> partialApply(BiPredicate<L, R> biPredicate, L left) {
        return right -> biPredicate.test(left, right);
    }

}
