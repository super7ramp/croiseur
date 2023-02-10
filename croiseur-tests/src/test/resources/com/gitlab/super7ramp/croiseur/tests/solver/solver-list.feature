Feature: List Solvers

  "Solvers", in croiseur jargon, are modules able to fill a crossword grid. Some call them
  "fillers". The application allows user to list the installed solvers.

  Scenario: List Solver - Default Deployment

  This scenario tests the output of the application when no dictionary provider is installed.

    When user requests to list the available solvers
    Then the application presents the following solvers:
      | Name               | Description                                                           |
      | Ginsberg           | A crossword solver based on Ginsberg's papers. Written in Java.       |
      | Crossword Composer | The solver powering the Crossword Composer software. Written in Rust. |
