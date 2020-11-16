package com.eg.chessgame

class Game {
    /*
    * Class that implements the game itself, stores it's instances of game objects,
    * keeps track of current player and winning state
    */

    val gameUtils = GameUtils()
    private val chessObjects = gameUtils.initGame()

    val playerBlack = chessObjects.first
    val playerWhite = chessObjects.second
    val board = chessObjects.third

    val players: Map<Int, Player> = mapOf(-1 to playerWhite, 1 to playerBlack)

    var isEnd = 0
    var currentPlayer = -1  // first turn is white's turn

    init {
        gameUtils.updateAllAvailableMoves(players, board)
    }

    fun makeMove(piecePos: Pair<Int, Int>, movePos: Pair<Int, Int>) {
        gameUtils.makeMove(players, currentPlayer, board, piecePos, movePos)
        gameUtils.updateAllAvailableMoves(players, board)
        // Change current player to opponent
        currentPlayer *= -1
        // Update winning state
        isEnd = gameUtils.checkEnd(players)
    }

   /* while (isEnd == 0) {
        gameHost.makeMove(players, currentPlayer, board)
        gameHost.updateAllAvailableMoves(players, board)
        isEnd = gameHost.checkEnd(players)
        currentPlayer *= -1
    }*/
}
