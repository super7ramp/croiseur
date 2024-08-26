# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: Run Solver - SAT

  "Solvers", in croiseur jargon, are modules able to fill a crossword grid. Some call them
  "fillers". The application allows user to fill crossword grid using these solvers. That is the
  main usecase of the application.

  Here it is tested basic solving capabilities using generic SAT solver.

  Note that for scenarios without specified dictionary, the application will select a preferred
  dictionary considering system's locale and dictionary provider performance. Since tests are
  executed in English locale and since XML provider is considered as the most efficient,
  default dictionary is "General British English dictionary" provided by "Local XML Provider".

  Scenario: Run Solver - SAT - Simple

    When user requests to solve the following grid with "SAT" solver:
      |  |  |  |
      |  |  |  |
      |  |  |  |
    Then the application presents the following successful solver result:
      | O | D | D |
      | L | B | J |
      | A | I | D |

  Scenario: Run Solver - SAT - With Specific Dictionary

    When user requests to solve the following grid with "SAT" solver and with "The UK Advanced Cryptics Dictionary" provided by "Local Text Provider":
      |  |  |  |
      |  |  |  |
      |  |  |  |
    Then the application presents the following successful solver result:
      | B | A | A |
      | A | B | B |
      | B | A | A |

  Scenario: Run Solver - SAT - With Shaded Cell

    When user requests to solve the following grid with "SAT" solver:
      |  |  |   |
      |  |  | # |
      |  |  |   |
    Then the application presents the following successful solver result:
      | D | D | R |
      | D | J | # |
      | T | D | D |

  Scenario: Run Solver - SAT - With Prefilled Cell

    When user requests to solve the following grid with "SAT" solver:
      | A |  |  |
      | C |  |  |
      | T |  |  |
    Then the application presents the following successful solver result:
      | A | N | D |
      | C | R | C |
      | T | A | M |

  Scenario: Run Solver - SAT - With Prefilled and Shaded Cells

    When user requests to solve the following grid with "SAT" solver:
      | A |   |   |
      | C | # |   |
      | T |   | # |
    Then the application presents the following successful solver result:
      | A | A | A |
      | C | # | A |
      | T | V | # |

  Scenario: Run Solver - SAT - With Randomness

  It is the same grid that the first scenario "Simple" but the dictionary is shuffled. This leads
  to a different solution found.

    When user requests to solve the following grid with "SAT" solver and with dictionary shuffled using a seed of 42:
      |  |  |  |
      |  |  |  |
      |  |  |  |
    Then the application presents the following successful solver result:
      | I | T | T |
      | F | A | A |
      | S | S | S |
