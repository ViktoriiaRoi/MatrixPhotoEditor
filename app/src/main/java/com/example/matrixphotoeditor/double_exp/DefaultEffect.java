package com.example.matrixphotoeditor.double_exp;

public class DefaultEffect implements DoubleExpEffect{

    @Override
    public int combinePixels(int first, int second, int intensity) {
        double i = intensity/200.0 + 0.5;
        return (int) ((1-i)*first + i*second);
    }
}
