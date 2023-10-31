/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.presenter;

import re.belv.croiseur.cli.l10n.ResourceBundles;
import re.belv.croiseur.cli.status.Status;
import re.belv.croiseur.spi.presenter.Presenter;
import re.belv.croiseur.spi.presenter.clue.CluePresenter;
import re.belv.croiseur.spi.presenter.clue.ClueProviderDescription;

import java.util.List;
import java.util.Map;

import static re.belv.croiseur.cli.presenter.CliPresenterUtil.lineOf;

/**
 * CLI implementation of {@link Presenter}.
 */
final class CliCluePresenter implements CluePresenter {

    /** Providers output format. */
    private static final String PROVIDERS_FORMAT = "%-16s\t%-54s%n";

    /** Clue presentation format. */
    private static final String CLUE_FORMAT = $("clue-format");

    /**
     * Constructs an instance.
     */
    CliCluePresenter() {
        // Nothing to do.
    }

    @Override
    public void presentClueError(final String error) {
        System.err.println(error);
        Status.setGeneralApplicativeError();
    }

    @Override
    public void presentClueProviders(final List<ClueProviderDescription> clueProviderDescriptions) {
        final String providerHeader = $("name");
        final String descriptionHeader = $("description");

        System.out.printf(PROVIDERS_FORMAT, providerHeader, descriptionHeader);
        System.out.printf(PROVIDERS_FORMAT, lineOf(providerHeader.length()),
                          lineOf(descriptionHeader.length()));

        clueProviderDescriptions.forEach(
                provider -> System.out.printf(PROVIDERS_FORMAT, provider.name(),
                                              provider.description()));
    }

    @Override
    public void presentClues(final Map<String, String> clues) {
        for (final Map.Entry<String, String> clue : clues.entrySet()) {
            System.out.printf(CLUE_FORMAT, clue.getKey(), clue.getValue());
        }
    }

    /**
     * Returns the localised message with given key.
     *
     * @param key the message key
     * @return the localised message
     */
    private static String $(final String key) {
        return ResourceBundles.$("presenter.clue." + key);
    }
}
