# TODO

## Features

### Add English dictionary

### UTF8

- Don't think it's necessary for French, it might be useful for other languages

## Improvements

### Solver

#### Backtrack

- Likely backtrack and iterator should be coupled more.
- Refactor Backtracker, SlotIerator and History.
- Doc to read:
  - (again) Gashnig algorithms applied for 8-Queens SAP
  - Knuth's fascicle on satisfiability

#### GridDataBuilder

- Make it more readable
- Should ID creation/incrementation be in GridDataBuilder? => maybe not if id is used for backtracking

#### Dictionary wrapper

- Improve cache (for contains in particular)

#### Progress indication

- Percentage of filled slot every 30s?
- Remaining slot
- Record stats? Number of dead-ends encountered, number of assignments, stats on number of assignments before dead-ends

### Dictionary

- Filtering

### CandidateChooser

- Perfs: Check if probe parallelization improves performances with default k and with bigger k

### Others

#### GraalVM

To test. See if performances are improved - but just make backtrack more decent first