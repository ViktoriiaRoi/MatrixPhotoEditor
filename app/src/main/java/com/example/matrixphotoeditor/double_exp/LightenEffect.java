package com.example.matrixphotoeditor.double_exp;

import static java.lang.Math.max;

public class LightenEffect implements DoubleExpEffect{
    @Override
    public int combinePixels(int first, int second, int intensity) {
        double i = intensity/100.0;

        int result;
        if (i <= 0) {
            result = (int) ((-i)*first + (1+i)*max(first,second));
        } else{
            result = (int) (i*second + (1-i)*max(first,second));
        }

        return result;
    }
}
