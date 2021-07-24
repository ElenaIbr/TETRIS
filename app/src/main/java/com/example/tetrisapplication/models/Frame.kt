package com.example.tetrisapplication.models

import com.example.tetrisapplication.helpers.array2dOfByte

class Frame(private val width: Int) {

    //width is column count
    val array: ArrayList<ByteArray> = ArrayList()

    fun addRow(byteString: String):Frame {

        //byteStr is each row length
        val row = ByteArray(byteString.length)

        for(ind in byteString.indices) {
            row[ind] = "${byteString[ind]}".toByte()
        }

        array.add(row)
        return this
    }

    fun as2dByteArray(): Array<ByteArray> {
        val bytes = array2dOfByte(array.size, width)
        return array.toArray(bytes)
    }
}