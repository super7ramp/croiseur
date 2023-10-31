# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: Delete Puzzle

  The application manages its own collection of puzzles, stored in a puzzle repository. It allows
  users to store their work so they can be later retrieved.

  The application provides a way to delete any puzzle of this puzzle repository.

  Scenario: Delete Puzzle - Delete Single Puzzle
    Given the puzzle repository contains:
      | Id | Revision | Title | Author   | Editor | Copyright | Date       | Grid (rows) |
      | 1  | 1        |       | Jane Doe |        |           | 2023-06-16 | ABC,DEF,GHI |
    When user requests to delete the puzzle with id 1
    Then the application deletes the puzzle with id 1 from repository
    And the application presents the confirmation the puzzle with identifier 1 has been deleted

  Scenario: Delete Puzzle - Delete All Puzzles
    Given the puzzle repository contains:
      | Id | Revision | Title | Author   | Editor | Copyright | Date       | Grid (rows) |
      | 1  | 1        |       | Jane Doe |        |           | 2023-06-16 | ABC,DEF,GHI |
      | 2  | 1        |       | Jane Doe |        |           | 2023-06-22 | XYZ,DEF,GHI |
    When user requests to delete all puzzles
    Then the application deletes the puzzle with id 1 from repository
    And the application deletes the puzzle with id 2 from repository
    And the application presents the confirmation that all puzzles have been deleted

  Scenario: Delete Puzzle - Puzzle does not exist
    When user requests to delete the puzzle with id 404
    Then the application presents the puzzle repository error "Failed to delete puzzle: Cannot delete a non-existing puzzle"