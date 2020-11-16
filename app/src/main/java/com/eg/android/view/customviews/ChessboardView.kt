package com.eg.android.view.customviews

import android.content.Context
import android.content.ContextWrapper
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.eg.chessgame.R

class ChessboardView(context: Context, attrs: AttributeSet): View(context, attrs) {

    // Color vars
    private var brightColor: Int
    private var darkColor: Int

    // Delta between each square
    private var delta: Int = 0

    // Vars to determine bounds of squares needed to draw
    var selectedSquareBounds: Rect = Rect(0, 0, 0, 0)
    var availableMovesBounds: MutableList<Rect> = mutableListOf()

    // Maps to init positions of pieces and keep track of them
    var whitePlayerPieces: MutableMap<Int, Pair<String, Pair<Int, Int>>> = mutableMapOf(
        -1 to Pair("King", Pair(7, 3)),
        -2 to Pair("Queen", Pair(7, 4)),
        -3 to Pair("Rook", Pair(7, 0)),
        -4 to Pair("Rook", Pair(7, 7)),
        -5 to Pair("Knight", Pair(7, 1)),
        -6 to Pair("Knight", Pair(7, 6)),
        -7 to Pair("Bishop", Pair(7, 2)),
        -8 to Pair("Bishop", Pair(7, 5)),
        -9 to Pair("Pawn", Pair(6, 0)),
        -10 to Pair("Pawn", Pair(6, 1)),
        -11 to Pair("Pawn", Pair(6, 2)),
        -12 to Pair("Pawn", Pair(6, 3)),
        -13 to Pair("Pawn", Pair(6, 4)),
        -14 to Pair("Pawn", Pair(6, 5)),
        -15 to Pair("Pawn", Pair(6, 6)),
        -16 to Pair("Pawn", Pair(6, 7)),
    )
    var blackPlayerPieces: MutableMap<Int, Pair<String, Pair<Int, Int>>> = mutableMapOf(
        1 to Pair("King", Pair(0, 3)),
        2 to Pair("Queen", Pair(0, 4)),
        3 to Pair("Rook", Pair(0, 0)),
        4 to Pair("Rook", Pair(0, 7)),
        5 to Pair("Knight", Pair(0, 1)),
        6 to Pair("Knight", Pair(0, 6)),
        7 to Pair("Bishop", Pair(0, 2)),
        8 to Pair("Bishop", Pair(0, 5)),
        9 to Pair("Pawn", Pair(1, 0)),
        10 to Pair("Pawn", Pair(1, 1)),
        11 to Pair("Pawn", Pair(1, 2)),
        12 to Pair("Pawn", Pair(1, 3)),
        13 to Pair("Pawn", Pair(1, 4)),
        14 to Pair("Pawn", Pair(1, 5)),
        15 to Pair("Pawn", Pair(1, 6)),
        16 to Pair("Pawn", Pair(1, 7)),
    )

    // variables to handle selecting and moving pieces
    var currentChosenPos: Pair<Int, Int>? = null
    var previousChosenPos: Pair<Int, Int>? = null

    // Drawable resources for pieces
    private val drawableWhitePieces: MutableMap<Int, Drawable?> = mutableMapOf(
        -1 to AppCompatResources.getDrawable(context, R.drawable.circle_white),
        -2 to AppCompatResources.getDrawable(context, R.drawable.circle_white),
        -3 to AppCompatResources.getDrawable(context, R.drawable.circle_white),
        -4 to AppCompatResources.getDrawable(context, R.drawable.circle_white),
        -5 to AppCompatResources.getDrawable(context, R.drawable.circle_white),
        -6 to AppCompatResources.getDrawable(context, R.drawable.circle_white),
        -7 to AppCompatResources.getDrawable(context, R.drawable.circle_white),
        -8 to AppCompatResources.getDrawable(context, R.drawable.circle_white),
        -9 to AppCompatResources.getDrawable(context, R.drawable.circle_white),
        -10 to AppCompatResources.getDrawable(context, R.drawable.circle_white),
        -11 to AppCompatResources.getDrawable(context, R.drawable.circle_white),
        -12 to AppCompatResources.getDrawable(context, R.drawable.circle_white),
        -13 to AppCompatResources.getDrawable(context, R.drawable.circle_white),
        -14 to AppCompatResources.getDrawable(context, R.drawable.circle_white),
        -15 to AppCompatResources.getDrawable(context, R.drawable.circle_white),
        -16 to AppCompatResources.getDrawable(context, R.drawable.circle_white))
    private val drawableBlackPieces: MutableMap<Int, Drawable?> = mutableMapOf(
        1 to AppCompatResources.getDrawable(context, R.drawable.circle),
        2 to AppCompatResources.getDrawable(context, R.drawable.circle),
        3 to AppCompatResources.getDrawable(context, R.drawable.circle),
        4 to AppCompatResources.getDrawable(context, R.drawable.circle),
        5 to AppCompatResources.getDrawable(context, R.drawable.circle),
        6 to AppCompatResources.getDrawable(context, R.drawable.circle),
        7 to AppCompatResources.getDrawable(context, R.drawable.circle),
        8 to AppCompatResources.getDrawable(context, R.drawable.circle),
        9 to AppCompatResources.getDrawable(context, R.drawable.circle),
        10 to AppCompatResources.getDrawable(context, R.drawable.circle),
        11 to AppCompatResources.getDrawable(context, R.drawable.circle),
        12 to AppCompatResources.getDrawable(context, R.drawable.circle),
        13 to AppCompatResources.getDrawable(context, R.drawable.circle),
        14 to AppCompatResources.getDrawable(context, R.drawable.circle),
        15 to AppCompatResources.getDrawable(context, R.drawable.circle),
        16 to AppCompatResources.getDrawable(context, R.drawable.circle))

