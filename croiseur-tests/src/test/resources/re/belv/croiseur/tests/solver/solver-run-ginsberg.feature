# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: Run Solver - Ginsberg

  "Solvers", in croiseur jargon, are modules able to fill a crossword grid. Some call them
  "fillers". The application allows user to fill crossword grid using these solvers. That is the
  main usecase of the application.

  Here it is tested basic solving capabilities using default Ginsberg solver.

  Note that for scenarios without specified dictionary, the application will select a preferred
  dictionary considering system's locale and dictionary provider performance. Since tests are
  executed in English locale and since XML provider is considered as the most efficient,
  default dictionary is "General British English dictionary" provided by "Local XML Provider".

  Scenario: Run Solver - Ginsberg - Simple

    When user requests to solve the following grid with "Ginsberg" solver:
      |  |  |  |
      |  |  |  |
      |  |  |  |
    Then the application presents the following successful solver result:
      | D | C | M |
      | E | P | A |
      | S | A | Y |

  Scenario: Run Solver - Ginsberg - With Specific Dictionary

    When user requests to solve the following grid with "Ginsberg" solver and with "The UK Advanced Cryptics Dictionary" provided by "Local Text Provider":
      |  |  |  |
      |  |  |  |
      |  |  |  |
    Then the application presents the following successful solver result:
      | A | D | S |
      | B | A | A |
      | A | L | T |

  Scenario: Run Solver - Ginsberg - With Shaded Cell

    When user requests to solve the following grid with "Ginsberg" solver:
      |  |  |   |
      |  |  | # |
      |  |  |   |
    Then the application presents the following successful solver result:
      | C | M | S |
      | A | A | # |
      | A | D | V |

  Scenario: Run Solver - Ginsberg - With Prefilled Cell

    When user requests to solve the following grid with "Ginsberg" solver:
      | A |  |  |
      | C |  |  |
      | T |  |  |
    Then the application presents the following successful solver result:
      | A | D | S |
      | C | A | A |
      | T | D | D |

  Scenario: Run Solver - Ginsberg - With Prefilled and Shaded Cells

    When user requests to solve the following grid with "Ginsberg" solver:
      | A |   |   |
      | C | # |   |
      | T |   | # |
    Then the application presents the following successful solver result:
      | A | B | O |
      | C | # | T |
      | T | Y | # |

  Scenario: Run Solver - Ginsberg - With Randomness

  It is the same grid that the first scenario "Simple" but the dictionary is shuffled. This leads
  to a different solution found.

    When user requests to solve the following grid with "Ginsberg" solver and with dictionary shuffled using a seed of 42:
      |  |  |  |
      |  |  |  |
      |  |  |  |
    Then the application presents the following successful solver result:
      | T | A | S |
      | A | D | S |
      | S | S | S |
