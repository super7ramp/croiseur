/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.presenter;

import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.clue.CluePresenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.clue.ClueProviderDescription;

import java.util.List;
import java.util.Map;

import static com.gitlab.super7ramp.croiseur.cli.presenter.CliPresenterUtil.lineOf;

/**
 * CLI implementation of {@link Presenter}.
 */
final class CliCluePresenter implements CluePresenter {

    /** Providers output format. */
    private static final String PROVIDERS_FORMAT = "%-16s\t%-54s%n";

    /** Clue presentation format. */
    // TODO l10n (there may be space before ':')
    private static final String CLUE_FORMAT = "%s: %s\n";

    /**
     * Constructs an instance.
     */
    CliCluePresenter() {
        // Nothing to do.
    }

    @Override
    public void presentClueError(final String error) {
        System.err.println(error);
    }

    @Override
    public void presentClueProviders(final List<ClueProviderDescription> clueProviderDescriptions) {
        final String providerHeader = "Name"; // TODO l10n
        final String descriptionHeader = "Description"; // TODO l10n

        System.out.printf(PROVIDERS_FORMAT, providerHeader, descriptionHeader);
        System.out.printf(PROVIDERS_FORMAT, lineOf(providerHeader.length()),
                lineOf(descriptionHeader.length()));

        clueProviderDescriptions.forEach(provider -> System.out.printf(PROVIDERS_FORMAT,
                provider.name(),
                provider.description()));
    }

    @Override
    public void presentClues(final Map<String, String> clues) {
        for (final Map.Entry<String, String> clue : clues.entrySet()) {
            System.out.printf(CLUE_FORMAT, clue.getKey(), clue.getValue());
        }
    }
}
