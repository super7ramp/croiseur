# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: List Clue Providers

  Clue providers are modules which generates definitions for crossword entries. The application
  offers a way to list the installed clue providers.

  Scenario: List Clue Providers - Dummy

  No clue provider is available yet, hence an error message is printed.

    When user requests to list the available clue providers
    Then the application presents the clue service error "No clue provider found"
