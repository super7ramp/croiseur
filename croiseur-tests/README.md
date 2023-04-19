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

The test environment is set up using the following tools:

- Cucumber
- Mockito
- PicoContainer (for auto-wiring scenario steps to glue code)
- JUnit 5

`croiseur` is deployed with all the main service providers,
except [`Presenter`](../doc/reference/Available-service-providers.md#presenters)s as per the test
strategy described above. Check [`build.gradle`](build.gradle.kts) for the complete list of providers.

A `Presenter` mock is used to verify the output of `croiseur`.

For more details on this deployment, check the
[`DeploymentSteps`](src/test/java/com/gitlab/super7ramp/croiseur/tests/context/DeploymentSteps.java)
class.

### Test Organisation

Scenarios and glue code organisation follow `croiseur` API organisation.

#### Scenarios

Test packages are named following `CrosswordService` method names.
Scenario files are named following tested service structure:
`<tested-service>-<tested-method-alias>-<optional-test-flavour>`.

```
.
├── dictionary                                        // Scenarios testing croiseur.api.dictionary
│   ├── dictionary-cat.feature
│   ├── dictionary-grep.feature 
│   ├── dictionary-list.feature
│   └── ...
└── solver                                            // Scenarios testing croiseur.api.solver
    ├── solver-list.feature
    ├── solver-run-ginsberg.feature
    ├── solver-run-paulgb.feature
    └── ...
```

#### Glue Code

Test packages are named following `CrosswordService` method names.
A specific package `context` is used for managing test lifecycle.
In normal packages, the following kinds of classes are found:

- Step classes (suffixed with `Steps`): The glue code implementing scenario steps;
- Matcher classes (suffixed with `Matchers`): They allow to create Mockito's `ArgumentMatcher`s;
  Used in verification steps;
- Type classes (suffixed with `Types`): Type conversion methods, typically these classes register
  Cucumber datatable and parameter types.

```
.
├── DictionaryTestSuite.java           // At root, JUnit test suites for easy individual launch
├── SolverTestSuite.java
├── WipTestSuite.java
├── context                            // Manages test lifecycle
│   ├── DeploymentSteps.java
│   └── TestContext.java
├── dictionary                         // Glue code for scenarios testing croiseur.api.dictionary
│   ├── DictionaryMatchers.java
│   ├── DictionarySteps.java
│   └── DictionaryTypes.java
└── solver                             // Glue code for scenarios testing croiseur.api.solver
     ├── SolverMatchers.java
     ├── SolverSteps.java
     └── SolverTypes.java
```
