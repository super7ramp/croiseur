# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: Export Puzzle

  Puzzles can be exported from the application puzzle repository to a variety of formats, using
  puzzle encoders.

  Background:
    Given the puzzle repository contains:
      | Id | Revision | Title        | Author   | Editor   | Copyright | Date       | Grid (rows) |
      | 1  | 1        | Example Grid | Jane Doe | John Doe |           | 2023-06-19 | ABC,DEF,GHI |

  Scenario: Export Puzzle - xd Format
    When user requests to export the puzzle with id 1 to format '*.xd'
    Then the application writes the following export data:
    # TODO investigate: It looks like there are extra lines at the end
    """
    Title: Example Grid
    Author: Jane Doe
    Editor: John Doe
    Date: 2023-06-19


    ABC
    DEF
    GHI




    """

  Scenario: Export Puzzle - Unsupported Format
    When user requests to export the puzzle with id 1 to format 'unknown'
    Then the application presents the puzzle export error "No suitable encoder found for format 'unknown'"

  Scenario: Export Puzzle - Puzzle not found
    When user requests to export the puzzle with id 404 to format '*.xd'
    Then the application presents the puzzle repository error 'Puzzle with id 404 not found.'