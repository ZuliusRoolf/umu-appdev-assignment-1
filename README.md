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
  - ~~Info button to display ScoreFragment~~
  - Replay button to reset values and navigate to PlayFragment

## Feedback

Overall, you did a great job. Well done!
Feedback marked with * needs to be fixed, and + is general tips.

The interface design is nice, and there are no issues with elements, e.g., overlapping between components. Perfect!

- [x] *Score calculation is not working correctly. E.g., for a target of 7 and dice [2,2,2,2,3,3], your getScore returns 7, while the optimal grouping yields 14 (two groups of 2+2+3). It looks like your code greedily picks the first valid combo, marks those dice as used, and never backtracks to find the second group. To fix this, consider adding a full backtracking step— or allow manual grouping—so you capture all disjoint combinations. (The score calculation does not have to be automatic, and manual grouping of dice is also fine.)
  > **Response:** I see the flaw with no backtracking, I first thought sorting the list would resolve any issues with finding combinations, but the algorithm doesn't consider when there is a lot of the same values, such as [2,2,2,2,3,3] you mentioned. **I resolved it by changing the success behaviour, instead of using the next element when a combination is discovered, simply reset the function to the first element and run from the beginning. This should behave like backtracking and resolve the issue**
- [x] *Lacks in state saving. Your app does not properly preserve its state—for example, when minimizing the app using the home button (the circle) or switching to another app using the overview button and then returning, the game state resets instead of being restored. Test with the developer setting "Don't keep activities" activated and navigate out of the app using the home button (not via the back button) and go back into the app again. The app should work the same afterward and preserve the user's dice selections and game state played so far.
A tip is to look into the SavedStateHandle for your ViewModel and/or implement Parcelable interface.
Remember the Back navigation needs to work as intended, especially when state saving is fixed. In any stage of the game, it is OK if the back navigation restarts the game or exits the app (but does not result in an invalid state or allow editing the previous round).
  > **Response:** The main variables needed to recreate the game state is now saved in a `SavedStateHandle`. It was quite difficult to find information on how to make a custom data class into something the SavedStateHandle can handle. Finally found Parcelize plugin which enables such behaviour and I don't need to rewrite my scoreboard logic.
- [x] *You need to have a score table in the ScoreFragment that shows each score option (scoring target) as the label and the gathered scores for it, e.g., if the user got 24 scores for score option 6, you can have "choice six: 24".
It is nice that you separate the game and the model logic from the main activity.
  > **Response:** After some emails of clarifications, I interpret the problem to be the lack of a scoreboard view in the result screen. The prettiest way to do it would be to have a summary, but I feel a little lazy and will just add a button to display the scoreFragment in the result screen instead. I hope this is enough :)
- [ ] *Avoid having long methods. Try to refactor onViewCreated and break it into smaller methods.
  > **Response:**
- [ ] +Make sure you have well-commented code. Consider having documentation comments before the method signature, and overall, add more comments to your code (All the methods you make should have documentation comments)
`/* ... */` is a block comment that is used anywhere in the code.
`/** ... */` is a documentation comment used before method/class signatures for documentation. Supports annotations like @param, @return, and @throws.
  > **Response:**
- [ ] +If you include annotations such as @paramyou can specify what different parameters mean clearly and in a structured way. Or @return to document what a function returns.
  > **Response:**
