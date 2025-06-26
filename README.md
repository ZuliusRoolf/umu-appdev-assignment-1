# README

This repository contains a simple android game called **Thirty** for a [Mobile Application Development](https://www.umu.se/utbildning/kurser/utveckling-av-mobila-applikationer2?term=ST25#applications) course at Umeå University.

- The specifications of the assignments can be read in [`description.md`](assignment/description.md).
- The android code resides in [`ThirtyThrows`](ThirtyThrows) directory (Open this folder in Android Studio).
- Pseudo code for the game logic is written in [`pseudo.py`](pseudo.py).

## Fragments

- **PlayFragment**
  - 6 WhiteDice icons -> Toggle 'GreyDice icon' when locked
  - Start button -> Reroll button
  - Rolls 'X' TextView
  - Round 'X' TextView
  - Navigate to ScoreFragment when rolls = 0
  - Navigate to GameOverFragment when rolls = 0 and round = 0
- **ScoreFragment**
  - Table of scoring options -> Greyed out when locked
  - Navigate to PlayFragment if not full else GameOverFragment
- **GameOverFragment**
  - Display final score
  - Info button to display ScoreFragment
  - Replay button to reset values and navigate to PlayFragment
