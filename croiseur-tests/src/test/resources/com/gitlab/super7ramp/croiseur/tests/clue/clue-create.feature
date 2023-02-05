# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: Create Clues

  Clue providers are modules which generates definitions for crossword entries. The application
  offers to user the possibility to generate definitions for words, independently from crossword
  generation.

  Scenario: Create Clues - Dummy

  No clue provider is available yet, hence an error message is printed.

    When user requests to give clues for the following words:
      | HELLO     |
      | CROSSWORD |
    Then the application presents the clue error "No clue provider found"
