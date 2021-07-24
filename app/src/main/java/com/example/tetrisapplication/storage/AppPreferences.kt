package com.example.tetrisapplication.storage

import android.content.Context
import android.content.SharedPreferences

class AppPreferences (ctx: Context) {
    var data: SharedPreferences = ctx.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE)

    fun saveHighScore(higeScore : Int) {
        data.edit().putInt("HIGH_SCORE", higeScore).apply()
    }

    fun getHighScore(): Int {
        return data.getInt("HIGH_SCORE", 0)
    }

    fun clearHighScore() {
        data.edit().putInt("HIGH_SCORE", 0).apply()
    }
}