# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: List Puzzle Dedoders

  Puzzle can be imported to the application puzzle repository from a variety of formats, using
  puzzle decoders.

  The application offers a way to list of the available decoders.

  Scenario: List Puzzle Decoders - All decoders
    When user requests to list the available puzzle decoders
    Then the application presents the following puzzle decoders:
      | Name | Description       | Supported Formats |
      | xd   | xd format decoder | *.xd              |
