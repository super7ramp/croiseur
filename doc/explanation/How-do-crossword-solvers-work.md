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
same work, proposed by [Gaschnig79].

##### Backjump

Backjump ([Gaschnig79]) is a backtrack to the real source of the dead-end, erasing assignments made
in-between. In the context of crossword problem, it can be implemented by backtracking to a slot
crossing the unassignable slot according to [Ginsberg90].

> — [Ginsberg90] suggests an improved variant called "smart backjump" that identifies the
> problematic letters of the word of the backtracked slot and avoid them when choosing a new word. I
> do not see clearly how it is supposed to be done.

##### Dynamic Backtrack

A kind of backjump which does not erase the values set for the variables jumped over. Relies on
an elimination memory for each variable. Effectively changes search order.

> — Honestly it feels a bit magical. I am not confident at all that `croiseur-solver-ginsberg`
> implementation is correct.

##### Example

Let us take the following grid. `#` indicates a shaded box while a blank indicates a box to fill.
The digits outside the grid indicate the slot number.

```
       4       2
       v       v
1 > | | | | | | |
    |#| |#|#|#| |
    |#| |#|#|#| |
    |#| |#|#|#| |
    |#| |#|#|#| |
3 > |#| | | | | |
```

Let us also assume our dictionary
is `{ABCDEF,ATCDEF,FGHIJK,TSRQPO,BSRQPX,ONMLK,ONMLJ,ONMLI,ONMLH,ONMLG}` and that we are going to
naively iterate over slots using slot numbers and iterating over word candidates using the
dictionary order.

Let us proceed until a dead end is reached:

```
-- Assigning slot #1:

       4       2  
       v       v      
1 > | | | | | | |         | # | Current | Candidates                             | New    |
    |#| |#|#|#| |         | 1 |         | ABCDEF, ATCDEF, FGHIJK, TSRQPO, BSRQPX | ABCDEF |
    |#| |#|#|#| |         | 2 |         | ABCDEF, ATCDEF, FGHIJK, TSRQPO, BSRQPX |        |
    |#| |#|#|#| |         | 3 |         | ONMLK, ONMLJ, ONMLI, ONMLH, ONMLG      |        |
    |#| |#|#|#| |         | 4 |         | ABCDEF, ATCDEF, FGHIJK, TSRQPO, BSRQPX |        |
3 > |#| | | | | |          

-- Assigning slot #2

       4       2
       v       v
1 > |A|B|C|D|E|F|         | # | Current | Candidates                             | New    |
    |#| |#|#|#| |         | 1 | ABCDEF  | ABCDEF                                 | ABCDEF |
    |#| |#|#|#| |         | 2 |         | FGHIJK                                 | FGHIJK |
    |#| |#|#|#| |         | 3 |         | ONMLK, ONMLJ, ONMLI, ONMLH             |        |
    |#| |#|#|#| |         | 4 |         | BSRQPX                                 |        |
3 > |#| | | | | |

-- Assigning slot #3

       4       2
       v       v
1 > |A|B|C|D|E|F|         | # | Current | Candidates                             | New    |
    |#| |#|#|#|G|         | 1 | ABCDEF  | ABCDEF                                 | ABCDEF |
    |#| |#|#|#|H|         | 2 | FGHIJK  | FGHIJK                                 | FGHIJK |
    |#| |#|#|#|I|         | 3 |         | ONMLK                                  | ONMLK  |
    |#| |#|#|#|J|         | 4 |         | BSRQPX                                 |        |
3 > |#| | | | |K|

-- Cannot assign slot #4

       4       2  
       v       v  
1 > |A|B|C|D|E|F|         | # | Current | Candidates                             | New    |
    |#| |#|#|#|G|         | 1 | ABCDEF  | ABCDEF                                 | ABCDEF |
    |#| |#|#|#|H|         | 2 | FGHIJK  | FGHIJK                                 | FGHIJK |
    |#| |#|#|#|I|         | 3 | ONMLK   | ONMLK                                  | ONMLK  |
    |#| |#|#|#|J|         | 4 |         | (none, dead-end)                       | !!!!!! |
3 > |#|O|N|M|L|K| 
```

Slot 4 cannot be assigned a value. The problem actually comes from the slot 1, because of the
constraint it puts on slot 4. One should have chosen `ATCDEF` so that the grid could be filled
like this:

```
       4       2                4       2                4       2                4       2 
       v       v                v       v                v       v                v       v 
1 > |A|T|C|D|E|F|        1 > |A|T|C|D|E|F|        1 > |A|T|C|D|E|F|        1 > |A|T|C|D|E|F|
    |#| |#|#|#| |            |#| |#|#|#|G|            |#| |#|#|#|G|            |#|S|#|#|#|G|
    |#| |#|#|#| |            |#| |#|#|#|H|            |#| |#|#|#|H|            |#|R|#|#|#|H|
    |#| |#|#|#| |            |#| |#|#|#|I|            |#| |#|#|#|I|            |#|Q|#|#|#|I|
    |#| |#|#|#| |            |#| |#|#|#|J|            |#| |#|#|#|J|            |#|P|#|#|#|J|
3 > |#| | | | | |        3 > |#| | | | |K|        3 > |#|O|N|M|L|K|        3 > |#|O|N|M|L|K|
```

Let us see how the different backtrack strategies deal with this situation.

With simple backtrack, the algorithm repeatedly backtracks to the previous slot. The dead-end
propagates till the source of the problem, slot #1:

