# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: List Dictionaries

  Word lists - or "dictionaries" in croiseur jargon - used for crossword generation are provided
  via modules called "dictionary providers". The application offers a way to list the
  dictionaries provided by these providers.

  Scenario: List Dictionaries - No filter

    When user requests to list the available dictionaries
    Then the application presents the following dictionaries:
      | Provider                | Name                                | Locale | Description                                                                           |
      | Local Hunspell Provider | Hunspell Dictionary de_DE.dic       | de-DE  | Hunspell Dictionary for German (Germany)                                              |
      | Local XML Provider      | General German dictionary           | de-DE  | Dictionary adapted from LibreOffice German dictionary for a crossword usage.          |
      | Local Hunspell Provider | Hunspell Dictionary en_GB.dic       | en-GB  | Hunspell Dictionary for English (United Kingdom)                                      |
      | Local Text Provider     | The UK Advanced Cryptics Dictionary | en-GB  | The UKACD is a word list compiled for the crossword community.                        |
      | Local XML Provider      | General British English dictionary  | en-GB  | Dictionary adapted from LibreOffice British English dictionary for a crossword usage. |
      | Local Hunspell Provider | Hunspell Dictionary es_ES.dic       | es-ES  | Hunspell Dictionary for Spanish (Spain)                                               |
      | Local XML Provider      | General Spanish dictionary          | es-ES  | Dictionary adapted from LibreOffice Spanish dictionary for a crossword usage.         |
      | Local Hunspell Provider | Hunspell Dictionary fr.dic          | fr     | Hunspell Dictionary for French                                                        |
      | Local XML Provider      | General French dictionary           | fr-FR  | Dictionary adapted from LibreOffice French dictionary for a crossword usage.          |
      | Local Hunspell Provider | Hunspell Dictionary it_IT.dic       | it-IT  | Hunspell Dictionary for Italian (Italy)                                               |
      | Local XML Provider      | General Italian Dictionary          | it-IT  | Dictionary adapted from LibreOffice Italian dictionary for a crossword usage.         |

  Scenario: List Dictionaries - Filter by Locale
    When user requests to list the available dictionaries of locale en-GB
    Then the application presents the following dictionaries:
      | Provider                | Name                                | Locale | Description                                                                           |
      | Local Hunspell Provider | Hunspell Dictionary en_GB.dic       | en-GB  | Hunspell Dictionary for English (United Kingdom)                                      |
      | Local Text Provider     | The UK Advanced Cryptics Dictionary | en-GB  | The UKACD is a word list compiled for the crossword community.                        |
      | Local XML Provider      | General British English dictionary  | en-GB  | Dictionary adapted from LibreOffice British English dictionary for a crossword usage. |

  Scenario: List Dictionaries - Filter by Provider
    When user requests to list the available dictionaries provided by Local Text Provider
    Then the application presents the following dictionaries:
      | Provider            | Name                                | Locale | Description                                                    |
      | Local Text Provider | The UK Advanced Cryptics Dictionary | en-GB  | The UKACD is a word list compiled for the crossword community. |

  Scenario: List Dictionaries - Filter by Locale and Provider
    When user requests to list the available dictionaries of locale en-GB provided by Local XML Provider
    Then the application presents the following dictionaries:
      | Provider           | Name                               | Locale | Description                                                                           |
      | Local XML Provider | General British English dictionary | en-GB  | Dictionary adapted from LibreOffice British English dictionary for a crossword usage. |

  @no-auto-deploy
  Scenario: List Dictionaries - No Dictionary Provider

  This scenario tests the output of the application when no dictionary provider is installed.

    Given an application deployed without dictionary provider
    When user requests to list the available dictionaries
    Then the application presents the dictionary error "No dictionary found"
