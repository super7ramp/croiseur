# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: List Dictionaries

  Word lists - or "dictionaries" in croiseur jargon - used for crossword generation are provided
  via modules called "dictionary providers". The application offers a way to list the
  dictionaries provided by these providers.

  Scenario: List Dictionaries - No filter

    When user requests to list the available dictionaries
    Then the application presents the following dictionaries:
      | Provider                | Name                                | Locale |
      | Local Hunspell Provider | Hunspell Dictionary de_DE.dic       | de-DE  |
      | Local XML Provider      | General German dictionary           | de-DE  |
      | Local Hunspell Provider | Hunspell Dictionary en_GB.dic       | en-GB  |
      | Local Text Provider     | The UK Advanced Cryptics Dictionary | en-GB  |
      | Local XML Provider      | General British English dictionary  | en-GB  |
      | Local Hunspell Provider | Hunspell Dictionary es_ES.dic       | es-ES  |
      | Local XML Provider      | General Spanish dictionary          | es-ES  |
      | Local Hunspell Provider | Hunspell Dictionary fr.dic          | fr     |
      | Local XML Provider      | General French dictionary           | fr-FR  |
      | Local Hunspell Provider | Hunspell Dictionary it_IT.dic       | it-IT  |
      | Local XML Provider      | General Italian Dictionary          | it-IT  |

  Scenario: List Dictionaries - Filter by Locale
    When user requests to list the available dictionaries of locale en-GB
    Then the application presents the following dictionaries:
      | Provider                | Name                                | Locale |
      | Local Hunspell Provider | Hunspell Dictionary en_GB.dic       | en-GB  |
      | Local Text Provider     | The UK Advanced Cryptics Dictionary | en-GB  |
      | Local XML Provider      | General British English dictionary  | en-GB  |

  Scenario: List Dictionaries - Filter by Provider
    When user requests to list the available dictionaries provided by Local Text Provider
    Then the application presents the following dictionaries:
      | Provider            | Name                                | Locale |
      | Local Text Provider | The UK Advanced Cryptics Dictionary | en-GB  |

  Scenario: List Dictionaries - Filter by Locale and Provider
    When user requests to list the available dictionaries of locale en-GB provided by Local XML Provider
    Then the application presents the following dictionaries:
      | Provider           | Name                               | Locale |
      | Local XML Provider | General British English dictionary | en-GB  |

  @no-auto-deploy
  Scenario: List Dictionaries - No Dictionary Provider

  This scenario tests the output of the application when no dictionary provider is installed.

    Given an application deployed without dictionary provider
    When user requests to list the available dictionaries
    Then the application presents the dictionary error "No dictionary found"
