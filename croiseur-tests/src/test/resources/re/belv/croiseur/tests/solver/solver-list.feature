# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: List Solvers

  "Solvers", in croiseur jargon, are modules able to fill a crossword grid. Some call them
  "fillers". The application allows user to list the installed solvers.

  Scenario: List Solver - Default Deployment

    When user requests to list the available solvers
    Then the application presents the following solvers:
      | Name               | Description                                                                                                                                     |
      | Ginsberg           | A crossword solver based on Ginsberg's papers.                                                                                                  |
      | Crossword Composer | The solver powering the Crossword Composer software. Does not support pre-filled grids.                                                         |
      | SAT                | A crossword solver based on Sat4j default pseudo-boolean solver. Slow and memory intensive.                                                     |
      | XWords RS          | The solver powering the XWords RS tool.                                                                                                         |

  @no-auto-deploy
  Scenario: List Solver - No solver installed

  This scenario tests the output of the application when no solver is installed.

    Given an application deployed without solver
    When user requests to list the available solvers
    Then the application presents the solver service error "No solver found"
