/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.clue.shared;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import re.belv.croiseur.impl.clue.error.ClueErrorMessages;
import re.belv.croiseur.spi.clue.ClueProvider;
import re.belv.croiseur.spi.presenter.clue.CluePresenter;

/**
 * A wrapper around {@link ClueProvider} which handles common errors.
 *
 * <p>It basically allows to share clue-related behaviours across services.
 */
public final class SafeClueProvider {

    /** The clue providers. */
    private final Collection<ClueProvider> clueProviders;

    /** The clue presenter. */
    private final CluePresenter cluePresenter;

    /**
     * Constructs an instance.
     *
     * @param clueProvidersArg the clue providers
     * @param cluePresenterArg the clue presenter
     */
    public SafeClueProvider(final Collection<ClueProvider> clueProvidersArg, final CluePresenter cluePresenterArg) {
        clueProviders = clueProvidersArg;
        cluePresenter = cluePresenterArg;
    }

    /**
     * Safely get clues, using the first clue provider found.
     *
     * @param words the words to define
     * @return the clues, indexed by the defined words
     */
    public Map<String, String> getClues(final Set<String> words) {
        return getClues(null, words);
    }

    /**
     * Safely get clues, using the given provider.
     *
     * @param providerName the provider to use
     * @param words the words to define
     * @return the clues, indexed by the defined words
     */
    public Map<String, String> getClues(final String providerName, final Set<String> words) {
        final Optional<ClueProvider> selectedClueProvider = selectClueProvider(providerName);
        if (selectedClueProvider.isEmpty()) {
            cluePresenter.presentClueError(ClueErrorMessages.NO_CLUE_PROVIDER);
            return Collections.emptyMap();
        }
        final Map<String, String> clues = safeGetClues(selectedClueProvider.get(), words);
        if (clues.isEmpty()) {
            cluePresenter.presentClueError(ClueErrorMessages.NO_CLUE);
        }
        return clues;
    }

    /**
     * Selects the appropriate clue provider given the desired provider name.
     *
     * @param providerName the desired provider name, or {@code null} if no preference
     * @return the appropriate clue provider, if found, otherwise {@link Optional#empty()}
     */
    private Optional<ClueProvider> selectClueProvider(final String providerName) {
        return clueProviders.stream()
                .filter(clueProvider ->
                        providerName == null || clueProvider.name().equals(providerName))
                .findFirst();
    }

    /**
     * Calls the clue provider, handling the potential exceptions.
     *
     * @param clueProvider the clue provider
     * @param words the words for which to get clues
     * @return the clues indexed by the defined words
     */
    private Map<String, String> safeGetClues(final ClueProvider clueProvider, final Set<String> words) {
        try {
            return clueProvider.define(words);
        } catch (final Exception e) {
            /*
             * Present exception message, even for runtime exceptions: Exception comes from only one
             * clue provider plugin, it should not stop the whole application or print a stacktrace.
             */
            final String error =
                    String.format(ClueErrorMessages.CLUE_PROVIDER_FAILED, clueProvider.name(), e.getMessage());
            cluePresenter.presentClueError(error);
            return Collections.emptyMap();
        }
    }
}
