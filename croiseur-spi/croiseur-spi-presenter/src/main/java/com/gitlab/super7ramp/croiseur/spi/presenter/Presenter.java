/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.presenter;

import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryPresenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter;

/**
 * Required presentation services.
 */
public interface Presenter extends DictionaryPresenter, SolverPresenter {
    // Marker interface
}
