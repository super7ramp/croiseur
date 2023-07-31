<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## croiseur-clue-openai-plugin

`croiseur-clue-openai-plugin` is a [clue provider](../../croiseur-spi/croiseur-spi-clue) based
on OpenAI's ChatGPT.

This plugin requires a network access: It makes requests to OpenAI's web API.

Only words to define are sent through the API; no personal data is sent.

In order for the plugin to work, a valid OpenAI API access key must be provided. The plugin reads
the access key from the environment variable `OPENAI_API_KEY`. Refer
to the [OpenAI documentation](https://platform.openai.com/docs/api-reference/authentication) for how
to create an access key.
