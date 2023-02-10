# SPDX-FileCopyrightText: 2023 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

Feature: Display Dictionary Content

  Word lists - or "dictionaries" in croiseur jargon - used for crossword generation are provided
  via modules called "dictionary providers". The application offers a way to display the content
  of the dictionaries provided by these providers.

  # TODO Shouldn't be better if all the entries were sorted?

  Scenario: Display Dictionary Content - Local Hunspell Provider, Hunspell Dictionary en_GB.dic

    When user requests to display the entries of "Hunspell Dictionary en_GB.dic" provided by "Local Hunspell Provider"
    Then the application presents 274371 dictionary entries, the first ones being:
      | GAINST     |
      | MONGST     |
      | NEATH      |
      | STREWTH    |
      | STRUTH     |
      | TIS        |
      | TWAS       |
      | TWEENDECKS |

  Scenario: Display Dictionary Content - Local Text Provider, The UK Advanced Cryptics Dictionary

    When user requests to display the entries of "The UK Advanced Cryptics Dictionary" provided by "Local Text Provider"
    Then the application presents 250568 dictionary entries, the first ones being:
      | AA         |
      | AACHEN     |
      | AARDVARK   |
      | AARDVARKS  |
      | AARDWOLF   |
      | AARDWOLVES |
      | AARHUS     |
      | AARON      |

  Scenario: Display Dictionary Content - Local XML Provider, General British English dictionary

    When user requests to display the entries of "General British English dictionary" provided by "Local XML Provider"
    Then the application presents 224608 dictionary entries, the first ones being:
      | ELECTROSTRICTIONS |
      | FROWNING          |
      | UNDERMINING       |
      | DEIPNOSOPHISTS    |
      | ANAMORPHOSIS      |
      | DECIPHERERS       |
      | COLLOCATE         |
      | AQUAMARINES       |
