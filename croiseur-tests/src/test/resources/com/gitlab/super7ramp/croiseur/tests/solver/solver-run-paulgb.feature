# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: Run Solver - Crossword Composer (paulgb)

  "Solvers", in croiseur jargon, are modules able to fill a crossword grid. Some call them
  "fillers". The application allows user to fill crossword grid using these solvers. That is the
  main usecase of the application.

  Here it is tested basic solving capabilities using paulgb's Crossword Composer solver.

  Scenario: Run Solver - Crossword Composer (paulgb) - Simple

    When user requests to solve the following grid with "Crossword Composer" solver:
      |  |  |  |
      |  |  |  |
      |  |  |  |
    Then the application presents the following successful solver result:
      | G | A | D |
      | L | B | J |
      | C | M | D |

  Scenario: Run Solver - Crossword Composer (paulgb) - With Specific Dictionary

    When user requests to solve the following grid with "Crossword Composer" solver and with "The UK Advanced Cryptics Dictionary" provided by "Local Text Provider":
      |  |  |  |
      |  |  |  |
      |  |  |  |
    Then the application presents the following successful solver result:
      | M | A | A |
      | N | I | B |
      | A | D | A |

  Scenario: Run Solver - Crossword Composer (paulgb) - With Shaded Cell

    When user requests to solve the following grid with "Crossword Composer" solver:
      |  |  |   |
      |  |  | # |
      |  |  |   |
    Then the application presents the following successful solver result:
      | F | D | A |
      | O | J | # |
      | Y | D | S |

  Scenario: Run Solver - Crossword Composer (paulgb) - With Prefilled Cell

  Crossword Composer solver does not support pre-filled grids.

    When user requests to solve the following grid with "Crossword Composer" solver:
      | A |  |  |
      | C |  |  |
      | T |  |  |
    Then the application presents the grid as impossible to solve

  Scenario: Run Solver - Crossword Composer (paulgb) - With Prefilled and Shaded Cells

  Crossword Composer solver does not support pre-filled grids.

    When user requests to solve the following grid with "Crossword Composer" solver:
      | A |   |   |
      | C | # |   |
      | T |   | # |
    Then the application presents the grid as impossible to solve

  Scenario: Run Solver - Crossword Composer (paulgb) - With Randomness

  It is the same grid that the first scenario "Simple" but the dictionary is shuffled. This leads
  to a different solution found.

    When user requests to solve the following grid with "Crossword Composer" solver and with dictionary shuffled using a seed of 42:
      |  |  |  |
      |  |  |  |
      |  |  |  |
    Then the application presents the following successful solver result:
      | A | M | T |
      | B | E | V |
      | S | E | A |
