package com.example.tetrisapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.tetrisapplication.models.GameModel
import com.example.tetrisapplication.storage.AppPreferences
import com.example.tetrisapplication.view.TetrisView

class GameActivity : AppCompatActivity() {

    var tvHighScore: TextView? = null
    var tvCurrentScore: TextView? = null
    var restartGame: Button? = null

    private lateinit var tetrisView: TetrisView
    var appPreferences: AppPreferences? = null
    private val appModel: GameModel = GameModel()

    var toLeft: ImageView? = null
    var toRotate: ImageView? = null
    var toDown: ImageView? = null
    var toRight: ImageView? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        supportActionBar?.hide()

        appPreferences = AppPreferences(this)
        appModel.setPreferences(appPreferences)


        tvHighScore = findViewById(R.id.tv_high_score)
        tvCurrentScore = findViewById(R.id.tv_current_score)
        tetrisView = findViewById(R.id.view_tetris)
        restartGame = findViewById(R.id.restartBtn)

        tetrisView.setActivity(this)

        tetrisView.setModel(appModel)
        tetrisView.setOnTouchListener(this::startPlay)


        tetrisView.setOnClickListener {
            appModel.restartGame()
        }

        upDateHighScore()
        upDateCurrentScore()


        toLeft = findViewById<ImageView>(R.id.iv_left)
        toLeft?.setOnClickListener {
            moveTetromino(GameModel.Motions.LEFT)
        }

        toRotate = findViewById<ImageView>(R.id.rotate)
        toRotate?.setOnClickListener{
            moveTetromino(GameModel.Motions.ROTATE)
        }

        toDown = findViewById<ImageView>(R.id.iv_down)
        toDown?.setOnClickListener{
            moveTetromino(GameModel.Motions.FASTDOWN)
        }

        toRight = findViewById<ImageView>(R.id.iv_right)
        toRight?.setOnClickListener{
            moveTetromino(GameModel.Motions.RIGHT)
        }

    }

    fun restartGame(view: View) {
        appModel.restartGame()
    }

    private fun moveTetromino(motion: GameModel.Motions){
        if(appModel.isGameActive()){
            tetrisView.setGameCommand(motion)
        }
    }

    private fun startPlay(view: View, event: MotionEvent): Boolean{
        if(appModel.isGameOver() || appModel.isGameAwatingStart()){
            appModel.startGame()
            tetrisView.setGameCommandWithDelay(GameModel.Motions.DOWN)
        }
        return true
    }

    private fun upDateCurrentScore() {
        tvCurrentScore?.text = "0"
    }

    private fun upDateHighScore() {
        tvHighScore?.text = "${appPreferences?.getHighScore()}"
    }
}

