# TODO

## Add prefilled word support

Create an immutable BoxData.

## Implement db module

Hunspell dictionaries first.

## Create CLI

Microcli?

## Add build system

Likely gradle.

## Rework backtrack

- Likely backtrack and iterator should be coupled more.
- Refactory Backtracker, SlotIerator and History.
- Take the time to read and understand Gashnig algorithms applied for 8-Queens SAP and translate them to crossword
  => create wiki page about it

## Test graalvm

See if performances are improved - but just make backtrack more decent first

## Cleanup

Some parts need some love:
- GridDataBuilder:
    - Make it more readable
    - Should ID creation/incrementation be in GridDataBuilder?