# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: Run Solver - Run and Save

  Here is tested that, upon explicit request, the puzzle passed to solver run is saved to the puzzle
  repository. The save is made before the solver is actually called. If the solver run is
  successful, the saved puzzle is updated with the solver result.

  In the following scenarios:
  - $today is replaced by current local date.
  - $id is the puzzle identifier provided by the puzzle repository when puzzle is saved for the
    first time; It is captured at that time then used for further verifications.

  Scenario: Run Solver - Run and Save - Success

    When user requests to solve and save the following grid:
      |  |  |  |
      |  |  |  |
      |  |  |  |
    Then the application saves the following puzzle:
      | Id          | $id         |
      | Revision    | 1           |
      | Title       |             |
      | Author      |             |
      | Editor      |             |
      | Copyright   |             |
      | Date        | $today      |
      | Grid (rows) | ...,...,... |
    And the application presents the following successful solver result:
      | D | C | M |
      | E | P | A |
      | S | A | Y |
    And the application updates the saved puzzle:
      | Id          | $id         |
      | Revision    | 2           |
      | Title       |             |
      | Author      |             |
      | Editor      |             |
      | Copyright   |             |
      | Date        | $today      |
      | Grid (rows) | DCM,EPA,SAY |
