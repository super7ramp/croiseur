<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## Explanation: How crossword solvers work

### Disclaimer

This is a short, high level and unreviewed explanation of how the solvers available in Croiseur
and elsewhere work. Do not take everything here as necessarily correct. If you are looking for more
thorough explanations, check out the [reference papers](#references).

### Problem Definition

[Jensen97] proposes the following definition:

> Given a word list and a grid configuration a crossword compiler, man or machine should find one or
> more solutions. A solution in this context is a filling of the grid with words all belonging to
> the specified word list.

He also introduces an unconstrained variant, where shaded cells are not an input of the problem but
determined at run-time, by the compiler. This variant, known as *crozzle*, will not be discussed
here.

### Approach

Crossword solving is a *constraint satisfaction problem (CSP)*: Variables (word slots) must be
assigned values (words) satisfying a set of constraints (letters shared by slots).

An alternative approach is to consider that the variables are the letter cells, the values are
the letters and the constraints are that certain groups of letters must form valid words.

Despite the by-letter approach being historically the first successful one ([Mazlack76]), most of
the subsequent works have used the by-word approach. The following explanations are made with a
by-word approach in mind but should be transposable to a by-letter approach.

> — [Jensen97] presents a hybrid, generalised approach where a variable is a _chunk_, which can be a
> letter or a group of letters, possibly an entire word.

Solvers available in Croiseur – at the exception of `croiseur-solver-sat` which works on a more
generalized form of the problem – search for a solution using similar techniques
described notably by [Ginsberg90], [Peterson20] and [Jensen97].

This search can be summarised in four steps:

1. Select the next slot to fill;
2. Choose a value for the selected slot;
3. If a value is present, assign it and continue search; Otherwise, backtrack to a previously
   assigned slot and unassign it.
4. Repeat until all slots are assigned or no possibility is left.

Solvers implement these steps using slightly different heuristics, as described in the next section.

### Heuristics

#### Slot Selection

All slots must be filled at the end so one may be tempted to use a default order like the
numbering of slots. But that might lead to more work in the end.

An efficient strategy is to select *the most constrained slot first*. This can be e.g. by
determining the number of words in dictionary that satisfy the slot constraints, either
initially – the number of words of the length of the slot – or at the moment of variable
selection – the number of words of the length of the slots respecting the letters already set by
crossing slots. [Ginsberg90] suggests the latter is the more effective.

A more primitive strategy is to always select a slot connected to the previously assigned slot.

> — In practice, using the currently most constrained slot as next slot seems to often follows
> connections (the previous assignment constraining significantly the crossing slots).

#### Value Selection

A typical strategy to avoid backtracks is to probe the grid with the candidate value to check if it
is viable, i.e. if the grid has still solutions after filling the selected slot with the candidate
value. Given the nature of the problem, verifying that the crossing slots have still solutions
suffices.

A finer strategy is to compare the viable candidates and select the *least-constraining* one, i.e.
the value leaving the crossing slots with the highest number of candidates. [Ginsberg90]
suggests it is the more effective strategy to avoid backtracks, although it is more costly. A
solution to reduce costs is to only compare a subset of the viable candidates. Experiments suggest
that a limit of 10 elements in the comparison is the best compromise.

This kind of strategies, where one takes a look in the future to make a choice, is called
*look-ahead* or *forward-checking (FC)* [Posser99].

#### Backtrack

> — Backtrack strategies are more complicated to understand or at least to implement correctly.

##### Chronological Backtrack

The simplest backtrack strategy is the chronological one: Take the slot assigned last and give
it another value.

However, such a strategy may:

- Lead to a lot of repeated work (*thrashing* [Posser99]);
- Not be smart enough for solving crosswords ([Ginsberg90])

> — A classic implementation implicitly pushes state on a stack when assigning variables using a
> recursive call, to pop back to the previous state when a dead-end is encountered. Such an
> implementation is elegant but is a bit rigid because it does not allow to (easily) change the
> search order later. It couples the way we move forward with the way we move backwards.

##### Backmark

An improved version of the simple backtrack that stores some information to avoid repeating the
same work, proposed by [Gaschnig79].

> — This strategy is not used in Croiseur. I am not sure whether it can scale for very large
> search spaces such as the ones found in crossword problems.

##### Backjump

Backjump ([Gaschnig79]) is a backtrack to the real source of the dead-end, erasing assignments made
in-between. In the context of crossword problem, it can be implemented by backtracking to a slot
crossing the unassignable slot according to [Ginsberg90].

> — [Ginsberg90] suggests an improved variant called "smart backjump" that identifies the
> problematic letters of the word of the backtracked slot and avoid them when choosing a new word. I
> do not see clearly how it is supposed to be done.

##### Dynamic Backtrack

A kind of backjump which does not erase the values set for the variables jumped over [Ginsberg93].
Relies on an elimination memory for each variable. Effectively changes search order.

> — I am not sure to understand it and as a result I have doubts on the correctness of the
> implementation in `croiseur-solver-ginsberg`. It does not prevent this solver to find solutions
> though, but it might only be due to the other heuristics, for slot selection and value selection.

##### Limited Discrepancy Search

Limited Discrepancy Search (LDS) is a backtrack technique introduced by [Harvey95] which aims to
offer a way to reduce the impact of value selection heuristics limitations. Indeed, a solver can get
stuck exploring a large space with no or few solutions because of a few bad decisions taken early in
the search process. LDS dynamically reorders the search in a way which allows to get back faster to
these early decisions.

> — This technique is not implemented in Croiseur yet but is planned to be. This technique has
> been adapted and implemented in Matt Ginsberg's Dr. Fill software ([Ginsberg11]), whose source
> code is available for educational
> purpose [here](https://github.com/albertkx/Berkeley-Crossword-Solver/tree/drfill).

##### Example

Let us take the following grid. `#` indicates a shaded box while a blank indicates a box to fill.
The digits outside the grid indicate the slot number.

```
     1
     v
2 > | | |#|#|
    | |#|#|#|
3 > | | | |#|
    | |#|#|#|
4 > | | | | |
```

This grid is accompanied by the following dictionary: `{AXCXX,ABCDE,AB,AX,CDE,CXX,EFGH,YYYY}`.
To be clearer, let us represent the possible words per slot:

```
1: {AXCXX,ABCDE}
2: {AB,AX}
3: {CDE,CXX}
4: {EFGH,YYYY}
```

For this example, we will:

- Take a default search order based on slot number;
- Take the values in the order of the dictionary;
- Not do any forward checking.

> — Forward checking makes this example trivial and removes differences between backtrack
> strategies.

Let us now represent our search space as a tree:

```
├── 1:AXCXX
│   ├── 2:AB
│   │   ├── 3:CDE
│   │   │   ├── 4:EFGH
│   │   │   └── 4:YYYY
│   │   └── 3:XXX
│   │       ├── 4:EFGH
│   │       └── 4:YYYY
│   └── 2:AX
│      ├── 3:CDE
│      │   ├── 4:EFGH
│      │   └── 4:YYYY
│      └── 3:CXX
│          ├── 4:EFGH
│          └── 4:YYYY
└── 1:ABCDE
    ├── 2:AB
    │   ├── 3:CDE
    │   │   ├── 4:EFGH
    │   │   └── 4:YYYY
    │   └── 3:CXX
    │       ├── 4:EFGH
    │       └── 4:YYYY
    └── 2:AX
        ├── 3:CDE
        │   ├── 4:EFGH
        │   └── 4:YYYY
        └── 3:CXX
            ├── 4:EFGH
            └── 4:YYYY
```

Let us see how the chronological backtrack, backjump and dynamic backtrack strategies handle the
situation, beginning with chronological backtrack:

```
-- Assigning 1: AXCXX

     1
     v
2 > |A| |#|#|         | # | Current | Candidates  | New    |                         
    |X|#|#|#|         | 1 |         | AXCXX,ABCDE | AXCXX  | 
3 > |C| | |#|         | 2 |         | AB,AX       |        | 
    |X|#|#|#|         | 3 |         | CDE,CXX     |        | 
4 > |X| | | |         | 4 |         | EFGH,YYYY   |        |

-- Because we do not have an intelligent search order and no forward check, we do not see that 4 is 
-- not assignable anymore and this is a dead-end. We will do a lot of useless tries since we have 
-- not spotted this issue.

-- Assigning 2: AB

     1                                                             
     v                                                             
2 > |A|B|#|#|         | # | Current | Candidates  | New    |       
    |X|#|#|#|         | 1 | AXCXX   | AXCXX,ABCDE | AXCXX  |       
3 > |C| | |#|         | 2 |         | AB,AX       | AB     |       
    |X|#|#|#|         | 3 |         | CDE,CXX     |        |       
4 > |X| | | |         | 4 |         | EFGH,YYYY   |        |

-- Assigning 3: CDE

     1                                                             
     v                                                             
2 > |A|B|#|#|         | # | Current | Candidates  | New    |       
    |X|#|#|#|         | 1 | AXCXX   | AXCXX,ABCDE | AXCXX  |       
3 > |C|D|E|#|         | 2 | AB      | AB,AX       | AB     |       
    |X|#|#|#|         | 3 |         | CDE,CXX     | CDE    |       
4 > |X| | | |         | 4 |         | EFGH,YYYY   |        |

-- Assigning 4: Both YYYY and EFGH fail
-- Backtracking to last slot 3
-- Assigning slot 3: CXX

     1                                                             
     v                                                             
2 > |A|B|#|#|         | # | Current | Candidates  | New    |       
    |X|#|#|#|         | 1 | AXCXX   | AXCXX,ABCDE | AXCXX  |       
3 > |C|X|X|#|         | 2 | AB      | AB,AX       | AB     |       
    |X|#|#|#|         | 3 | CDE     | (CDE) CXX   | CXX    |       
4 > |X| | | |         | 4 |         | EFGH,YYYY   |        |

-- Assigning 4: Both YYYY and EFGH fail
-- Backtracking to last slot 3: No more values left.
-- Backtracking to last slot 2
-- Assigning 2: AX

     1                                                             
     v                                                             
2 > |A|X|#|#|         | # | Current | Candidates  | New    |       
    |X|#|#|#|         | 1 | AXCXX   | AXCXX,ABCDE | AXCXX  |       
3 > |C| | |#|         | 2 | AB      | (AB) AX     | AX     |       
    |X|#|#|#|         | 3 |         | CDE, CXX    |        |       
4 > |X| | | |         | 4 |         | EFGH,YYYY   |        |

-- Assigning 3: CDE

     1                                                             
     v                                                             
2 > |A|X|#|#|         | # | Current | Candidates  | New    |       
    |X|#|#|#|         | 1 | AXCXX   | AXCXX,ABCDE | AXCXX  |       
3 > |C|D|E|#|         | 2 | AX      | (AB) AX     | AX     |       
    |X|#|#|#|         | 3 |         | CDE, CXX    | CDE    |       
4 > |X| | | |         | 4 |         | EFGH,YYYY   |        |

-- Assigning 4: Both YYYY and EFGH fail
-- Backtracking to last slot 3
-- Assigning slot 3: CXX

     1                                                             
     v                                                             
2 > |A|X|#|#|         | # | Current | Candidates  | New    |       
    |X|#|#|#|         | 1 | AXCXX   | AXCXX,ABCDE | AXCXX  |       
3 > |C|X|X|#|         | 2 | AX      | (AB) AX     | AX     |       
    |X|#|#|#|         | 3 | CDE     | (CDE) CXX   | CXX    |       
4 > |X| | | |         | 4 |         | EFGH,YYYY   |        |

-- Assigning 4: Both YYYY and EFGH fail
-- Backtracking to last slot 3: No more values left.
-- Backtracking to last slot 2: No more values left.
-- Backtracking to last slot 1 (at last).
-- Assigning 1: ABCDE

     1
     v
2 > |A| |#|#|         | # | Current | Candidates    | New    |                         
    |B|#|#|#|         | 1 |         | (AXCXX) ABCDE | ABCDE  | 
3 > |C| | |#|         | 2 |         | AB, AX        |        | 
    |D|#|#|#|         | 3 |         | CDE, CXX      |        | 
4 > |E| | | |         | 4 |         | EFGH,YYYY     |        |

-- Assigning 2: AB
-- Assigning 3: CDE
-- Assigning 4: EFGH
-- All slots filled

     1         
     v         
2 > |A|B|#|#|  
    |B|#|#|#|  
3 > |C|D|E|#|  
    |D|#|#|#|  
4 > |E|F|G|H|   
```

As expected, simple backtrack (coupled with no intelligence on the slot selection and the way we
verify constraints) takes a lot of steps.

Basically we have covered this part of the search tree entirely:

```
├── 1:AXCXX
│   ├── 2:AB
│   │   ├── 3:CDE
│   │   │   ├── 4:EFGH
│   │   │   └── 4:YYYY
│   │   └── 3:XXX
│   │       ├── 4:EFGH
│   │       └── 4:YYYY
│   └── 2:AX
│      ├── 3:CDE
│      │   ├── 4:EFGH
│      │   └── 4:YYYY
│      └── 3:CXX
│          ├── 4:EFGH
│          └── 4:YYYY
└── 1:ABCDE
    └── 2:AB
        └── 3:CDE
            └── 4:EFGH
```

Now let us look at how backjump would handle this situation. Backjump is able to determine that
slots 2 and 3 have no influence on 4: They do not cross 4. But 1 is, so it is the backjump point
to target.

```
-- Assigning 1: AXCXX
-- Assigning 2: AB
-- Assigning 3: CDE

     1                                                             
     v                                                             
2 > |A|B|#|#|         | # | Current | Candidates  | New    |       
    |X|#|#|#|         | 1 | AXCXX   | AXCXX,ABCDE | AXCXX  |       
3 > |C|D|E|#|         | 2 | AB      | AB,AX       | AB     |       
    |X|#|#|#|         | 3 |         | CDE,CXX     | CDE    |       
4 > |X| | | |         | 4 |         | EFGH,YYYY   |        |

-- Assigning 4: Both YYYY and EFGH fail
-- Backjumping to latest connected slot 1
-- Assigning 1: ABCDE

     1                                                             
     v                                                             
2 > |A| |#|#|         | # | Current | Candidates    | New    |       
    |B|#|#|#|         | 1 | AXCXX   | (AXCXX) ABCDE | ABCDE  |       
3 > |C| | |#|         | 2 | AB      | AB,AX         |        |       
    |D|#|#|#|         | 3 | CDE     | CDE,CXX       |        |       
4 > |E| | | |         | 4 |         | EFGH,YYYY     |        |

-- Then no problem to find solution
```

With this strategy, here are the nodes which have been visited from the search tree:

```
├── 1:AXCXX
│   └── 2:AB
│       └── 3:CDE
│           ├── 4:EFGH
│           └── 4:YYYY
└── 1:ABCDE
    └── 2:AB
        └── 3:CDE
            └── 4:EFGH
```

This is significantly less.

Dynamic backtracking pushes the idea further by keeping values of nodes which have been jumped
over:

```
-- Assigning 1: AXCXX
-- Assigning 2: AB
-- Assigning 3: CDE

     1                                                             
     v                                                             
2 > |A|B|#|#|         | # | Current | Candidates  | New    |       
    |X|#|#|#|         | 1 | AXCXX   | AXCXX,ABCDE | AXCXX  |       
3 > |C|D|E|#|         | 2 | AB      | AB,AX       | AB     |       
    |X|#|#|#|         | 3 |         | CDE,CXX     | CDE    |       
4 > |X| | | |         | 4 |         | EFGH,YYYY   |        |

-- Assigning 4: Both YYYY and EFGH fail
-- Dynamically backtracking to latest connected slot 1
-- Assigning 1: ABCDE

     1                                                             
     v                                                             
2 > |A|B|#|#|         | # | Current | Candidates    | New    |       
    |B|#|#|#|         | 1 | AXCXX   | (AXCXX) ABCDE | ABCDE  |       
3 > |C|D|E|#|         | 2 | AB      | AB,AX         | AB     |       
    |D|#|#|#|         | 3 | CDE     | CDE,CXX       | CDE    |       
4 > |E| | | |         | 4 |         | EFGH,YYYY     |        |

-- Assigning 4: EFGH, done.
```

With this strategy, the search order has actually been modified:

```
   1:AXCXX  ---------------------------\
    └── 2:AB                           |
        └── 3:CDE                      |
            ├── 4:EFGH  -- invalid     | reordered
            ├── 4:YYYY  -- invalid     |
            └── 1:ABCDE <--------------/
                └── 4:EFGH
```

### Solver Comparison

Here is a comparison between solvers available in Croiseur:

| Solvers ↓ Characteristics → | Variable Kind | Variable Selection                                                      | Value Selection    | Backtrack     |
|-----------------------------|---------------|-------------------------------------------------------------------------|--------------------|---------------|
| Crossword Composer          | Letter        | Max by word constraint length then by number of assigned crossing words | First satisfying   | Chronological |
| Ginsberg                    | Word          | Min by current number of possible values                                | Least constraining | Dynamic       |
| XWords RS                   | Word          | Min by current number of possible values                                | First viable       | Chronological |

### Other Approaches

#### Integer Programming (or Integer Optimization)

[Wilson89] used Integer Programming but concluded at that time that other, faster, approaches may be
preferable.

This approach has been revisited recently.

An example can be found in [Morse23]. An interesting point is that it shows that Integer Programming
allows to easily express a preference in the words to select, which is something apparently
desirable for professional grids: Word lists often come with a preference score attached to each
word.

In the same vein, [Olivier23] shows in great depth the mental process for modelling the problem and
configuring the solver.

Finally, [Solvermax24] discusses the speed improvements of both hardware and software solvers
since Wilson's 1989 article, then presents their own models and results.

#### SAT Solving

> — There seems to be a lot of similarities between Integer Programming and SAT solving. SAT
> problems look like specific cases of Integer Programming problems.

SAT solving refers to solving a boolean satisfiability problem, i.e. a problem that can be expressed
as a boolean formula.

A typical form for boolean formula is the conjunctive normal form (CNF), which is a conjunction (=
and) of disjunctions (= or) of boolean variables, e.g.:

```math
(x_1 \vee \neg x_2) \wedge (\neg x_1 \vee x_2 \vee x_3) \wedge \neg x_1
```

A crossword problem can be expressed as a boolean formula.

For example, one can define the following variables:

- Cell variables: One for each pair $(cell,value)$ where $cell$ is a cell of the input
  crossword grid and $value$ is a letter of the alphabet or a block. A variable set to true
  means that the cell represented by this variable contains the value represented by this
  variable.
- Slot variables: One for each pair $(slot,word)$ where $slot$ is a slot of the input crossword
  grid (i.e. a set of contiguous cells) and $word$ a word of the input word list. A variable set to
  true means that the slot represented by this variable contains the word represented by this
  variable.

And here are the constraints:

- Each cell must contain one and only one letter from the alphabet or a block, i.e. for each cell
  $c$, assuming a Latin alphabet and $'\#'$ as block character:

```math
exactlyOne(cellVariable(c,'A'), cellVariable(c, 'B'), ..., cellVariable(c, 'Z'), cellVariable(c, '\#'))
```

- Each slot must contain one and only one word from the input word list, i.e. for each slot $s$
  and with the word list $\left\{ w_1,w_2,...,w_n \right\}$:

```math
exactlyOne(slotVariable(s, w_1), slotVariable(s, w_2), ..., slotVariable(s, w_n))
```

- Each slot variable is equivalent to a conjunction of cell variables, i.e. for each slot $s$
  and word $w$ of same length $l$, noting $s[i]$ the i-th cell in slot $s$ and $w[i]$ the i-th
  letter in word $w$:

```math
and(slotVariable(s, w), cellVariable(s[1],w[1]), cellVariable(s[2],w[2]), ..., cellVariable(s[l],w[l])
```

- Prefilled cells must be kept as is, i.e. for each cell $c$ prefilled with a value $v$,
  $cellVariable(c,v)$ must be true.

Because expressing this directly in CNF is a bit complicated, the previous definitions used the
following functions:

- $cellVariable(cell,value)$: Returns the variable corresponding to the given $(cell,value)$ pair,
  implementation detail.
- $slotVariable(slot,word)$: Returns the variable corresponding to the given $(slot,word)$ pair,
  implementation detail.
- $exactlyOne(x_1, x_2, ..., x_n)$: Exactly one literal is true. The CNF form is:

```math
\left(\bigvee_{i \in 1..n} x_i \right) \wedge \left(\bigwedge_{i \in 1..n,j \in 1..n, i < j } \neg x_i \vee \neg x_j \right)
```

- $and(y, x_1, x_2, ..., x_n)$: Make $y$ equivalent to the conjunction of $x_i$, i.e.
  $y \Leftrightarrow x_1 \wedge x_2 \wedge ... \wedge x_n$. The CNF form is:

```math
(\neg y \vee x_1) \wedge (\neg y \vee x_2) \wedge ... \wedge (\neg y \vee x_n) \wedge (\neg x_1 \vee \neg x_2 \vee ... \vee \neg x_n \vee y)
```

`croiseur-solver-sat` feeds a SAT solver provided by Sat4j with such a formula. The selected
Sat4j solver relies on Conflict Driven Clause Learning (CDCL) and pseudo-boolean solving
algorithms [Leberre10] to find a solution.

#### Neural Network

The [Dr. Fill/Berkeley Crossword Solver](https://berkeleycrosswordsolver.com/) is a solver combining
Neural-Network (NN) answer generation based on provided clues with traditional search
techniques [Wallace22]. Excellent to solve an already created grid, but not usable to create a new
crossword grid as it uses clues as inputs.

### References

* [Gaschnig79]: John Gaschnig. *Performance Measurement and Analysis of Certain Search Algorithms*.
  Technical Report CMU-CS-79-124, Carnegie-Mellon University, 1979.
* [Ginsberg90]: Matthew Ginsberg, Michael Frank, Michael P. Halpin and Mark C. Torrance. "Search
  Lessons Learned from Crossword Puzzles", _AAAI_, 1990.
* [Ginsberg93]: Matthew Ginsberg. "Dynamic Backtracking", _Journal of Artificial Intelligence
  Research_, 1 (1993) 25-46.
* [Ginsberg11]: Matthew Ginsberg, "Dr.Fill: Crosswords and an Implemented Solver for Singly Weighted
  CSPs", _Journal of Artificial Intelligence Research_, 42 (2011) 851-886.
* [Harvey95]: William Harvey, Matthew Ginsberg, "Limited Discrepancy Search", _IJCAI'95: Proceedings
  of the 14th international joint conference on Artificial intelligence_, Volume 1, 1995, 607–613.
* [Jensen97]: Sik Cambon Jensen, _Design and Implementation of Crossword Compilation Programs_, 1997.
* [Leberre10]: Daniel Leberre, Anne Parrain. "The Sat4j library, release 2.2". _Journal on
  Satisfiability, Boolean Modeling and Computation 7_, (2010) 59-64.
* [Mazlack76]: Lawrence Mazlack. "Computer construction of crossword puzzles using precedence
  relationships". _Artificial Intelligence_, 7:1-19, 1976.
* [Morse23]: Steven Morse,
  [Making crossword puzzles with integer programming](https://stmorse.github.io/journal/IP-Crossword-puzzles.html),
  blog post, 2023.
* [Olivier23], Philippe
  Olivier, [Generating Crossword Grids Using Constraint Programming](https://pedtsr.ca/2023/generating-crossword-grids-using-constraint-programming.html),
  blog post, 2023.
* [Peterson20]: Otis Peterson and Michael
  Wehar [crosswordconstruction.com](https://www.crosswordconstruction.com/).
* [Posser99]: Patrick Posser. "Hybrid Algorithms for the Constraint Satisfaction Problem",
  _Computational Intelligence_, Volume 9, Number 3, 1999.
* [Solvermax24]: Solver Max,
  [Solver performance: 1989 vs 2024](https://www.solvermax.com/blog/crossword-milp-model-1),
  blog post, 2024.
* [Wallace22]: Eric Wallace, Nicholas Tomlin, Albert Xu, Kevin Yang, Eshaan Pathak, Matthew
  Ginsberg, Dan Klein. "Automated Crossword Solving", _arXiv:2205.09665_, 2022.
* [Wilson89]: J.M. Wilson, _Crossword Compilation using Integer Programming_, 1989.
