<!--
SPDX-FileCopyrightText: 2024 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## croiseur-solver-benchmark

`croiseur-solveur-benchmark` provides a benchmark suite for crossword solvers implementing the [
`CrosswordSolver`](../../croiseur-spi/croiseur-spi-solver/src/main/java/re/belv/croiseur/spi/solver/CrosswordSolver.java)
interface.

### How to use it

1. Get the [jmh](https://github.com/openjdk/jmh) tooling by adding the Gradle convention plugin
   `re.belv.croiseur.java-benchmark`:

```gradle
plugins {
    id("re.belv.croiseur.java-benchmark")
}
```

2. Pull the `croiseur-solver-benchmark` dependency:

```gradle
dependencies {
    jmh(project(":croiseur-solver:croiseur-solver-benchmark")
}
```

3. Implement the `CrosswordSolverBenchmark` interface in `src/main/jmh` in your solver project:

```java
package mypackage;

import re.belv.croiseur.solver.benchmark.CrosswordSolverBenchmark;

public class MyBenchmark implements CrosswordSolverBenchmark {
    @Override
    protected final CrosswordSolver solver() {
        return new MyCrosswordSolver();
    }
}
```

4. Run the benchmark:

```shell
gradle jmh
```
