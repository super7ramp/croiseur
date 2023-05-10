## Explanation: Why Dictionaries Matter

### Disclaimer

This is an unreviewed explanation of how the dictionaries content can influence the solving
duration. Do not take everything here as necessarily correct. If you are looking for more
thorough explanations, check out the [reference papers](#references).

### Experiments

#### Scenarios

The effort to fill a grid differs between the used dictionaries.

To visualise this effort differences, we tried to solve various grids using various solvers and
dictionaries.

The selected grids were:

```
| | | | | |      | | | | | | |     | | | | | | | |     |#|#|#| | | |#|#|#| 
| | | | | |      | | | | | | |     | | | | | | | |     |#|#| | | | | |#|#| 
| | | | | |      | | | | | | |     | | | | | | | |     |#| | | | | | | |#| 
| | | | | |      | | | | | | |     | | | | | | | |     | | | | | | | | | | 
| | | | | |      | | | | | | |     | | | | | | | |     | | | | | | | | | | 
                 | | | | | | |     | | | | | | | |     | | | | | | | | | | 
                                   | | | | | | | |     |#| | | | | | | |#| 
                                                       |#|#| | | | | |#|#|
                                                       |#|#|#| | | |#|#|#|   
                                                                  
    5x5               6x6                7x7              9x9 (3-5-7-9)         
```

The selected dictionaries, available in Croiseur, were:

- UKACD
- General British English
- General French

For each dictionary, 6 solving attempts were made with different shuffles in order to limit the
effect of the word order in the search:

- No shuffle
- Shuffle with seed 7
- Shuffle with seed 42
- Shuffle with seed 365
- Shuffle with seed 1992
- Shuffle with seed 2023

The selected solvers, available in Croiseur, were:

- [Ginsberg](../../croiseur-solver/croiseur-solver-ginsberg): A solver implementing in Java some of
  the solutions found by Matt Ginsberg.
- [XWords RS](../../croiseur-solver/croiseur-solver-szunami): A solver with simpler backtrack
  algorithms but whose implementation in Rust is more optimised.

We ignored Crossword Composer solver as early tests showed it did not manage well
on the selected grids. More details about these solvers are
available [here](How-crossword-solvers-work.md).

#### Environment

We used [Croiseur CLI](../../croiseur-cli/README.md) to run the solvers. Given Croiseur CLI
architecture (no daemon), each solving attempt resulted in the launch of a new Java Virtual Machine
and a read of the selected dictionary from the filesystem, plus optionally a shuffle. This overhead
is estimated to 2 to 3 seconds on the machine used for the tests.

Given current Croiseur project architecture, overhead could have been eliminated or reduced using
either:

- [Croiseur GUI](../../croiseur-gui/README.md): This would have allowed to load the JVM and
  dictionaries only once but the GUI would have made the test automation more difficult;
- Croiseur
  CLI [Ahead-of-Time compilation option](../../croiseur-cli/INSTALL.md#native-image-experimental):
  We preferred to use the standard build for reproducibility, given the experimental nature of this
  option.

The machine which ran the tests had an i5 CPU from 2010 whose maximum frequency was set to 1.7 GHz
for reproducibility.

#### Results

The following table lists the durations in seconds necessary to find a solution to several grids
with different dictionaries.

| Grid          | Dictionary              | Ginsberg                | XWords RS              | 
|---------------|-------------------------|-------------------------|------------------------|
| 5x5           | UKACD                   | 3-3-3-5-3-3             | 3-4-4-4-4-4            |
| 5x5           | General British English | 3-5-5-4-4-4             | 4-5-5-4-5-4            |
| 5x5           | General French          | 4-5-4-4-5-5             | 6-7-6-7-6-6            |
| 6x6           | UKACD                   | 4-6-16-5-7-9            | 4-7-4-4-7-6            |
| 6x6           | General British English | DNF-12-10-DNF-40-7      | 5-6-6-10-19-11         |
| 6x6           | General French          | DNF-6-7-7-7-13          | 6-7-7-6-6-7            |
| 7x7           | UKACD                   | DNF-DNF-DNF-DNF-DNF-DNF | 35-221-DNF-DNF-132-164 |
| 7x7           | General British English | DNF-DNF-15-DNF-DNF-DNF  | 33-DNF-DNF-DNF-145-DNF |
| 7x7           | General French          | 11-DNF-DNF-DNF-DNF-11   | 6-24-122-157-227-12    |
| 9x9 (3-5-7-9) | UKACD                   | DNF-DNF-DNF-DNF-DNF-DNF | 64-231-189-29-68-10    |
| 9x9 (3-5-7-9) | General British English | DNF-170-DNF-DNF-DNF-DNF | 63-12-72-76-63-28      |
| 9x9 (3-5-7-9) | General French          | 198-DNF-41-165-DNF-10   | 23-19-43-8-8-81        |

(DNF = Did not finish within 245 seconds.)

> — It will be interesting to capture the number of backtracks instead of the solving durations, in
> order to abstract away the raw throughput differences between solvers. For a given solver, the
> number of backtracks is expected to be proportional to the solving duration.

From the results above, one can make the hypothesises on the compared difficulties of the
dictionaries. With the notation "A > B" meaning "Dictionary A is easier to work with than dictionary
B":

- 6x6: UKACD > General French >> General British English
- 7x7: General French > General British English, UKACD
- 9x9 (3-5-7-9): General French > General British English > UKACD

### How to Predict the Number of Solutions

A first intuition is that the more the words in the dictionary, the more chances to have solutions,
and the easier for the solvers to find one.

In this section are presented a few estimations on the number of solutions based on the dictionary
content and the grid geometry. We will see that these estimations effectively depend on the number
of words, but not only.

#### Square Grids

##### Estimation

Using probabilities, [Long92] proposed the following estimation for the number of solutions for
square grids without shaded boxes:

```math
W^{2n} × p^{n^2}
```

Where:

* $n$ is the size of the square grid
* $W$ is the number of words of size $n$ in the dictionary
* $p = f(a)^2 + f(b)^2 + … + f(z)^2$ where $f(\*)$ is the frequency of letter $\*$ in the word
  list.

> — Watch out: [Jensen97] references this formula but contains a typo in the definition of $p$:
> The powers of 2 are missing on the frequencies. The real definition contains the powers of 2.

This formula comes with two important hypothesises:

1. Letter frequencies are positionally independent in the dictionary;
2. Words can be repeated in the grid.

> — [Long92] in his paper did mention having computed results dropping hypothesis 1., which
> resulted in slightly higher numbers with the dictionary he used. Unfortunately, he did not precise
> the formula in this case, nor the dictionary he used for his results.
> [Long92] did not mention the second point. It might be a mistake from my side but this is what
> I deduced when trying to re-find the formula, see next section.

##### Rationale

> — Be cautious about this section. [Long92] mentions the Bayes Theorem, but where is it used here?
> Everything ends up assumed independent.

Let us start with the simplest case, a square grid of size $n = 2$. Let us define the fixed
crossings $c_1, c_2, c_3, c_4$:

```
     | c1 | c2 |
     | c3 | c4 |
```

Now, let us define the following events:

* $X_2$: 4 words $\left\{ w_1, w_2, w_3, w_4 \right\}$ of size 2, not necessarily
  distinct, taken in the word list, correctly fill the grid, such as described in the figure below.
* $x_i$: Crossing $c_i$ is satisfied, e.g. for $c_1$, first character of $w_1$ equals first
  character of $w_3$.

```
       w1   w2
       v    v
w3 > | c1 | c2 |
w4 > | c3 | c4 |
```

The probability of event $X_2$ is:

```math
P(X_2) = P(x_1)×P(x_2)×P(x_3)×P(x_4)
```

Another way to write it, by noting $w_i[j]$ the $j$'th letter of word $w_i$, is:

```math
P(X_2) = P(w_1[1] == w_3[1])×P(w_2[1] == w_3[1])×P(w_1[2] == w_4[1])×P(w_4[2] == w_2[2])
```

Let us define the event $a_i$ (respectively $b_i, c_i, …, z_i$) the probability to have the letter
$a$ (resp. $b$, $c$, …, $z$) at the position $i$ for a word of length 2 picked in the dictionary.

Assuming that letter frequencies in a word are independent of other letters in the word:

```math
\begin{split}
P(X) = & (P(a_1) × P(a_1) + P(b_1) × P(b_1) + … + P(z_1) × P(z_1)) × \newline
       & (P(a_2) × P(a_1) + P(b_2) × P(b_1) + … + P(z_2) × P(z_1)) × \newline
       & (P(a_1) × P(a_2) + P(b_1) × P(b_2) + … + P(z_1) × P(z_2)) × \newline
       & (P(a_2) × P(a_2) + P(b_2) × P(b_2) + … + P(z_2) × P(z_2))
\end{split}
```

Assuming that letter frequencies are totally positionally independent, i.e. $P(a_i) = f(a)$ for
every letter and word position $i$, where $f(\*)$ is the frequency of letter $\*$ in the word list,
the expression can be simplified to:

```math
\begin{split}
P(X_2) = &(f(a)^2 + f(b)^2 + … f(z)^2) × \newline
         &(f(a)^2 + f(b)^2 + … f(z)^2) × \newline
         &(f(a)^2 + f(b)^2 + … f(z)^2) × \newline
         &(f(a)^2 + f(b)^2 + … f(z)^2) \newline
P(X_2) = &(f(a)^2 + f(b)^2 + … f(z)^2)^4
\end{split}
```

To get the estimated number of valid fills $E_2$ for square grids of size 2, we multiply the
probability of $X_2$ with the number of possible arrangements with repetition of 4 words of length
2 in the word list. Noting $W_2$ the number of words of length 2 in the word list, the number of
possible arrangement of 4 words among W is $W_2^4$ hence the estimated number of fills:

```math
E_2 = W_2^4×P(X_2)^4
```

Let us generalize to square grids of size $n$.
Let us note $X_n$ the event to pick $2n$ words of size $n$, not necessarily distinct, that fills
a square grid of size $n$ with a similar ordering of crossings as for $X_2$.
We find that $P(X_n) = (f(a)^2 + f(b)^2 + … f(z)^2)^{n^2}$.
Noting $P(X_n) = p$, and $W$ the number of words of length $n$ in the word list, we get back to Long
formula for the estimated number of fills $E$:

```math
E = W^{2n}×p^{n^2}
```

##### Application

Here is a table with the estimated number of solutions for the dictionaries used in
our [experiments](#experiments).

| ↓ Dictionary / n →      | 5                                     | 6                                     | 7                                  |
|-------------------------|---------------------------------------|---------------------------------------|------------------------------------|
| UKACD                   | 9,030,548,510 (W = 11,646; p = 0.059) | 89,013,686 (W = 20,160; p = 0.061)    | 1,705 (W = 29,050; p = 0.062)      |
| General British English | 8,764,187,461 (W = 11,082; p = 0.060) | 127,703,534 (W = 19,948; p = 0.062)   | 2,350 (W = 28,681; p = 0.062)      |
| General French          | 11,207,001,557 (W = 8,116; p = 0.069) | 6,711,200,867 (W = 17,882; p = 0.072) | 45,575,233 (W = 31,744; p = 0.074) |

And a recall of what we expected based on our experiments:

- 6x6: UKACD > General French >> General British English
- 7x7: General French > General British English, UKACD

Estimations for n = 7 are consistent with the experiments: General French dictionary is supposed to
have 2 orders of magnitude more solutions than the others and is effectively easier to work with.

Estimations for n = 6 are more surprising.

Even if UKACD has more words of size 6 than the others, UKACD is supposed to have fewer solutions
than the others considering its letter frequencies. But in practice, solvers seem to work better
with it. Maybe UKACD has another property that is useful here.

> — It would be interesting to drop the hypothesis that letter frequencies are positionally
> independent here and see if that makes a difference.

General British English, which is "in the middle" in the estimations, performs significantly worse
than the others. But if you ignore UKACD, it is consistent compared with the General French
dictionary, which is supposed to have an order of magnitude more solutions.

For n = 5, nothing to say, all dictionaries have roughly the same number of estimated solutions
and all solvers quickly find a solution.

#### Generic Grid

##### Estimation

[Harris90] proposed more generic estimations, including this lower limit:

```math
\left(\prod_{i}^{} \binom{n_i}{w_i} \right) × \left(\prod_{j=1}^{m}\sum_{a}^{}P_{n_{1}^{(j)}}(a, i_1)×P_{n_{2}^{(j)}}(a, i_2) \right)
```

Where:

* $n_i$ the number of words of length $i$ in dictionary, the number of words in the dictionary
  being $N = n_1 + n_2 + …$
* $w_i$ the number of slots of length $i$, i.e. $W = w_1 + w_2 + …$
* $m$ the number of intersections in the puzzle, with the $j$'th intersection being between one word
  of length $n_1$ and another word of length $n_2$, occurring between the $i_1$'th letter of the
  first word and the $i_2$'th letter of the second word.
* $P_n(a, i)$ the probability of an $n$ letter word in the dictionary having the $a$'th letter
  of the alphabet in the $i$'th position within the word.

##### Rationale

> — This section is empty. It will eventually contain explanations for the formula. For the time
> being, refer to [Jensen97] which contains some high level explanations, or [Harris90] if you are
> lucky (no free access).
>
> Note: I think the estimation is closer to the estimation of [Long92] than it looks, if you remove
> the assumption that letter frequencies are positionally independent (right part of the formula).
> It seems to use combinations instead of arrangements with repetition (left part).

##### Application

> — This section is empty. It will eventually contain computed estimations for the dictionary used
> in experiments and a comparison between these estimations and the solving durations for grid 9x9 (
> 3-5-7-9).

### References

* [Harris90]: G.H. Harris, J.J.H. Forster. _On the Bayesian Estimation and Computation of the Number
  of Solutions to Crossword Puzzles_. Symposium on Applied Computry, 1990.
* [Jensen97]: Sik Cambon Jensen, _Design and Implementation of Crossword Compilation Programs_,1997.
* [Long92]: Chris Long. _Mathematics of Square Construction_. Word Ways, 26(1), 1992.
