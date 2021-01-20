# My Implementation of chess on Android
This is a pet project I'm making. The only purpose of it is to learn android developement.
Made using only Android studio (i.e. it's not an unity project), written in Kotlin.
WIP.
# Description
Current state of the game is playable though.

Implemented mechanics:
* you can move pieces 
* you can capture opponent's pieces
* you can do a check(chekmate)
* swaps on each turn
* move can be cancelled(only last one)
* you can restart the game

Hopefully will be implemented(I'm really not sure about these):
* some fancy graphics
* multiplayer

Known issues:
* Checkmate is registered even if you can capture opponent's piece attacking your King, i.e. you can avoid checkmate only by moving your King and if have no ability to move it- you lose
* State of the game isn't being saved to restore in case of destroying activity
* No handling for horizontal orientation of a device
