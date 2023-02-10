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
      | C | A | A |
      | I | D | A |
      | A | D | A |

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
      | B | A | A |
      | E | A | # |
      | A | A | C |

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
