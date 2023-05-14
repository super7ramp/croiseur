/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.tests.dictionary;

import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.croiseur.api.dictionary.ListDictionariesRequest;
import com.gitlab.super7ramp.croiseur.api.dictionary.ListDictionaryEntriesRequest;
import com.gitlab.super7ramp.croiseur.api.dictionary.SearchDictionaryEntriesRequest;
import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryProviderDetails;
import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDetails;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryPresenter;
import com.gitlab.super7ramp.croiseur.tests.context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.gitlab.super7ramp.croiseur.tests.dictionary.DictionaryMatchers.dictionaryContentWith;
import static com.gitlab.super7ramp.croiseur.tests.dictionary.DictionaryMatchers.searchResultWith;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Steps pertaining to the {@link DictionaryService}.
 */
public final class DictionarySteps {

    /** The dictionary service. */
    private final DictionaryService dictionaryService;

    /** The mocked presenter. */
    private final DictionaryPresenter presenterMock;

    /**
     * Constructs an instance.
     *
     * @param testContext the test context
     */
    public DictionarySteps(final TestContext testContext) {
        dictionaryService = testContext.dictionaryService();
        presenterMock = testContext.presenterMock();
    }

    @When("user requests to display the entries of {string} provided by {string}")
    public void whenDisplayEntries(final String dictionary, final String dictionaryProvider) {
        final DictionaryIdentifier dictionaryIdentifier =
                new DictionaryIdentifier(dictionaryProvider, dictionary);
        final ListDictionaryEntriesRequest listDictionaryEntriesRequest =
                ListDictionaryEntriesRequest.of(dictionaryIdentifier);
        dictionaryService.listEntries(listDictionaryEntriesRequest);
    }

    @When("user requests to search the entries of {string} provided by {string} matching the " +
            "regular expression {string}")
    public void whenSearch(final String dictionary, final String dictionaryProvider,
                           final String regex) {
        final DictionaryIdentifier dictionaryIdentifier =
                new DictionaryIdentifier(dictionaryProvider, dictionary);
        final SearchDictionaryEntriesRequest searchDictionaryEntriesRequest =
                SearchDictionaryEntriesRequest.of(dictionaryIdentifier, regex);
        dictionaryService.searchEntries(searchDictionaryEntriesRequest);
    }

    @When("user requests to list the available dictionary providers")
    public void whenListDictionaryProviders() {
        dictionaryService.listProviders();
    }

    @When("^user requests to list the available dictionaries(?: of locale ([^ ]+))?(?: provided " + "by (.+))?$")
    public void whenListDictionariesOfLocaleProvidedBy(final String locale, final String provider) {
        final Locale parsedLocale = locale != null ? Locale.forLanguageTag(locale) : null;
        final ListDictionariesRequest listDictionariesRequest =
                ListDictionariesRequest.of(parsedLocale, provider);
        dictionaryService.listDictionaries(listDictionariesRequest);
    }

    @Then("the application presents {int} dictionary entries, the first ones being:")
    public void thenPresentDictionaryEntries(final int totalNumberOfEntries,
                                             final List<String> firstEntries) {
        verify(presenterMock).presentDictionaryEntries(dictionaryContentWith(totalNumberOfEntries
                , firstEntries));
    }

    @Then("the application presents {int} dictionary entry matches, the first ones being:")
    public void thenPresentSearchResult(final int totalNumberOfMatches,
                                        final List<String> firstMatches) {
        verify(presenterMock).presentDictionarySearchResult(searchResultWith(totalNumberOfMatches
                , firstMatches));
    }

    @Then("the application presents the following dictionary entry match(es):")
    public void thenPresentSearchResult(final List<String> words) {
        thenPresentSearchResult(words.size(), words);
    }

    @Then("the application presents an empty dictionary search result")
    public void thenPresentSearchResult() {
        thenPresentSearchResult(Collections.emptyList());
    }

    @Then("the application presents the following dictionaries:")
    public void thenPresentDictionaries(final List<ProvidedDictionaryDetails> dictionaries) {
        verify(presenterMock).presentDictionaries(eq(dictionaries));
    }

    @Then("the application presents the following dictionary providers:")
    public void thenPresentDictionaryProviders(
            final List<DictionaryProviderDetails> dictionaryProviders) {
        verify(presenterMock).presentDictionaryProviders(eq(dictionaryProviders));
    }

    @Then("the application presents the dictionary error {string}")
    public void thenPresentError(final String error) {
        verify(presenterMock).presentDictionaryError(eq(error));
    }

}
