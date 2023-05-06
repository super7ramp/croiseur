## Explanation: Why Dictionaries Matter

### Disclaimer

This is an unreviewed explanation of how the dictionaries content can influence the solving
duration. Do not take everything here as necessarily correct. If you are looking for more
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

#### Square Grids

##### Estimation

Using probabilities, [Long92] proposed the following simple estimation for the number of solutions
for square grids without shaded boxes:

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

> — [Long92] in his paper does mention having computed results dropping hypothesis 1., which
> resulted in slightly higher numbers with the dictionary it used. Unfortunately, he doesn't precise
> the formula in this case, nor the dictionary he used for his results.
> [Long92] did not mention the second point. It might be a mistake from my side but this what
> I deduced when trying to re-find the formula, see next section.

#### Rationale

[Long92] does not describe the steps he used to get to this estimation. Here is an attempt to
re-find them.

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
P(X_2) = P(x_i)×P(x_2)×P(x_3)×P(x_4)
```

Let us note, $w_i[j]$ the $j$'th letter of word $w_i$. So the expression becomes:

```math
P(X_2) = P(w_1[1] == w_3[1])×P(w_2[1] == w_3[1])×P(w_1[2] == w_4[1])×P(w_4[2] == w_2[2])
```

Let us define the event $a_i$ (respectively $b_i, c_i, …, z_i$) the probability to have the letter
a (resp. b, c, …, z) at the position $i$ for a word of length 2 picked in the dictionary.

Assuming that letter frequencies in a word are independent of other letters in the word:

```math
P(X_2) = (P(a_1) × P(a_1) + P(b_1) × P(b_1) + … + P(z_1) × P(z_1)) × \newline
         (P(a_2) × P(a_1) + P(b_2) × P(b_1) + … + P(z_2) × P(z_1)) × \newline
         (P(a_1) × P(a_2) + P(b_1) × P(b_2) + … + P(z_1) × P(z_2)) × \newline
         (P(a_2) × P(a_2) + P(b_2) × P(b_2) + … + P(z_2) × P(z_2))
```

Assuming that letter frequencies are totally positionally independent, i.e. $P(a_i) = f(a)$ for
every letter and word position $i$, where $f(\*)$ is the frequency of letter $\*$ in the word list,
the expression can be simplified to:

```math
P(X_2) = (f(a)^2 + f(b)^2 + … f(z)^2) × \newline
         (f(a)^2 + f(b)^2 + … f(z)^2) × \newline
         (f(a)^2 + f(b)^2 + … f(z)^2) × \newline
         (f(a)^2 + f(b)^2 + … f(z)^2) \newline
P(X_2) = (f(a)^2 + f(b)^2 + … f(z)^2)^4
```

To get the estimated number of valid fills $E_2$ for square grids of size 2, we multiply the
probability of $X_4$ with the number of possible arrangement with repetition of 4 words of length
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
formula for the estimated number of fills $E_n$:

```math
E_n = W^{2n}×p^{n^2}
```

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

> TODO understand that. I think it is closer to the estimation of [Long92] than it looks, if
> you remove the assumption than letter frequencies are positionally independent (right part of the
> formula). It seems to use combinations instead of arrangements with repetition (left part).

### References

* [Harris90]: G.H. Harris, J.J.H. Forster. _On the Bayesian Estimation and Computation of the Number
  of Solutions to Crossword Puzzles_. Symposium on Applied Computry, 1990.
* [Jensen97]: Sik Cambon Jensen, _Design and Implementation of Crossword Compilation Programs_,1997.
* [Long92]: Chris Long. _Mathematics of Square Construction_. Word Ways, 26(1), 1992.
