# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: Get Default Dictionary

  The application offers a way to display the default dictionary, which is the dictionary used by
  solver when none is explicitly requested.

  The default dictionary is selected using the following criteria, sorted by priority:

  - Locale: Dictionary matching system's locale (language + country) is preferred over one which
  doesn't;
  - Language: Dictionary matching system's language is preferred over one which doesn't;
  - Fallback language: Dictionary matching English language is preferred over one which doesn't;
  - Provider: Dictionary provided by "Local XML Provider" is preferred over one of an other provider;

  Scenario: Get Default Dictionary - Prefer locale-matching dictionary
    Given system locale is fr-FR
    When user requests to show the default dictionary
    Then application presents the following default dictionary:
      | Provider           | Name                          | Locale | Description                                                                                             |
      | Local XML Provider | Dictionnaire français général | fr-FR  | Dictionnaire adapté du dictionnaire français de LibreOffice pour une utilisation dans des mots-croisés. |

  Scenario: Get Default Dictionary - Prefer language-matching dictionary
    Given system locale is fr-BE
    When user requests to show the default dictionary
    Then application presents the following default dictionary:
    # FIXME name and descriptions should be in French
      | Provider           | Name                      | Locale | Description                                                                  |
      | Local XML Provider | General French dictionary | fr-FR  | Dictionary adapted from LibreOffice French dictionary for a crossword usage. |

  Scenario: Get Default Dictionary - Prefer Local XML Provider

  There are two dictionaries in British English, one provided by "Local Text Provider", the other
  by "Local XML Provider". The latter is preferred.

    Given system locale is en-GB
    When user requests to show the default dictionary
    Then application presents the following default dictionary:
      | Provider           | Name                               | Locale | Description                                                                           |
      | Local XML Provider | General British English dictionary | en-GB  | Dictionary adapted from LibreOffice British English dictionary for a crossword usage. |

  Scenario: Get Default Dictionary - Fallback to English

  There is no dictionary with locale "jp", fallback to English.

    Given system locale is jp
    When user requests to show the default dictionary
    Then application presents the following default dictionary:
      | Provider           | Name                               | Locale | Description                                                                           |
      | Local XML Provider | General British English dictionary | en-GB  | Dictionary adapted from LibreOffice British English dictionary for a crossword usage. |
