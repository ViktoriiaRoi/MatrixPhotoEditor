package com.example.matrixphotoeditor.double_exp;

public class AddingEffect implements DoubleExpEffect{
    @Override
    public int combinePixels(int first, int second, int intensity) {
        double i = intensity/100.0;
        
        int result;
        if (i < 0) {
            result = (int) (first + (1+i)*second);
        } else {
            result = (int) ((1-i)*first + second);
        }

        return Math.min(result, 255);
    }
}
