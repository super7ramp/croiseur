# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: Run Solver - XWords RS (szunami)

  "Solvers", in croiseur jargon, are modules able to fill a crossword grid. Some call them
  "fillers". The application allows user to fill crossword grid using these solvers. That is the
  main usecase of the application.

  Here it is tested basic solving capabilities using szunami's XWords RS solver.

  Note that for scenarios without specified dictionary, the application will select a preferred
  dictionary considering system's locale and dictionary provider performance. Since tests are
  executed in English locale and since XML provider is considered as the most efficient,
  default dictionary is "General British English dictionary" provided by "Local XML Provider".

  Scenario: Run Solver - XWords RS (szunami) - Simple

    When user requests to solve the following grid with "XWords RS" solver:
      |  |  |  |
      |  |  |  |
      |  |  |  |
    Then the application presents the following successful solver result:
      | Z | U | Z |
      | U | T | U |
      | Z | U | G |

  Scenario: Run Solver - XWords RS (szunami) - With Specific Dictionary

    When user requests to solve the following grid with "XWords RS" solver and with "The UK Advanced Cryptics Dictionary" provided by "Local Text Provider":
      |  |  |  |
      |  |  |  |
      |  |  |  |
    Then the application presents the following successful solver result:
      | Z | U | Z |
      | U | T | U |
      | Z | U | G |

  Scenario: Run Solver - XWords RS (szunami) - With Shaded Cell

    When user requests to solve the following grid with "XWords RS" solver:
      |  |  |   |
      |  |  | # |
      |  |  |   |
    Then the application presents the following successful solver result:
      | Z | U | Z |
      | U | T | # |
      | Z | E | D |

  Scenario: Run Solver - XWords RS (szunami) - With Prefilled Cell

    When user requests to solve the following grid with "XWords RS" solver:
      | A |  |  |
      | C |  |  |
      | T |  |  |
    Then the application presents the following successful solver result:
      | A | S | L |
      | C | R | Y |
      | T | A | N |

  Scenario: Run Solver - XWords RS (szunami) - With Prefilled and Shaded Cells

    When user requests to solve the following grid with "XWords RS" solver:
      | A |   |   |
      | C | # |   |
      | T |   | # |
    Then the application presents the following successful solver result:
      | A | Z | T |
      | C | # | V |
      | T | A | # |

  Scenario: Run Solver - XWords RS (szunami) - With Randomness

  It is the same grid that the first scenario "Simple" but the dictionary is shuffled. This leads
  to a different solution found.

    When user requests to solve the following grid with "XWords RS" solver and with dictionary shuffled using a seed of 42:
      |  |  |  |
      |  |  |  |
      |  |  |  |
    Then the application presents the following successful solver result:
      | B | Y | S |
      | O | O | O |
      | B | B | S |
