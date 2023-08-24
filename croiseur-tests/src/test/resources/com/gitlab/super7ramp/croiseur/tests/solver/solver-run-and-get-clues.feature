# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: Run Solver - Run and Get Clues

  Application offers a way to solve a given grid and get clues for the words of the solution, in a
  single request.

  Scenario: Run Solver - Run and Get Clues - Error - No Clue Provider

    When user requests to solve and get clues for the following grid:
      |  |  |  |
      |  |  |  |
      |  |  |  |
    And the application presents the following successful solver result:
      | D | C | M |
      | E | P | A |
      | S | A | Y |
    And the application presents the clue service error "No clue provider found"