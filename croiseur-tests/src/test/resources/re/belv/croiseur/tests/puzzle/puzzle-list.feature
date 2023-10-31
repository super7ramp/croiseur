# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: List Puzzles

  The application manages its own collection of puzzles, stored in a puzzle repository. It allows
  users to store their work so they can be later retrieved.

  The application provides a way to list the puzzle of this puzzle repository.

  Scenario: List Puzzles - Empty
    When user requests to list the available puzzles
    Then the application presents an empty list of puzzles

  Scenario: List Puzzles - Not empty

    Given the puzzle repository contains:
      | Id | Revision | Title | Author   | Editor | Copyright | Date       | Grid (rows) | Clues (across)        | Clues (down) |
      | 1  | 1        |       | Jane Doe |        |           | 2023-06-16 | ABC,DEF,GHI | Test. - Test. - Test. | Test.        |
      | 2  | 1        |       | John Doe |        |           |            | ...,XYZ,... |                       |              |
      | 3  | 1        |       | John Doe |        | CC-BY     |            | ...,...,... |                       |              |
    When user requests to list the available puzzles
    Then the application presents the following list of puzzles:
      | Id | Revision | Title | Author   | Editor | Copyright | Date       | Grid (rows) | Clues (across)        | Clues (down) |
      | 1  | 1        |       | Jane Doe |        |           | 2023-06-16 | ABC,DEF,GHI | Test. - Test. - Test. | Test.        |
      | 2  | 1        |       | John Doe |        |           |            | ...,XYZ,... |                       |              |
      | 3  | 1        |       | John Doe |        | CC-BY     |            | ...,...,... |                       |              |