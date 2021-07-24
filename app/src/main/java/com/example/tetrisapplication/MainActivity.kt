package com.example.tetrisapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.tetrisapplication.storage.AppPreferences
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MainActivity : AppCompatActivity() {

    var btnReset : Button? = null
    var btnNewGame : Button? = null
    var appPreferences: AppPreferences? = null

    var timer: Timer? = null
    var cub1 : ImageView? = null
    var cub2 : ImageView? = null
    var cub3 : ImageView? = null
    var cub4 : ImageView? = null
    var imageArray: IntArray = intArrayOf(R.drawable.red_cub2, R.drawable.yl_cub2, R.drawable.green_cub2, R.drawable.orange_cub, R.drawable.red_cub2)
    var counter: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        btnNewGame = findViewById(R.id.btn_new_game)
        btnReset = findViewById(R.id.btn_reset_score)

        cub1 = findViewById(R.id.cub_1_iv)
        cub2 = findViewById(R.id.cub_2_iv)
        cub3 = findViewById(R.id.cub_3_iv)
        cub4 = findViewById(R.id.cub_4_iv)

        showHighScore()
        startTimer()
    }

    override fun onResume() {
        super.onResume()
        showHighScore()
        startTimer()
    }
    fun onBtnNewGameClick(view: View) {

        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }
    @SuppressLint("SetTextI18n")
    fun inBtnResetScore(view: View) {
        appPreferences = AppPreferences(this)
        appPreferences!!.clearHighScore()
        showHighScore()
    }

    @SuppressLint("SetTextI18n")
    fun showHighScore(){
        appPreferences = AppPreferences(this)
        btnReset?.text = resources.getString(R.string.reset_sc)+": ${appPreferences!!.getHighScore()}"
    }

    fun startTimer(){
        timer = Timer()
        timer?.schedule(object:TimerTask(){
            override fun run() {
                cub1?.setImageResource(imageArray[counter+1])
                cub2?.setImageResource(imageArray[counter])
                cub3?.setImageResource(imageArray[counter+1])
                cub4?.setImageResource(imageArray[counter])
                counter++
                if(counter==4) counter=0
            }
        },0, 500)
    }
}