# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: Import Puzzle

  Puzzle can be imported to the application puzzle repository from a variety of formats, using
  puzzle decoders.

  Scenario: Import Puzzle - Supported Format
    When user requests to import the following puzzle in the '.xd' format:
    """
    Title: Example Grid
    Author: Jane Doe
    Editor: John Doe
    Date: 2023-06-19


    ABC
    DEF
    GHI


    A1. Start. ~ ABC
    A2. Middle. ~ DEF
    A3. End. ~ GHI

    D1. Some Very. ~ ADG
    D2. Dummy. ~ BEH
    D3. Clues. ~ CFI
    """
    Then the application saves the following puzzle:
      | Id          | $id          |
      | Revision    | 1            |
      | Title       | Example Grid |
      | Author      | Jane Doe     |
      | Editor      | John Doe     |
      | Copyright   |              |
      | Date        | 2023-06-19   |
      | Grid (rows) | ABC,DEF,GHI  |
    And the application presents the confirmation the puzzle has been saved using identifier $id

  Scenario: Import Puzzle - Supported Format - Decoding Error
    When user requests to import the following puzzle in the '.xd' format:
    """
    Title - Example Grid
    Author - Jane Doe
    Editor - John Doe
    Date - 2023-06-19


    ABC
    DEF
    GHI


    A1. Start. ~ ABC
    A2. Middle. ~ DEF
    A3. End. ~ GHI

    D1. Some Very. ~ ADG
    D2. Dummy. ~ BEH
    D3. Clues. ~ CFI
    """
    Then the application presents the following puzzle import error "Error while importing puzzle: com.gitlab.super7ramp.croiseur.puzzle.codec.xd.reader.XdMetadataReadException: Invalid metadata: Invalid property 'Title - Example Grid'. Property must respect the format 'Key: Value'."

  Scenario: Import Puzzle - Unsupported Format
    When user requests to import the following puzzle in the 'unknown' format:
    """
    |A|B|C|
    |D|E|F|
    |G|H|I|
    """
    Then the application presents the following puzzle import error "No suitable decoder found for format 'unknown'"