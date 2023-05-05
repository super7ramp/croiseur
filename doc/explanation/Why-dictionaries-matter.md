## Explanation: Why Dictionaries Matter

### Disclaimer

This is a short, high level and unreviewed explanation of how the dictionaries content influence the
solving duration. Do not take everything here as necessarily correct. If you are looking for more
thorough explanations, check out the [reference papers](#references).

### Preliminary Observation

The effort to fill a grid may greatly differ between the used dictionaries.

TODO: Examples

- grid 1: 3 dictionaries, 3 solvers
- grid 2: 3 dictionaries, 3 solvers

### How to Predict the Number of Solutions

A first intuition is that the more the words the dictionary contains, the more solutions there are.

> — Unfortunately, it doesn't mean that necessarily the more solutions there is, the shorter will be
> the solving time. If heuristics gives wrong directions early, a depth-first solver can get stuck
> in a search space with not many solutions.

In this section are presented a few estimations on the number of solutions based on the dictionary
content and the grid geometry.

#### Estimation for Square Grids

Using probabilities, [Long92] proposed the following simple estimation for the number of solutions
for square without shaded boxes:

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

> — Here are the steps to get to this result:
> TODO explain the steps to get to this result. Long did not describe the steps. I got the steps for
> the right part $p^{n^2}$ but I don't get the left part: $W^{2n}$. I don't get why it's not
> $\binom{W}{2n}$. I must misunderstand something.

> — This comes with the assumption that letter frequencies are positionally independent in the
> dictionary, which is inaccurate in English as well as probably in all other languages. [Long92]
> does mention having computed results dropping this hypothesis, which resulted in slightly higher
> numbers with the dictionary it used. Unfortunately, he doesn't precise the formula in this case,
> nor the dictionary he used for his results.

#### A More Generic Estimation

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

> TODO understand that. I think it is closer to the estimation of [Long92] than it looks, if
> you remove the assumption than letter frequencies are positionally independent. Like for Long
> estimate I'm more worried not understanding the left part which annoyingly looks like the simplest
> part.

### References

* [Harris90]: G.H. Harris, J.J.H. Forster. _On the Bayesian Estimation and Computation of the Number
  of Solutions to Crossword Puzzles_. Symposium on Applied Computry, 1990.
* [Jensen97]: Sik Cambon Jensen, _Design and Implementation of Crossword Compilation Programs_,1997.
* [Long92]: Chris Long. _Mathematics of Square Construction_. Word Ways, 26(1), 1992.
