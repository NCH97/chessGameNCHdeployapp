package com.eg.chessgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import com.eg.android.view.customviews.ChessboardView

class MainActivity : AppCompatActivity(), Presenter.ChessboardInterface {
    private lateinit var chessboard: ChessboardView
    private lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = Presenter(this)
        chessboard = findViewById(R.id.chessboard)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        sendInputToPresenter(chessboard.currentChosenPos, chessboard.previousChosenPos)
        return super.onTouchEvent(event)
    }

    override fun sendInputToPresenter(currentPosition: Pair<Int, Int>?, previousPosition: Pair<Int, Int>?) {
        presenter.handleInput(currentPosition, previousPosition)
    }

    override fun displayAvailableMoves(movesCoordinates: List<Pair<Int, Int>>) {
        chessboard.displaySelection()
        chessboard.displayAvailableMoves(movesCoordinates)
    }

    override fun clearSelection() {
        chessboard.clearSelection()
    }

    override fun redrawPieces(
        whitePieces: MutableMap<Int, Pair<String, Pair<Int, Int>>>,
        blackPieces: MutableMap<Int, Pair<String, Pair<Int, Int>>>) {
        chessboard.redrawPieces(whitePieces, blackPieces)
    }

    override fun displayWinner(player: Int) {
        val winnerColorString = if (player == -1) "White player" else "Black player"
        Toast.makeText(this, "$winnerColorString wins!", Toast.LENGTH_LONG).show()
    }
}