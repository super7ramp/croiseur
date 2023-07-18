# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: List Puzzle Encoders

  Puzzle can be exported to a variety of formats, using puzzle encoders.

  The application offers a way to list of the available encoders.

  Scenario: List Puzzle Encoders - All encoders
    When user requests to list the available puzzle encoders
    Then the application presents the following puzzle encoders:
      | Name | Description       | Supported Formats |
      | xd   | xd format encoder | *.xd              |