```
-- Unassigning slot #3

       4       2                                                                              
       v       v                                                                              
1 > |A|B|C|D|E|F|         | # | Current | Candidates                              | New    |
    |#| |#|#|#|G|         | 1 | ABCDEF  | ABCDEF                                  | ABCDEF |
    |#| |#|#|#|H|         | 2 | FGHIJK  | FGHIJK                                  | FGHIJK |
    |#| |#|#|#|I|         | 3 | ONMLK   | ONMLK                                   |        |
    |#| |#|#|#|J|         | 4 |         | (none, dead-end)                        |        |
3 > |#|O|N|M|L|K|                                                                             

-- Unassigning slot #2

       4       2
       v       v
1 > |A|B|C|D|E|F|         | # | Current | Candidates                              | New    |
    |#| |#|#|#|G|         | 1 | ABCDEF  | ABCDEF                                  | ABCDEF |
    |#| |#|#|#|H|         | 2 | FGHIJK  | FGHIJK                                  |        |
    |#| |#|#|#|I|         | 3 |         | (none, dead-end)                        |        |
    |#| |#|#|#|J|         | 4 |         | BSRQPX                                  |        |
3 > |#| | | | |K|

-- Unassigning slot #1 - ABCDEF definitely eliminated

       4       2
       v       v
1 > |A|B|C|D|E|F|         | # | Current | Candidates                              | New    |
    |#| |#|#|#| |         | 1 | ABCDEF  | ABCDEF                                  |        |
    |#| |#|#|#| |         | 2 |         | (none, dead-end)                        |        |
    |#| |#|#|#| |         | 3 |         | ONMLK, ONMLJ, ONMLI, ONMLH              |        |
    |#| |#|#|#| |         | 4 |         | BSRQPX                                  |        |
3 > |#| | | | | |

-- Assigning slot #1

       4       2
       v       v
1 > | | | | | | |         | # | Current | Candidates                              | New    |
    |#| |#|#|#| |         | 1 |         | ATCDEF, FGHIJK, TSRQPO, BSRQPX          | ATCDEF |
    |#| |#|#|#| |         | 2 |         | ABCDEF, ATCDEF, FGHIJK, TSRQPO, BSRQPX  |        |
    |#| |#|#|#| |         | 3 |         | ONMLK, ONMLJ, ONMLI, ONMLH              |        |
    |#| |#|#|#| |         | 4 |         | BSRQPX                                  |        |
3 > |#| | | | | |

[Then no problem to fill the grid as described above.]
```

Backjump avoids this repeated backtrack by directly going to the source of the problem found in slot
4: The value of the crossing slot 1.

```
-- Backjumping to 1, unassigning slot 1 to 3 - ABCDEF definitely eliminated

       4       2                                                                              
       v       v                                                                              
1 > |A|B|C|D|E|F|         | # | Current | Candidates              | New    |                  
    |#| |#|#|#|G|         | 1 | ABCDEF  | ABCDEF                  |        | < dead-end cause
    |#| |#|#|#|H|         | 2 | FGHIJK  | FGHIJK                  |        |                  
    |#| |#|#|#|I|         | 3 | ONMLK   | ONMLK                   |        |                  
    |#| |#|#|#|J|         | 4 |         | (none, dead-end)        |        |        
3 > |#|O|N|M|L|K|

-- Assigning slot 1

       4       2
       v       v
1 > | | | | | | |         | # | Current | Candidates                              | New    |
    |#| |#|#|#| |         | 1 |         | ATCDEF, FGHIJK, TSRQPO, BSRQPX          | ATCDEF |
    |#| |#|#|#| |         | 2 |         | ABCDEF, ATCDEF, FGHIJK, TSRQPO, BSRQPX  |        |
    |#| |#|#|#| |         | 3 |         | ONMLK, ONMLJ, ONMLI, ONMLH              |        |
    |#| |#|#|#| |         | 4 |         | BSRQPX                                  |        |
3 > |#| | | | | |

[Then no problem to fill the grid as described above.]
```

Dynamic backtrack tries to be smarter and say: "I can determine that slot 1 is the source of the
problem, and I don't want to lose the work done for 2 and 3, so I'll just unassign #1 and keep
in mind that I have eliminated its value because of this situation".

```
-- Unassigning slot #1 - ABCDEF eliminated because of slot #3 

       4       2                                                                              
       v       v                                                                              
1 > |A|B|C|D|E|F|         | # | Current | Candidates              | New    |                  
    |#| |#|#|#|G|         | 1 | ABCDEF  | ABCDEF                  |        | < dead-end cause                 
    |#| |#|#|#|H|         | 2 | FGHIJK  | FGHIJK                  | FGHIJK |                  
    |#| |#|#|#|I|         | 3 | ONMLK   | ONMLK                   | ONMLK  |                  
    |#| |#|#|#|J|         | 4 |         | (none, dead-end)        |        |        
3 > |#|O|N|M|L|K|

-- Assigning slot #1

       4       2                                                                              
       v       v                                                                              
1 > | | | | | |F|         | # | Current | Candidates              | New    |                 
    |#| |#|#|#|G|         | 1 |         | ATCDEF                  | ATCDEF | 
    |#| |#|#|#|H|         | 2 | FGHIJK  | FGHIJK                  | FGHIJK |                  
    |#| |#|#|#|I|         | 3 | ONMLK   | ONMLK                   | ONMLK  |                  
    |#| |#|#|#|J|         | 4 |         | BSRQPX, TSRQPO          |        |        
3 > |#|O|N|M|L|K|

[Then no problem to assign slot #4 with TSRQPO]
```

Note that here elimination of `ABCDEF` for slot 1 because of slot 3 is not definitive: It is
valid until slot 1 is unassigned (which does not occur here).

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
