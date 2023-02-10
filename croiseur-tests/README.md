<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## croiseur-tests

### Test Strategy

This subproject contains tests for [`croiseur`](../croiseur). They verify the good behaviour of
the core library and its main [solver](../croiseur-solver) and [dictionary](../croiseur-dictionary)
plugins.

Scenarios are written in the high-level [Gherkin](https://cucumber.io/docs/gherkin/reference/)
language.

The tests provided here are not end-to-end tests: They do not verify control/presentation details
implemented by application modules such as [`croiseur-cli`](../croiseur-cli)
and [`croiseur-gui`](../croiseur-gui). Such tests should be part of applications' tests.

### Test Deployment

`croiseur` is deployed with the following service providers:

- Dictionary providers
  - [Local Hunspell Dictionary Provider](../croiseur-dictionary/croiseur-dictionary-hunspell-plugin)
  - [Local Text Dictionary Provider](../croiseur-dictionary/croiseur-dictionary-txt-plugin)
  - [Local XML Dictionary Provider](../croiseur-dictionary/croiseur-dictionary-xml-plugin)
- Solvers
  - [Ginsberg](../croiseur-solver/croiseur-solver-ginsberg-plugin)
  - [Crossword Composer (paulgb)](../croiseur-solver/croiseur-solver-paulgb-plugin)
  - A mock presenter allowing to verify the output of `croiseur`
