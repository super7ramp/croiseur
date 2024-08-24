/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.spi.presenter;

import re.belv.croiseur.spi.presenter.clue.CluePresenter;
import re.belv.croiseur.spi.presenter.dictionary.DictionaryPresenter;
import re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import re.belv.croiseur.spi.presenter.solver.SolverPresenter;

/**
 * Required presentation services.
 *
 * <p>A marker interface: Relevant methods to implement are in {@link DictionaryPresenter}, {@link CluePresenter},
 * {@link PuzzlePresenter} and {@link SolverPresenter}.
 */
public interface Presenter extends DictionaryPresenter, CluePresenter, PuzzlePresenter, SolverPresenter {

    /**
     * Convenience method to construct a presenter aggregating several presenters.
     *
     * <p>Calls to the created presenter will be broadcast to all presenters passed at construction.
     *
     * @param presenters the presenters
     * @return a new {@link Presenter} broadcasting calls it receives to all presenters given as arguments
     * @throws NullPointerException if given presenters iterable is {@code null}
     */
    static Presenter broadcastingTo(final Iterable<? extends Presenter> presenters) {
        return new BroadcastingPresenter(presenters);
    }
}
