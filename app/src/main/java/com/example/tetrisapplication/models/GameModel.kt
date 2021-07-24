package com.example.tetrisapplication.models

import android.graphics.Point
import com.example.tetrisapplication.constants.FieldSq
import com.example.tetrisapplication.helpers.array2dOfByte
import com.example.tetrisapplication.storage.AppPreferences
import com.example.tetrisapplication.constants.CellState


class GameModel {

    var score: Int = 0
    private var preferences: AppPreferences? = null

    var currentBlock: Block? = null
    var currentState: String = Statuses.AWAITING_START.name

    private var field: Array<ByteArray> = array2dOfByte( //поле по умолчанию
        FieldSq.ROW_COUNT.value,
        FieldSq.COLUMN_COUNT.value
    )

    enum class Statuses {
        AWAITING_START, ACTIVE, INACTIVE, OVER
    }

    enum class Motions {
        LEFT, RIGHT, DOWN, ROTATE, FASTDOWN
    }

    fun setPreferences(preferences: AppPreferences?) {
        this.preferences = preferences
    }

    fun getSellStatus(row:Int, column:Int): Byte? {
        return field[row][column]
    }

    private fun setCellStatus(row: Int, column: Int, status: Byte?) {
        if(status != null) {
            field[row][column] = status
        }
    }

    fun isGameOver(): Boolean {
        return currentState == Statuses.OVER.name
    }

    fun isGameActive(): Boolean {
        return currentState == Statuses.ACTIVE.name
    }

    fun isGameAwatingStart(): Boolean {
        return currentState == Statuses.AWAITING_START.name
    }

    private fun boostScore() {
        score += 10
        if(score > preferences?.getHighScore() as Int) {
            preferences?.saveHighScore(score)
        }
    }

    private fun generateNextBlock() {
        currentBlock = Block.createBlock()
    }

    private fun validTranslation(position: Point, shape: Array<ByteArray>): Boolean {
        return if (position.y < 0 || position.x < 0) {
            false
        } else if (position.y + shape.size > FieldSq.ROW_COUNT.value) {
            false
        } else if (position.x + shape[0].size > FieldSq.COLUMN_COUNT.value) {
            false
        } else {
            for (i in 0 until shape.size) {
                for (j in 0 until shape[i].size) {
                    val y = position.y + i
                    val x = position.x + j
                    if (CellState.EMPTY.value != shape[i][j] &&
                        CellState.EMPTY.value != field[y][x]
                    ) {
                        return false
                    }
                }
            }
            true
        }
    }

    private fun moveValid(position: Point, frameNumber: Int?): Boolean {
        val shape: Array<ByteArray>? = currentBlock?.getShape(frameNumber as Int)
        return validTranslation(position, shape as Array<ByteArray>)
    }

    private fun persistCellData() {
        for(i in 0 until field.size) {
            for(j in 0 until field[i].size) {
                var status = getSellStatus(i, j)
                if(status == CellState.TRANS.value) {
                    status = currentBlock?.staticValue
                    setCellStatus(i, j, status)
                }}}
    }

    private fun resetField(ephemeralCellsOnly: Boolean = true) {
        for(i in 0 until  FieldSq.ROW_COUNT.value) {
            (0 until FieldSq.COLUMN_COUNT.value).filter {
                !ephemeralCellsOnly || field[i][it] ==
                        CellState.TRANS.value }
                .forEach {field[i][it] = CellState.EMPTY.value}
        }
    }

    private fun translateBlock(position: Point, frameNumber: Int) {
        synchronized(field) {
            val shape: Array<ByteArray>? = currentBlock?.getShape(frameNumber)

            if (shape != null) {
                for (i in shape.indices) {
                    for (j in 0 until shape[i].size) {
                        val y = position.y + i
                        val x = position.x + j

                        if (CellState.EMPTY.value != shape[i][j]) {
                            field[y][x] = shape[i][j]
                        }
                    }
                }
            }
        }
    }

    fun generateField(action: String) {
        if(isGameActive()) {
            resetField()
            var frameNumber: Int? = currentBlock?.frameNumber
            val coordinate: Point? = Point()
            coordinate?.x = currentBlock?.position?.x
            coordinate?.y = currentBlock?.position?.y

            when (action) {
                Motions.LEFT.name -> {
                    coordinate?.x = currentBlock?.position?.x?.minus(1)
                }

                Motions.RIGHT.name -> {
                    coordinate?.x = currentBlock?.position?.x?.plus(1)
                }

                Motions.FASTDOWN.name -> {
                    coordinate?.y = currentBlock?.position?.y?.plus(2)
                }

                Motions.DOWN.name -> {
                    coordinate?.y = currentBlock?.position?.y?.plus(1)
                }

                Motions.ROTATE.name -> {
                    frameNumber = frameNumber?.plus(1)

                    if(frameNumber != null) {
                        if (frameNumber >= currentBlock?.frameCount as Int) {
                            frameNumber = 0
                        }
                    }
                }
            }
            if(!moveValid(coordinate as Point, frameNumber)){
                translateBlock(currentBlock?.position as Point, currentBlock?.frameNumber as Int)
                if(Motions.DOWN.name == action) {

                    persistCellData()
                    assesField()
                    generateNextBlock()

                    if(!blockAdditionPossible()){
                        currentState = Statuses.OVER.name;
                        currentBlock = null;
                        resetField(false);
                    }
                }
            } else {
                if(frameNumber != null) {
                    translateBlock(coordinate, frameNumber)
                    currentBlock?.setState(frameNumber, coordinate)
                }
            }
        }
    }

    private fun blockAdditionPossible(): Boolean {
        if(!moveValid(currentBlock?.position as Point,
                currentBlock?.frameNumber)) {
            return false
        }
        return true
    }

    private fun shiftRows(nToROw: Int) {
        if(nToROw > 0) {
            for(j in nToROw - 1 downTo 0) {
                for(m in 0 until field[j].size) {
                    setCellStatus(j+1, m, getSellStatus(j, m))
                }
            }
        }
        for (j in 0 until field[0].size) {
            setCellStatus(0, j, CellState.EMPTY.value)
        }
    }

    private fun resetModel(){
        resetField(false)
        currentState = Statuses.AWAITING_START.name
        score = 0
    }



    fun startGame() {
        if(!isGameActive()) {
            currentState = Statuses.ACTIVE.name
            generateNextBlock()
        }
    }

    fun restartGame() {
        resetModel()
        startGame()
    }

    fun endGame() {
        score = 0
        currentState = Statuses.OVER.name
    }

    private fun assesField() {
        for(i in 0 until field.size) {
            var emptyCells = 0;
            for(j in 0 until field[i].size) {
                val status = getSellStatus(i, j)
                val isEmpty = CellState.EMPTY.value == status

                if(isEmpty) {
                    emptyCells++
                }
            }

            if(emptyCells == 0) {
                boostScore()
                shiftRows(i)
            }
        }
    }
}