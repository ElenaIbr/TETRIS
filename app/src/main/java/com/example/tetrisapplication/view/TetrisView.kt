package com.example.tetrisapplication.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.example.tetrisapplication.GameActivity
import android.os.Handler
import com.example.tetrisapplication.constants.CellState
import com.example.tetrisapplication.constants.FieldSq
import com.example.tetrisapplication.models.Block
import com.example.tetrisapplication.models.GameModel

class TetrisView : View {
    private val paint = Paint()
    private var lastMove: Long = 0
    private var model: GameModel? = null
    private var activity: GameActivity? = null
    private val viewHolder = ViewHandler(this)
    private var cellSize: Dimension = Dimension(0,0)
    private var frameOffSet: Dimension = Dimension(0,0)

    constructor(cont: Context, attrs: AttributeSet):super(cont, attrs)

    constructor(cont: Context, attrs: AttributeSet, defStyle: Int):super(cont, attrs, defStyle)

    companion object {
        private val DELAY = 500
        private val BLOCK_OFFSET = 2
        private val FRAME_OFFSET_BASE = 10
    }

    fun setModel(model: GameModel){
        this.model = model
    }

    fun setActivity(gameActivity: GameActivity) {
        this.activity = gameActivity
    }

    private fun updateScores() {
        activity?.tvCurrentScore?.text = "${model?.score}"
        activity?.tvHighScore?.text = "${activity?.appPreferences?.getHighScore()}"
    }

    fun setGameCommandWithDelay(move: GameModel.Motions) {
        val now = System.currentTimeMillis()

        if(now - lastMove > DELAY) {
            model?.generateField(move.name)
            invalidate()
            lastMove = now
        }
        updateScores()
        viewHolder.sleep(DELAY.toLong())
    }

    private class ViewHandler(private val owner: TetrisView) : Handler() {

        override fun handleMessage(message: Message) {
            if(message.what == 0){
                if(owner.model != null) {
                    if(owner.model!!.isGameOver())  {
                        owner.model?.endGame()
                        Toast.makeText(owner.activity, "Game over!", Toast.LENGTH_SHORT).show();
                    }
                    if(owner.model!!.isGameActive()) {
                        owner.setGameCommand(GameModel.Motions.DOWN)
                    }
                }
            }
        }

        fun sleep(delay: Long) {
            removeMessages(0)
            sendMessageDelayed(obtainMessage(0), delay)
        }
    }

    private data class Dimension(val width: Int, val height: Int)



    fun setGameCommand(move: GameModel.Motions) {
        if(null != model && (model?.currentState == GameModel.Statuses.ACTIVE.name)) {
            if(GameModel.Motions.DOWN != move) {
                model?.generateField(move.name)
                invalidate()
                return
            }
            setGameCommandWithDelay(move)
        }
    }

    private fun drawCell(canvas: Canvas, x: Int, y: Int, rgbColor: Int) {
        paint.color = rgbColor
        val top: Float = (frameOffSet.height + y*cellSize.height + BLOCK_OFFSET).toFloat()
        val left: Float = (frameOffSet.width + x*cellSize.width + BLOCK_OFFSET).toFloat()
        val bottom: Float = (frameOffSet.height + (y+1)*cellSize.height - BLOCK_OFFSET).toFloat()
        val right: Float = (frameOffSet.width + (x+1)*cellSize.width - BLOCK_OFFSET).toFloat()

        val rectangle = RectF(left, top, right, bottom)
        canvas.drawRoundRect(rectangle, 4F, 4F, paint)
    }

    override fun onSizeChanged(width: Int, height: Int, previosWidth: Int, previosHeight: Int){
        super.onSizeChanged(width, height, previosWidth,  previosHeight)
        val cellWidth = (width - 2 * FRAME_OFFSET_BASE)/FieldSq.COLUMN_COUNT.value
        val cellHeight = (height - 2* FRAME_OFFSET_BASE)/FieldSq.ROW_COUNT.value

        val n = Math.min(cellWidth, cellHeight)
        this.cellSize = Dimension(n,n)

        val offsetX = (width - FieldSq.COLUMN_COUNT.value*n)/2
        val offsetY = (height - FieldSq.ROW_COUNT.value*n)/2
        this.frameOffSet = TetrisView.Dimension(offsetX, offsetY)
    }

    private fun drawCell(canvas: Canvas, row: Int, col: Int) {
        val cellStatus = model?.getSellStatus(row, col)
        if(CellState.EMPTY.value != cellStatus){
            val color = if (CellState.TRANS.value == cellStatus) {
                model?.currentBlock?.color
            } else {
                Block.getColor(cellStatus as Byte)
            }
            drawCell(canvas, col, row, color as Int)
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawFrame(canvas)
        if(model != null) {
            for(i in 0 until FieldSq.ROW_COUNT.value) {
                for(j in 0 until FieldSq.COLUMN_COUNT.value){
                    drawCell(canvas, i, j)
                }
            }
        }
    }

    private fun drawFrame(canvas: Canvas){
        paint.color = Color.rgb(180, 233, 240)
        canvas.drawRect(frameOffSet.width.toFloat(),
        frameOffSet.height.toFloat(),
        width-frameOffSet.width.toFloat(),
            height-frameOffSet.height.toFloat(), paint)
    }

}