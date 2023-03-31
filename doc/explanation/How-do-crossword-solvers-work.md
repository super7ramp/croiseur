<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## Explanation: How do crossword solvers work?

### Disclaimer

This is a short, high level and unreviewed explanation of how the solvers embedded in **croiseur**
work. Do not take everything here as necessarily correct. If you are looking for more thorough
explanations, check out the [reference papers](#references).

### Generalities

Crossword solving is a constraint satisfaction problem: Variables (word slots) must be assigned
values (words) satisfying a set of constraints (letters shared by slots).

Solvers currently available in **croiseur** search for a solution using similar approaches
described notably by [Ginsberg90] or [Peterson20].

This search can be summarised in three steps:

1. Select the next slot to fill;
2. Choose a value for the selected slot;
3. If a value is present, assign it and continue search; Otherwise, backtrack to a previously
   assigned slot, choose another value for it then continue search.

Solvers implement these steps using slightly different heuristics, as described in next section.

### Heuristics

#### Slot Selection

All slots must be filled at the end so one may be tempted to use a default order like the
numbering of slots. But that might lead to more work in the end.

The typical strategy is to select *the most constrained slot first*. This can be e.g. by
determining the number of words in dictionary that satisfy the slot constraints, either
initially – the number of words of the length of the slot – or at the moment of variable
selection – the number of words of the length of the slots respecting the letters already set by
crossing slots. [Ginsberg90] suggests the latter is the more effective.

A more primitive strategy is to always select a slot connected to the previously assigned slot.

> — In practice, using the currently most constrained slot as next slot seems to often follows
> connections (the previous assignment constraining significantly the crossing slots).

#### Word Selection

A typical strategy to avoid backtracks is to probe the grid with the candidate word to check if it
is viable, i.e. if the grid has still solutions after filling the selected slot with the
candidate word. Given the nature of the problem, verifying that the crossed slots have still
solutions suffices.

A finer strategy is to compare the viable word candidates and select the *least-constraining*
one, i.e. the word leaving the crossing slots with the highest number of candidates. [Ginsberg90]
suggests it is the more effective strategy to avoid backtracks, although it is more costly. In
order to reduce costs, only the 10 first word candidates may be used in the comparison.

This kind of strategies, where one takes a look in the future to make a choice, is called
*forward-checking (FC)* [Posser99].

#### Backtrack

Backtrack strategies are the most complicated to understand or at least to implement correctly.

##### Chronological Backtrack

The simplest backtrack strategy is the chronological one: Take the latest assigned slot and give
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
same work, proposed by [Gaschnig79]. This strategy is not used in **croiseur**.

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

Let us see how the different strategies handles the situation, beginning with simple
chronological backtrack:

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
-- not spotted that.

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

-- Assigning 4: Both YYYY and EFGH fails
-- Backtracking to last slot 3
-- Assigning slot 3: CXX

     1                                                             
     v                                                             
2 > |A|B|#|#|         | # | Current | Candidates  | New    |       
    |X|#|#|#|         | 1 | AXCXX   | AXCXX,ABCDE | AXCXX  |       
3 > |C|X|X|#|         | 2 | AB      | AB,AX       | AB     |       
    |X|#|#|#|         | 3 | CDE     | (CDE) CXX   | CXX    |       
4 > |X| | | |         | 4 |         | EFGH,YYYY   |        |

-- Assigning 4: Both YYYY and EFGH fails
-- Backtracking to last slot 3: No more values left.
-- Backtracking to last slot 2
-- Assigning 2: AX

     1                                                             
     v                                                             
2 > |A|B|#|#|         | # | Current | Candidates  | New    |       
    |X|#|#|#|         | 1 | AXCXX   | AXCXX,ABCDE | AXCXX  |       
3 > |C|X|X|#|         | 2 | AB      | (AB) AX     | AX     |       
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

-- Assigning 4: Both YYYY and EFGH fails
-- Backtracking to last slot 3
-- Assigning slot 3: CXX

     1                                                             
     v                                                             
2 > |A|X|#|#|         | # | Current | Candidates  | New    |       
    |X|#|#|#|         | 1 | AXCXX   | AXCXX,ABCDE | AXCXX  |       
3 > |C|X|X|#|         | 2 | AX      | (AB) AX     | AX     |       
    |X|#|#|#|         | 3 | CDE     | (CDE) CXX   | CXX    |       
4 > |X| | | |         | 4 |         | EFGH,YYYY   |        |

-- Assigning 4: Both YYYY and EFGH fails
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

-- Assigning 4: Both YYYY and EFGH fails
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

That is significantly less.

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

-- Assigning 4: Both YYYY and EFGH fails
-- Dynamically backtracking to latest connected slot 1
-- Assigning 1: ABCDE

     1                                                             
     v                                                             
2 > |A| |#|#|         | # | Current | Candidates    | New    |       
    |B|#|#|#|         | 1 | AXCXX   | (AXCXX) ABCDE | ABCDE  |       
3 > |C| | |#|         | 2 | AB      | AB,AX         | AB     |       
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

#### Solvers comparison

Here is a comparison between solvers available in **croiseur**:

| Solvers ↓ Steps →  | Variable selection         | Value selection    | Backtrack     |
|--------------------|----------------------------|--------------------|---------------|
| Ginsberg           | Currently most constrained | Least constraining | Dynamic       |
| Crossword Composer | Initially most constrained | First viable       | Chronological |
| xwords-rs          | Initially most constrained | First viable       | Chronological |

### Other solving techniques

#### Neural Network

The [Dr. Fill/Berkeley Crossword Solver](https://berkeleycrosswordsolver.com/) is a solver combining
Neural-Network (NN) answer generation based on provided clues with traditional search
techniques [Wallace22].

#### Integer Programming

Unsure whether this has been explored a lot but an example can be
found [in this blog post](https://stmorse.github.io/journal/IP-Crossword-puzzles.html).

### References

* [Gaschnig79]: John Gaschnig. *Performance Measurement and Analysis of Certain Search Algorithms*.
  Technical Report CMU-CS-79-124, Carnegie-Mellon University, 1979.
* [Ginsberg90]: Matthew Ginsberg, Michael Frank, Michael P. Halpin and Mark C. Torrance. "Search
  Lessons Learned from Crossword Puzzles", _AAAI_, 1990.
* [Ginsberg93]: Matthew Ginsberg. "Dynamic Backtracking", _Journal of Artificial Intelligence
  Research_, 1 (1993) 25-46.
* [Peterson20]: Otis Peterson and Michael
  Wehar [crosswordconstruction.com](https://www.crosswordconstruction.com/).
* [Posser99]: Patrick Posser. "Hybrid Algorithms for the Constraint Satisfaction Problem",
  _Computational Intelligence_, Volume 9, Number 3, 1999.
* [Wallace22]: Eric Wallace, Nicholas Tomlin, Albert Xu, Kevin Yang, Eshaan Pathak, Matthew
  Ginsberg, Dan Klein. "Automated Crossword Solving", _arXiv:2205.09665_, 2022.
