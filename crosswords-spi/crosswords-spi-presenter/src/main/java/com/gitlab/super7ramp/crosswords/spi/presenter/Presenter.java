/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.spi.presenter;

import com.gitlab.super7ramp.crosswords.spi.presenter.dictionary.DictionaryPresenter;
import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverPresenter;

/**
 * Required presentation services.
 */
public interface Presenter extends DictionaryPresenter, SolverPresenter {
    // Marker interface
}
