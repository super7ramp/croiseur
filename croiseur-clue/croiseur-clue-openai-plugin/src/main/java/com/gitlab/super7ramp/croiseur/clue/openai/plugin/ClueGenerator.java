/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gitlab.super7ramp.croiseur.clue.openai.plugin;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Clue generator backed by OpenAI API.
 */
final class ClueGenerator {

    /** Name of the environment variable storing the OpenAI API access key. */
    private static final String OPENAI_API_KEY = "OPENAI_API_KEY";

    /** Access to the OpenAI API. */
    private final OpenAiService openAiService;

    /** The model configuration. */
    private final ModelConfiguration config;

    /** The system message. */
    private final String systemMessage;

    /** The user message header. */
    private final String userMessageHeader;

    /** The placeholder. */
    private final String placeholder;

    /**
     * Constructs an instance.
     */
    public ClueGenerator() {
        final String token = System.getenv(OPENAI_API_KEY);
        openAiService = new OpenAiService(token);
        config = new ModelConfiguration();
        final ResourceBundle rb = ResourceBundle.getBundle(
                "com.gitlab.super7ramp.croiseur.clue.openai.plugin.Prompt");
        systemMessage = rb.getString("system");
        userMessageHeader = rb.getString("user");
        placeholder = rb.getString("placeholder");
    }

    /**
     * Extracts the clues from the generated completion.
     *
     * @param words      the input words
     * @param completion the completion
     * @return the clues extracted from the completion
     */
    private static Map<String, String> extractClues(final List<String> words,
                                                    final List<ChatCompletionChoice> completion) {
        if (completion.isEmpty()) {
            return Collections.emptyMap();
        }
        final String[] definitions =
                completion.get(0).getMessage().getContent().split(System.lineSeparator());
        if (definitions.length != words.size()) {
            return Collections.emptyMap();
        }
        final Map<String, String> clues = new HashMap<>();
        for (int i = 0; i < words.size(); i++) {
            final String word = words.get(i);
            final String definitionPayload =
                    definitions[i].replace(word + ":", "").replace("\"", "");
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
        final ChatCompletionRequest completionRequest = createRequest(words);
        final List<ChatCompletionChoice> choices =
                openAiService.createChatCompletion(completionRequest).getChoices();
        return extractClues(words, choices);
    }

    private ChatCompletionRequest createRequest(final List<String> words) {
        final List<ChatMessage> prompt = createPrompt(words);
        return ChatCompletionRequest.builder()
                                    .messages(prompt)
                                    .model(config.model())
                                    .frequencyPenalty(config.frequencyPenalty())
                                    .temperature(config.temperature())
                                    .build();
    }

    /**
     * Creates prompt.
     * <p>
     * Prompt is like this:
     * <ul>
     *     <li>System: ${system_message}</li>
     *     <li>User: ${user_message_header}/li>
     *     <li>User:
     *         <ul>
     *             <li>${word1}:${placeholder}</li>
     *             <li>${word2}:${placeholder}</li>
     *            <li>...</li>
     *         </ul>
     *    </li>
     * </ul>
     * Example:
     * <pre>
     * System: You are Shakespeare
     * User: Define the following words, without naming them, in less than 5 words
     * User:
     *     hello:[insert text here]
     *     world:[insert text here]
     * </pre>
     *
     * @param words the words to define
     * @return the prompt to define the given words
     */
    private List<ChatMessage> createPrompt(final List<String> words) {
        final ChatMessage system = new ChatMessage("system", systemMessage);
        final ChatMessage userHeader = new ChatMessage("user", userMessageHeader);
        final StringBuilder userMessageBodyBuilder = new StringBuilder();
        for (final String word : words) {
            userMessageBodyBuilder.append(word).append(':').append(placeholder);
            userMessageBodyBuilder.append(System.lineSeparator());
        }
        final ChatMessage userBody = new ChatMessage("user",
                                                     userMessageBodyBuilder.toString());
        return List.of(system, userHeader, userBody);
    }
}
