package com.example.matrixphotoeditor.double_exp;

import static java.lang.Math.max;

public class SubtractEffect implements DoubleExpEffect{

    @Override
    public int combinePixels(int first, int second, int intensity) {
        double i = intensity/100.0;

        int result;
        if (i < 0) {
            result = (int) (first + (1+i)*second + -i*255);
        } else {
            result = (int) ((1-i)*first + second + i*255);
        }

        return max(result-255, 0);
    }
}