    init {
        // Get access to attributes defined in xml
        context.theme.obtainStyledAttributes(attrs, R.styleable.ChessboardView, 0, 0).apply {
            try {
                brightColor = getColor(R.styleable.ChessboardView_brightColor, Color.WHITE)
                darkColor = getColor(R.styleable.ChessboardView_darkColor, Color.LTGRAY)
            }
            finally {
                recycle()
            }
        }
    }

    // Define Paints for elements to draw
    private val darkPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = darkColor
        style = Paint.Style.FILL
    }

    private val brightPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = brightColor
    }

    private val selectedPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.GREEN
        style = Paint.Style.FILL
    }

    private val availableMovePaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.RED
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        delta = measuredWidth/8
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    private fun getHostActivity(): AppCompatActivity? {
        // Helper method to gain access to parent activity
        var hostContext = context

        while (hostContext is ContextWrapper) {
            if (hostContext is AppCompatActivity) {
                return hostContext
            }
            hostContext = (context as ContextWrapper).baseContext
        }
        return null
    }

    // Helper functions to draw graphics
    private fun drawBoard(canvas: Canvas) {
        // Draw squares of chessboard
        for (i in (1..8)) {
            for (j in (1..8)) {
                if ((i+j).rem(2) == 0) canvas.drawRect(((i-1)*delta).toFloat(),
                    ((j-1)*delta).toFloat(), (i*delta).toFloat(), (j*delta).toFloat(), brightPaint)
                else canvas.drawRect(((i-1)*delta).toFloat(),
                    ((j-1)*delta).toFloat(), (i*delta).toFloat(), (j*delta).toFloat(), darkPaint)
            }
        }
    }

    private fun drawSelection(canvas: Canvas) {
        // Draw special tile for selected piece
        canvas.drawRect(selectedSquareBounds, selectedPaint)

        // Draw special tiles for available positions to move
        for (bounds in availableMovesBounds) {
            canvas.drawRect(bounds, availableMovePaint)
        }
    }

    private fun drawPieces(canvas: Canvas) {
        // Draw white pieces
        for ((pieceNum, piece) in whitePlayerPieces) {
            val piecePos = piece.second
            drawableWhitePieces[pieceNum]?.apply {
                bounds = transformToRect(piecePos.second, piecePos.first)
                draw(canvas)
            }
        }

        // Draw black pieces
        for ((pieceNum, piece) in blackPlayerPieces) {
            val piecePos = piece.second
            drawableBlackPieces[pieceNum]?.apply {
                bounds = transformToRect(piecePos.second, piecePos.first)
                draw(canvas)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawBoard(canvas)
        drawSelection(canvas)
        drawPieces(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                getPositionOfSquare(event.x, event.y)
                val hostActivity = getHostActivity()
                hostActivity?.onTouchEvent(event)
            }
        }
        return true
    }

    // Function to extract position of tile in my board coordinates from coordinates from touch
    private fun getPositionOfSquare(x: Float, y: Float) {
        // get number of row and column for selected square
        val rowPositionOfSquare = (y/delta).toInt()
        val colPositionOfSquare = (x/delta).toInt()

        // Store coordinates of last selected position
        previousChosenPos = currentChosenPos
        // Store coordinates of selected position on chessboard
        currentChosenPos = Pair(rowPositionOfSquare, colPositionOfSquare)
        println("Current pos: " + currentChosenPos.toString())
        println("Previous pos: " + previousChosenPos.toString())
    }

    private fun transformToRect(xPositionOfSquare: Int, yPositionOfSquare: Int): Rect {
        val yBottom = (yPositionOfSquare+1)*delta
        val xLeft = (xPositionOfSquare)*delta
        val xRight = (xPositionOfSquare+1)*delta
        val yTop = (yPositionOfSquare)*delta

        return Rect(xLeft,yTop, xRight, yBottom)
    }

    // Make square with given coordinates selected
    fun displaySelection() {
        selectedSquareBounds = transformToRect(currentChosenPos!!.second, currentChosenPos!!.first)
        this.invalidate()
    }

    fun displayAvailableMoves(movesCoordinates: List<Pair<Int, Int>>) {
        availableMovesBounds = mutableListOf()
        for (movePosition in movesCoordinates) {
            val xPositionOfSquare = movePosition.second
            val yPositionOfSquare = movePosition.first

            availableMovesBounds.add(transformToRect(xPositionOfSquare, yPositionOfSquare))
            this.invalidate()
        }
    }

    fun clearSelection() {
        availableMovesBounds = mutableListOf()
        selectedSquareBounds = Rect(0, 0, 0, 0)
        invalidate()
    }

    fun redrawPieces(whitePieces: MutableMap<Int, Pair<String, Pair<Int, Int>>>,
                     blackPieces: MutableMap<Int, Pair<String, Pair<Int, Int>>>) {

        whitePlayerPieces = whitePieces
        blackPlayerPieces = blackPieces
        invalidate()
    }
}