package com.eg.chessgame

import com.eg.android.view.customviews.ChessboardView
import kotlin.math.sign

class Presenter(private val view: ChessboardInterface) {
    
    private val game = Game()

    private var lastAvailableMoves: List<Pair<Int, Int>> = listOf()

    fun handleInput(currentPosition: Pair<Int, Int>?, previousPosition: Pair<Int, Int>?) {

        val pieceNum = game.board[currentPosition!!.first][currentPosition.second]
        val currentPlayerNum = game.currentPlayer

         /* Handle the logic:
            -if chosen piece of current player's side -> tell view to select it and
             display available moves

            -if chosen pos is one of the available moves for previous pos -> make move for
             piece on previous pos

            -else -> clear all selections and list of available positions */
        when {
            (pieceNum.sign == currentPlayerNum) -> selectPieceToMove(pieceNum, currentPlayerNum)
            (lastAvailableMoves.contains(currentPosition)) -> movePiece(previousPosition!!, currentPosition)
            else -> view.clearSelection()
        }
    }

    private fun selectPieceToMove(pieceNum: Int, currentPlayerNum: Int) {
        lastAvailableMoves = game.gameUtils.getAvailableMovesForPiece(pieceNum, game.players[currentPlayerNum])
        view.displayAvailableMoves(lastAvailableMoves)
    }

    private fun movePiece(piecePos: Pair<Int, Int>, movePos: Pair<Int, Int>) {
        // Tell game to make move for current player
        game.makeMove(piecePos, movePos)
        // Clear available moves
        lastAvailableMoves = listOf()
        // Tell View to clear selection and available moves on board
        view.clearSelection()
        // Tell View to redraw pieces on board
        view.redrawPieces(game.playerWhite.pieces, game.playerBlack.pieces)

        // Display winner player if anyone wins
        when (game.isEnd) {
            -1 -> view.displayWinner(-1)
            1 -> view.displayWinner(1)
        }
    }

    // Interface for interaction with View(Activity)
    interface ChessboardInterface {
        fun displayAvailableMoves(movesCoordinates: List<Pair<Int, Int>>)
        fun sendInputToPresenter(currentPosition: Pair<Int, Int>?, previousPosition: Pair<Int, Int>?)
        fun clearSelection()
        fun redrawPieces(whitePieces: MutableMap<Int, Pair<String, Pair<Int, Int>>>,
                         blackPieces: MutableMap<Int, Pair<String, Pair<Int, Int>>>)
        fun displayWinner(player: Int)
    }
}