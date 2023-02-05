/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gitlab.super7ramp.croiseur.clue.openai.plugin;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Clue generator backed by OpenAI API.
 */
final class ClueGenerator {

    /** Access to the OpenAI API. */
    private final OpenAiService openAiService;

    /** The model configuration. */
    private final ModelConfiguration config;

    /** The prompt format. */
    private final String promptFormat;

    /**
     * Constructs an instance.
     */
    public ClueGenerator() {
        final String token = System.getenv("OPENAI_TOKEN");
        openAiService = new OpenAiService(token);
        config = new ModelConfiguration();
        promptFormat = ResourceBundle.getBundle("com.gitlab.super7ramp.croiseur.clue.openai" +
                ".plugin.Prompt").getString("prompt");
    }

    /**
     * Extracts the clues from the generated completion.
     *
     * @param words      the input words
     * @param completion the completion
     * @return the clues extracted from the completion
     */
    private static Map<String, String> extractClues(final List<String> words,
                                                    final List<CompletionChoice> completion) {
        if (completion.isEmpty()) {
            return Collections.emptyMap();
        }
        final String[] definitions = completion.get(0).getText().split(System.lineSeparator());
        if (definitions.length != words.size()) {
            return Collections.emptyMap();
        }
        final Map<String, String> clues = new HashMap<>();
        for (int i = 0; i < words.size(); i++) {
            final String word = words.get(i);
            final String definitionPayload = definitions[i].replace(word + ":", "");
            clues.put(word, definitionPayload);
        }
        return clues;
    }

    /**
     * Generates crossword clue using OpenAI API.
     *
     * @param words the words to define
     * @return the generated clues, indexed by the defined words
     */
    public Map<String, String> generate(final List<String> words) {
        final CompletionRequest completionRequest = createRequest(words);
        final List<CompletionChoice> choices =
                openAiService.createCompletion(completionRequest).getChoices();
        return extractClues(words, choices);
    }

    private CompletionRequest createRequest(final List<String> words) {
        final String prompt = createPrompt(words);
        return CompletionRequest.builder()
                                .prompt(prompt)
                                .model(config.model())
                                .frequencyPenalty(config.frequencyPenalty())
                                .temperature(config.temperature())
                                .build();
    }

    private String createPrompt(final List<String> words) {
        final StringBuilder promptArgumentBuilder = new StringBuilder();
        for (final String otherWord : words) {
            promptArgumentBuilder.append(otherWord).append(':');
            promptArgumentBuilder.append(System.lineSeparator());
        }
        final String promptArgument = promptArgumentBuilder.toString();
        return String.format(promptFormat, promptArgument);
    }
}
