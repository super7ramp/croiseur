# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: Save Puzzle

  The application manages its own collection of puzzles, stored in a puzzle repository. It allows
  users to store their work so they can be later retrieved.

  The application provides a way to save a puzzle into this puzzle repository.

  Scenario: Save Puzzle - Creation
    When user requests to save the following puzzle:
      | Title          |                                                |
      | Author         | Jane Doe                                       |
      | Editor         |                                                |
      | Copyright      |                                                |
      | Date           | 2023-06-16                                     |
      | Grid (rows)    | ABC,DEF,GHI                                    |
      | Clues (across) | A first clue. - A second clue. - A third clue. |
      | Clues (down)   | A fourth clue. - A fifth clue. - A sixth clue. |
    Then the application saves the following puzzle:
      | Id             | $id                                            |
      | Revision       | 1                                              |
      | Title          |                                                |
      | Author         | Jane Doe                                       |
      | Editor         |                                                |
      | Copyright      |                                                |
      | Date           | 2023-06-16                                     |
      | Grid (rows)    | ABC,DEF,GHI                                    |
      | Clues (across) | A first clue. - A second clue. - A third clue. |
      | Clues (down)   | A fourth clue. - A fifth clue. - A sixth clue. |
    And the application presents the confirmation the puzzle has been saved using identifier $id

  Scenario: Save Puzzle - Update
    Given the puzzle repository contains:
      | Id | Revision | Title | Author   | Editor | Copyright | Date       | Grid (rows) | Clues (across)                                 | Clues (down)                                   |
      | 1  | 1        |       | Jane Doe |        |           | 2023-06-16 | ABC,DEF,GHI | A first clue. - A second clue. - A third clue. | A fourth clue. - A fifth clue. - A sixth clue. |
    When user requests to save the following revised puzzle:
      | Id             | $id                                                     |
      | Title          |                                                         |
      | Author         | Jane Doe                                                |
      | Editor         |                                                         |
      | Copyright      |                                                         |
      | Date           | 2023-06-16                                              |
      | Grid (rows)    | ABC,DEF,XYZ                                             |
      | Clues (across) | A first clue. - A modified second clue. - A third clue. |
      | Clues (down)   | A fourth clue. - A fifth clue. - A sixth clue.          |
    Then the application updates the saved puzzle:
      | Id             | $id                                                     |
      | Revision       | 2                                                       |
      | Title          |                                                         |
      | Author         | Jane Doe                                                |
      | Editor         |                                                         |
      | Copyright      |                                                         |
      | Date           | 2023-06-16                                              |
      | Grid (rows)    | ABC,DEF,XYZ                                             |
      | Clues (across) | A first clue. - A modified second clue. - A third clue. |
      | Clues (down)   | A fourth clue. - A fifth clue. - A sixth clue.          |
    And the application presents the confirmation the puzzle has been saved using identifier $id

  Scenario: Save Puzzle - Update Error
    When user requests to save the following revised puzzle:
      | Id             | 404                                                     |
      | Title          |                                                         |
      | Author         | Jane Doe                                                |
      | Editor         |                                                         |
      | Copyright      |                                                         |
      | Date           | 2023-06-16                                              |
      | Grid (rows)    | ABC,DEF,XYZ                                             |
      | Clues (across) | A first clue. - A modified second clue. - A third clue. |
      | Clues (down)   | A fourth clue. - A fifth clue. - A sixth clue.          |
    Then the application presents the puzzle repository error "Failed to update puzzle: Cannot find saved puzzle with id 404"

  Scenario: Save Puzzle - Patch Details
    Given the puzzle repository contains:
      | Id | Revision | Title | Author   | Editor | Copyright | Date       | Grid (rows) | Clues (across)                                 | Clues (down)                                   |
      | 1  | 1        |       | Jane Doe |        |           | 2023-06-16 | ABC,DEF,GHI | A first clue. - A second clue. - A third clue. | A fourth clue. - A fifth clue. - A sixth clue. |
    When user requests to patch and save puzzle with id 1 with:
      | Title     | Example Grid |
      | Author    | John Doe     |
      | Editor    | Nobody       |
      | Copyright | CC-0         |
      | Date      | 2023-06-17   |
    Then the application updates the saved puzzle:
      | Id             | $id                                            |
      | Revision       | 2                                              |
      | Title          | Example Grid                                   |
      | Author         | John Doe                                       |
      | Editor         | Nobody                                         |
      | Copyright      | CC-0                                           |
      | Date           | 2023-06-17                                     |
      | Grid (rows)    | ABC,DEF,GHI                                    |
      | Clues (across) | A first clue. - A second clue. - A third clue. |
      | Clues (down)   | A fourth clue. - A fifth clue. - A sixth clue. |
    And the application presents the confirmation the puzzle has been saved using identifier $id

  Scenario: Save Puzzle - Patch Grid
    Given the puzzle repository contains:
      | Id | Revision | Title | Author   | Editor | Copyright | Date       | Grid (rows) | Clues (across)                                 | Clues (down)                                   |
      | 1  | 1        |       | Jane Doe |        |           | 2023-06-16 | ABC,DEF,GHI | A first clue. - A second clue. - A third clue. | A fourth clue. - A fifth clue. - A sixth clue. |
    When user requests to patch and save puzzle with id 1 with:
      | Grid (rows) | ABC,DEF,XYZ |
    Then the application updates the saved puzzle:
      | Id             | $id                                            |
      | Revision       | 2                                              |
      | Title          |                                                |
      | Author         | Jane Doe                                       |
      | Editor         |                                                |
      | Copyright      |                                                |
      | Date           | 2023-06-16                                     |
      | Grid (rows)    | ABC,DEF,XYZ                                    |
      | Clues (across) | A first clue. - A second clue. - A third clue. |
      | Clues (down)   | A fourth clue. - A fifth clue. - A sixth clue. |
    And the application presents the confirmation the puzzle has been saved using identifier $id

  Scenario: Save Puzzle - Patch Clues
    Given the puzzle repository contains:
      | Id | Revision | Title | Author   | Editor | Copyright | Date       | Grid (rows) | Clues (across)                                 | Clues (down) |
      | 1  | 1        |       | Jane Doe |        |           | 2023-06-16 | ABC,DEF,GHI | A first clue. - A second clue. - A third clue. |              |
    When user requests to patch and save puzzle with id 1 with:
      | Clues (across) | A first clue. - A modified second clue. - A third clue. |
      | Clues (down)   | A fourth clue. - A fifth clue. - A sixth clue.          |
    Then the application updates the saved puzzle:
      | Id             | $id                                                     |
      | Revision       | 2                                                       |
      | Title          |                                                         |
      | Author         | Jane Doe                                                |
      | Editor         |                                                         |
      | Copyright      |                                                         |
      | Date           | 2023-06-16                                              |
      | Grid (rows)    | ABC,DEF,GHI                                             |
      | Clues (across) | A first clue. - A modified second clue. - A third clue. |
      | Clues (down)   | A fourth clue. - A fifth clue. - A sixth clue.          |
    And the application presents the confirmation the puzzle has been saved using identifier $id

  Scenario: Save Puzzle - Patch Error
    When user requests to patch and save puzzle with id 404 with:
      | Title       | Example Grid |
      | Grid (rows) | ABC,DEF,XYZ  |
    Then the application presents the puzzle repository error "Failed to update puzzle: Cannot find saved puzzle with id 404"
