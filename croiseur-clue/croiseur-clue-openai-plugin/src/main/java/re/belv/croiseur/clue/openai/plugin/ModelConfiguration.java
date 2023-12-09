/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.clue.openai.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Model configuration.
 */
final class ModelConfiguration {

    /** The properties holding the configuration. */
    private final Properties properties;

    /**
     * Constructs an instance.
     *
     * @throws ModelConfigurationException if configuration is not found or is missing mandatory
     *                                     model name
     */
    ModelConfiguration() {
        properties = new Properties();
        try (final InputStream is = ModelConfiguration.class.getResourceAsStream("config" +
                                                                                 ".properties")) {
            if (is == null) {
                throw new ModelConfigurationException("Missing plugin configuration");
            }
            properties.load(is);
            if (!properties.containsKey("model")) {
                throw new ModelConfigurationException(
                        "Incomplete plugin configuration: Field 'model' is mandatory");
            }
        } catch (final IOException e) {
            throw new ModelConfigurationException("Failed to read plugin configuration", e);
        }
    }

    /**
     * Returns the model name.
     *
     * @return the model name
     * @see <a
     * href="https://platform.openai.com/docs/api-reference/completions/create#completions/create-model">OpenAI
     * API Reference</a>
     */
    String model() {
        return properties.getProperty("model");
    }

    /**
     * Returns the frequency penalty.
     * <p>
     * Within [-2.0, 2.0]. The higher the value is, the more random the completion will be.
     *
     * @return the frequency penalty
     * @see <a
     * href="https://platform.openai.com/docs/api-reference/completions/create#completions/create-frequency_penalty">OpenAI
     * API Reference</a>
     */
    double frequencyPenalty() {
        return doubleProperty("frequency_penalty", 0.0);
    }

    /**
     * Returns the temperature.
     * <p>
     * Within [0.0, 2.0]. The higher the value is, the lower chance for the completion to repeat the
     * prompt.
     *
     * @return the temperature
     * @see <a
     * href="https://platform.openai.com/docs/api-reference/completions/create#completions/create-temperature">OpenAI
     * API Reference</a>
     */
    double temperature() {
        return doubleProperty("temperature", 1.0);
    }

    private double doubleProperty(final String key, final double defaultValue) {
        final String stringValue = properties.getProperty(key);
        return stringValue != null ? Double.parseDouble(stringValue) : defaultValue;
    }
}
