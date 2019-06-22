# Basketball-Statistics-Tracking
### By: Alex Eidt
A program that can track player statistics for a Basketball Game


[ManagementPanelNoBench]: Screenshots/ManagementPanelNoBench.PNG
[ManagementPanelwBench]: Screenshots/ManagementPanelwBench.PNG
[BoxScoreTable]: Screenshots/BoxScoreTable.PNG
[BoxScoreTableTotal]: Screenshots/BoxScoreTableTotal.PNG
[AddStatWindow]: Screenshots/AddStatPanel.PNG
[BBALLFileType]: Screenshots/BBALLFileType.PNG
[BBALLFileTypeUndo]: Screenshots/BBALLFileTypeUndo.PNG
[UndoButton]: Screenshots/UndoButton.PNG
[OREB]: Screenshots/OffensiveRebounded.PNG
[WhoGotOREB]: Screenshots/WhoGotTheOffensiveRebound.PNG
[Assist]: Screenshots/AssistedFG.PNG
[WhoGotAst]: Screenshots/WhoGotTheAssist.PNG

## Overview:
The Basketball Statistics Tracking Program allows the user to track statistics for all their players
using rules from the Rulebook. 

#### Default Rules and Regulations:
The Player is ejected from the game after:
* 6 Personal Fouls 
* 2 Technical Fouls 
* 2 Flagrant I Fouls
* 1 Flagrant II Foul

The game is set to 4 Quarters each 12 minutes long.


## Home Screen:

When the program is launched from `BasketballMain` the *Home Screen* is created and is
shown below:

<img src = "Screenshots/GetPlayersPanel.PNG" width = "600">

## Buttons:
### Entering Players:

Player's first and last names must be entered in the text field. Pressing the
enter key or the Add Players button will add the player to the player list
at the bottom of the *Home Screen*. To remove the most recently added
player, press the Undo button. The player list is updated every time a player
is added or removed.

<img src = "Screenshots/GetPlayersPanelwPlayers.PNG" width = "600">

### Old Games Button:
<img src = "Image/oldGames.png" width = "50"> 

The Old Games button allows you to load any games you have previously
tracked using this software. In the case of a crash or unwanted closure of 
the program, you can resume tracking your game with no concerns.

<img src = "Screenshots/LoadOldGame.PNG" width = "600">

Clicking on any one of these games will result in a new *Roster Management* window opening
with all your players and their statistics ready to go for you to continue. 
The current period and time left in the period will also be shown
based on when the game was closed as well as the number of timeouts and
team fouls that were left. 

### Instructions Button:
<img src = "Image/instructions.png" width = "50">

The Instructions button opens a new window with the documentation for the
program. There is text that walks the user through all the components
of the program and has interactive panes where the user can become
familiar with the mechanics of the program. 

### Settings Button:
<img src = "Image/settings.png" width = "50">

If the user wishes to change the regulations/rules their game is played
by, they can do so in the settings window. 
The background color used throughout the program can also be changed
by clicking one of the colored buttons on the panel on the right.
The settings window is shown below:

<img src = "Screenshots/SettingsWindow.PNG" width = "600">

## Starter Selection:

If the number of players entered in the *Home Screen* is greater the number
of starters selected in the settings window, then you will need to choose
who will start in your game. The following window opens after the Start Game
button is pressed in the *Home Screen*: 

<img src = "Screenshots/StartersPanel.PNG" width = "600">

Clicking on the player names will add them to a list of players currently
selected to start the game. You will only be able to start the game
once you have selected the appropriate number of starters.

<img src = "Screenshots/StartersPanelwPlayers.PNG" width = "600">

### Roster Management

There are three different ways of getting to this next window.

If the number of players entered equals the number of starters specified (either by default or in
the settings) then you will be directed to this window after clicking the Start Game
button in the *Home Screen* or if the game you are loading with the *Old Games*
button had as many players as starters specified. 

<img src = "Screenshots/ManagementPanelNoBench.PNG" width = "600">

If the number of players entered was greate than the number of starters specified, then you were redirected to the *Starter Selection* window where you chose your starters. Once you pressed the *Start Game* button there, you will be redirected to an alternate version of the *Roster Management* window with starters and a bench.

<img src = "Screenshots/ManagementPanelwBench.PNG" width = "600">

## Buttons and Labels:
### Score:
<img src = "Image/score.png" width = "50">

Shows the total points scored by the team.

### Box Score:
<img src = "Image/boxScore.png" width = "50">

Opens a new window showing all player statistics in the traditional Box Score
format used for Basketball Games.

<img src = "Screenshots/BoxScoreTable.PNG" width = "600">

The totals for each statistic are shown at the bottom of the table:
<img src = "Screenshots/BoxScoreTableTotal.PNG" width = "600">

### Undo:
<img src = "Image/undo.png" width = "50">

Opens a new window allowing the user to correct any statistics they entered by
deleting them. All periods of the game are shown and clicking on a period
shows the user all statistics that were entered during that period.
Clicking on any statistic closes the Undo window and removes that statistic.

<img src = "Screenshots/UndoButton.PNG" width = "600">

### Done:
<img src = "Image/done.png" width = "50">

Stops the game and closes the *Roster Management* window.

### Start/Stop:

Starts/Stops the timer for the game.

### Timeouts:
<img src = "Image/timeout.png" width = "50">

Takes away one timeout (Timeouts remaining shown on button) and stops the 
timer.

### Team Fouls:
<img src = "Image/teamFouls.png" width = "50">

Shows the current number of team fouls in the period.
Resets after each period.




