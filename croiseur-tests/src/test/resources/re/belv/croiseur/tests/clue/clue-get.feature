# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: Get Clues

  Clue providers are modules which provide clues for crossword entries. The application offers to
  user the possibility to generate clues for words, independently from crossword generation.

  Scenario: Get Clues - Dummy

  No clue provider is available yet, hence an error message is printed.

    When user requests clues for the following words:
      | HELLO     |
      | CROSSWORD |
    Then the application presents the clue service error "No clue provider found"
