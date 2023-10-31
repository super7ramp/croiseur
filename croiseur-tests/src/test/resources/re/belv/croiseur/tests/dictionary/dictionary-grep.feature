# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: Search Dictionary Content

  The application offers a way to search for dictionary words using regular expression.

  Scenario: Search Dictionary Content - Prefix

    When user requests to search the entries of "General British English dictionary" provided by "Local XML Provider" matching the regular expression "SUN......"
    Then the application presents 31 dictionary entry matches, the first ones being:
      | SUNLIGHTS |
      | SUNFISHES |
      | SUNSEEKER |
      | SUNSHADES |
      | SUNNYNOOK |
      | SUNTANNED |
      | SUNFLOWER |
      | SUNDARAMS |

  Scenario: Search Dictionary Content - Suffix

    When user requests to search the entries of "General British English dictionary" provided by "Local XML Provider" matching the regular expression ".......ER"
    Then the application presents 1061 dictionary entry matches, the first ones being:
      | CHARTERER |
      | FREIGHTER |
      | PROCEEDER |
      | BOOTMAKER |
      | PUPPETEER |
      | WOMANLIER |
      | VOLTMETER |

  Scenario: Search Dictionary Content - Base

    When user requests to search the entries of "General British English dictionary" provided by "Local XML Provider" matching the regular expression "...FLOW.."
    Then the application presents the following dictionary entry matches:
      | SUNFLOWER |
      | MAYFLOWER |
      | SAFFLOWER |

  Scenario: Search Dictionary Content - Mixed

    When user requests to search the entries of "General British English dictionary" provided by "Local XML Provider" matching the regular expression ".U.FLOW.R"
    Then the application presents the following dictionary entry match:
      | SUNFLOWER |

  Scenario: Search Dictionary Content - Whole Word

    When user requests to search the entries of "General British English dictionary" provided by "Local XML Provider" matching the regular expression "MAYFLOWER"
    Then the application presents the following dictionary entry match:
      | MAYFLOWER |

  Scenario: Search Dictionary Content - No match

    When user requests to search the entries of "General British English dictionary" provided by "Local XML Provider" matching the regular expression ".I.FLOW.R"
    Then the application presents an empty dictionary search result
