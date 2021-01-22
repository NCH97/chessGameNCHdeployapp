# My Implementation of chess on Android
This is a pet project I'm making. The only purpose of it is to learn android developement.
Made using only Android studio (i.e. it's not an unity project), written in Kotlin.
WIP.

<img src="/readme_imgs/Screenshot_1611343276.png" width="344" height="726"/>

# Description
Current state of the game is playable though. You can try it yourself by downloading APK provided in Realise.

Implemented basic chess mechanics. Last move could be cancelled, game could be restarted. UI is also very simple without fancy animations or cool-looking design.

Hopefully will be implemented(I'm really not sure about these):
* fancy UI
* multiplayer

Known issues:
* Checkmate is registered even if you can capture opponent's piece attacking your King, i.e. you can avoid checkmate only by moving your King and if have no ability to move it- you lose
* State of the game isn't being saved to restore in case of destroying activity
* No handling for horizontal orientation of a device
