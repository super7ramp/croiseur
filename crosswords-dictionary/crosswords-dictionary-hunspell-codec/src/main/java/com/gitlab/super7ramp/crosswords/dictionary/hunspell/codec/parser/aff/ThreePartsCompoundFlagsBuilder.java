/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.aff.ThreePartsCompoundFlags;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.common.Flag;

import java.util.Objects;
import java.util.Optional;

/**
 * Builder for {@link ThreePartsCompoundFlags}.
 */
final class ThreePartsCompoundFlagsBuilder {

    /** The value of the {@code COMPOUNDBEGIN} flag, or {@code null} if no value set. */
    private Flag begin;

    /** The value of the {@code COMPOUNDMIDDLE} flag, or {@code null} if no value set. */
    private Flag middle;

    /** The value of the {@code COMPOUNEND} flag, or {@code null} if no value set. */
    private Flag end;

    /**
     * Constructs an instance.
     */
    ThreePartsCompoundFlagsBuilder() {
        // flags are left null
    }

    /**
     * Sets the {@code COMPOUNDBEGIN} flag value.
     *
     * @param beginArg the value
     * @return this builder
     * @throws IllegalStateException if flag has already been set
     */
    ThreePartsCompoundFlagsBuilder setBeginFlag(final Flag beginArg) {
        if (begin != null) {
            throw new IllegalStateException("Illegal attempt to set COMPOUNDBEGIN value a second " +
                    "time");
        }
        begin = Objects.requireNonNull(beginArg);
        return this;
    }

    /**
     * Sets the {@code COMPOUNDMIDDLE} flag value.
     *
     * @param middleArg the value
     * @return this builder
     * @throws IllegalStateException if flag has already been set
     */
    ThreePartsCompoundFlagsBuilder setMiddleFlag(final Flag middleArg) {
        if (middle != null) {
            throw new IllegalStateException("Illegal attempt to set COMPOUNDMIDDLE value a second" +
                    " time");
        }
        middle = Objects.requireNonNull(middleArg);
        return this;
    }

    /**
     * Sets the {@code COMPOUNDEND} flag value.
     *
     * @param endArg the value
     * @return this builder
     * @throws IllegalStateException if flag has already been set
     */
    ThreePartsCompoundFlagsBuilder setEndFlag(final Flag endArg) {
        if (end != null) {
            throw new IllegalStateException("Illegal attempt to set COMPOUNDEND value a second" +
                    " time");
        }
        end = Objects.requireNonNull(endArg);
        return this;
    }

    /**
     * Builds a {@link ThreePartsCompoundFlags}.
     *
     * @return a {@link ThreePartsCompoundFlags}, unless no flag have been set, in which case
     * {@link Optional#empty()} is returned.
     * @throws IllegalArgumentException if flags are not either all {@code null} or all
     *                                  non-{@code null}
     */
    Optional<ThreePartsCompoundFlags> build() {
        if (begin == null) {
            if (middle != null) {
                throw new IllegalArgumentException("COMPOUNDBEGIN not set but COMPOUNDMIDDLE is");
            }
            if (end != null) {
                throw new IllegalArgumentException("COMPOUNDBEGIN not set but not COMPOUNDEND is");
            }
            return Optional.empty();
        }
        return Optional.of(new ThreePartsCompoundFlags(begin, middle, end));
    }
}
