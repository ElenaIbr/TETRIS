package com.example.tetrisapplication.models

import java.lang.IllegalArgumentException

enum class TetShape(val frameCount: Int, val startPosition: Int) {

    Square(1, 1) {
        override fun getShape (frameNumber: Int): Frame {
            return Frame(2)
                .addRow("11")
                .addRow("11")
        }
    },

    Zshape(2, 1) {
        override fun getShape (frameNumber: Int): Frame {
            return when(frameNumber) {
                0 -> Frame(3)
                    .addRow("110")
                    .addRow("011")
                1 -> Frame(2)
                    .addRow("01")
                    .addRow("11")
                    .addRow("10")
                else -> throw IllegalArgumentException("$frameNumber is invalid")
            }
        }
    },

    ZshapeTurn(2, 1) {
        override fun getShape (frameNumber: Int): Frame {
            return when(frameNumber) {
                0 -> Frame(3)
                    .addRow("011")
                    .addRow("110")
                1 -> Frame(2)
                    .addRow("10")
                    .addRow("11")
                    .addRow("01")
                else -> throw IllegalArgumentException("$frameNumber is invalid")
            }
        }
    },

    Strip(2, 2) {
        override fun getShape (frameNumber: Int): Frame {
            return when(frameNumber) {
                0 -> Frame(4).addRow("1111")
                1 -> Frame(1)
                    .addRow("1")
                    .addRow("1")
                    .addRow("1")
                    .addRow("1")
                else -> throw IllegalArgumentException("$frameNumber is invalid")
            }
        }
    },

    Tshape(4, 1) {
        override fun getShape (frameNumber: Int): Frame {
            return when(frameNumber) {
                0 -> Frame(3)
                    .addRow("010")
                    .addRow("111")
                1 -> Frame(2)
                    .addRow("10")
                    .addRow("11")
                    .addRow("10")
                2 -> Frame(3)
                    .addRow("111")
                    .addRow("010")
                3 -> Frame(2)
                    .addRow("01")
                    .addRow("11")
                    .addRow("01")
                else -> throw IllegalArgumentException("$frameNumber is invalid")
            }
        }
    },

    Lshape(4, 1) {
        override fun getShape (frameNumber: Int): Frame {
            return when(frameNumber) {
                0 -> Frame(3)
                    .addRow("100")
                    .addRow("111")
                1 -> Frame(2)
                    .addRow("11")
                    .addRow("10")
                    .addRow("10")
                2 -> Frame(3)
                    .addRow("111")
                    .addRow("001")
                3 -> Frame(2)
                    .addRow("01")
                    .addRow("01")
                    .addRow("11")
                else -> throw IllegalArgumentException("$frameNumber is invalid")
            }
        }
    },

    LshapeTurn(4, 1) {
        override fun getShape (frameNumber: Int): Frame {
            return when(frameNumber) {
                0 -> Frame(3)
                    .addRow("001")
                    .addRow("111")
                1 -> Frame(2)
                    .addRow("10")
                    .addRow("10")
                    .addRow("11")
                2 -> Frame(3)
                    .addRow("111")
                    .addRow("100")
                3 -> Frame(2)
                    .addRow("11")
                    .addRow("01")
                    .addRow("01")
                else -> throw IllegalArgumentException("$frameNumber is invalid")
            }
        }
    };

    abstract fun getShape(frameNumber: Int): Frame
}