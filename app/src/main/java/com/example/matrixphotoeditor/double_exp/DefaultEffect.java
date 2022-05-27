package com.example.matrixphotoeditor.double_exp;

public class DefaultEffect implements DoubleExpEffect{

    @Override
    public int combinePixels(int first, int second, int intensity) {
        double i = intensity/200.0;
        return (int) ((0.5-i)*first + (0.5+i)*second);
    }
}
