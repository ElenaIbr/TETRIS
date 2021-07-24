package com.example.tetrisapplication.models;

import android.graphics.Color;
import android.graphics.Point;

import androidx.annotation.NonNull;

import com.example.tetrisapplication.constants.FieldSq;

import java.util.Random;

public class Block {

    private int shapeIndex;
    private int frameNumber;
    private  BlockColor color;
    private Point position;

    private Block(int shapeIndex, BlockColor blockColor) {
        this.frameNumber = 0;
        this.shapeIndex = shapeIndex;
        this.color = blockColor;
        this.position = new Point(FieldSq.COLUMN_COUNT.getValue()/2, 0);
    }

    //create random shape
    public static Block createBlock() {
        Random random = new Random();
        int shapeNum = random.nextInt(TetShape.values().length);
        BlockColor blockColor = BlockColor.values()
                [random.nextInt(BlockColor.values().length)];
        Block block = new Block(shapeNum, blockColor);
        block.position.x = block.position.x - TetShape.values()
                [shapeNum].getStartPosition();
        return block;
    }

    public enum BlockColor {
        RED(Color.rgb(237, 45, 58), (byte)2),
        GREEN(Color.rgb(56, 194, 86), (byte)3),
        PINK(Color.rgb(242, 48, 210), (byte)4),
        BLUE(Color.rgb(47, 17, 242), (byte)5),
        ORANGE(Color.rgb(242, 152, 17), (byte)6),
        PURPLE(Color.rgb(156, 17, 242), (byte)7);
        BlockColor(int rgbValue, byte value){
            this.rgbValue = rgbValue;
            this.byteValue = value;
        }
        private final int rgbValue;
        private final byte byteValue;
    }

    public static int getColor(byte value){
        for(BlockColor colour : BlockColor.values()) {
            if(value == colour.byteValue) {
                return colour.rgbValue;
            }
        }
        return -1;
    }

    public final void setState(int frame, Point position){
        this.frameNumber = frame;
        this.position = position;
    }

    @NonNull
    public final byte[][] getShape(int frameNumber) {
        return TetShape.values()[shapeIndex].getShape(
                frameNumber).as2dByteArray();
    }

    public Point getPosition() {
        return this.position;
    }

    public final int getFrameCount() {
        return TetShape.values()[shapeIndex].getFrameCount();
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public int getColor() {
        return color.rgbValue;
    }

    public byte getStaticValue() {
        return color.byteValue;
    }
}
