# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: Load Puzzle

  The application manages its own collection of puzzles, stored in a puzzle repository. It allows
  users to store their work so they can be later retrieved.

  The application provides a way to load a puzzle of this puzzle repository to present it to user.

  Scenario: Load Puzzle - Nominal Case
    Given the puzzle repository contains:
      | Id | Revision | Title | Author   | Editor | Copyright | Date       | Grid (rows) |
      | 1  | 1        |       | Jane Doe |        |           | 2023-06-16 | ABC,DEF,GHI |
    When user requests to load puzzle with id 1
    Then the application presents the following loaded puzzle:
      | Title       |             |
      | Author      | Jane Doe    |
      | Editor      |             |
      | Copyright   |             |
      | Date        | 2023-06-16  |
      | Grid (rows) | ABC,DEF,GHI |

  Scenario: Load Puzzle - Puzzle does not exist
    When user requests to load puzzle with id 404
    Then the application presents the puzzle repository error "Cannot display requested puzzle: No such puzzle exist"