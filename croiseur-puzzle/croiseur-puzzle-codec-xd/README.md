<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## croiseur-puzzle-xd-codec

`croiseur-puzzle-xd-codec` is a library which allows to read/write puzzles in
the [xd](https://github.com/century-arcade/xd) format.

```
Title: New York Times, Saturday, January 1, 1955
Author: Anthony Morse
Editor: Margaret Farrar
Rebus: 1=HEART 2=DIAMOND 3=SPADE 4=CLUB
Date: 1955-01-01


1ACHE#ADAM#2LIL
BLUER#GULL#MATA
EATIN#APEX#ICER
ATAR#TILE#SNEAK
TEN#MANI#ITE###
##DRUB#CANASTAS
FADED#BAGGY#OIL
ONES#KATES#TUNA
ETA#JOKER#JORUM
SILLABUB#SOON##
###ACE#RUIN#ARK
3WORK#JINX#4MAN
BIRD#WADS#SCENE
ISLE#EDGE#PANEL
DEER#BEET#ARTEL


A1. Sadness. ~ HEARTACHE
A6. Progenitor. ~ ADAM
A10. Mae West stand-by. ~ DIAMONDLIL
[...]

D1. Vital throb. ~ HEARTBEAT
D2. Having wings. ~ ALATE
D3. Start the card game. ~ CUTANDDEAL
[...]
```

### Limitations

See [TODO.md](TODO.md).

### References

* [The xd format specification](https://github.com/century-arcade/xd/blob/059c2eca6917cd94c0a61199198b96e8aa80f6db/doc/xd-format.md)
* [xdfile.py](https://github.com/century-arcade/xd/blob/059c2eca6917cd94c0a61199198b96e8aa80f6db/xdfile/xdfile.py),
  the reference parser written in Python
