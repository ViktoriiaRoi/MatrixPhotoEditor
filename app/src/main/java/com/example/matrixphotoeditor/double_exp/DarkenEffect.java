package com.example.matrixphotoeditor.double_exp;

import static java.lang.Math.min;

public class DarkenEffect implements DoubleExpEffect{
    @Override
    public int combinePixels(int first, int second, int intensity) {
        double i = intensity/100.0;

        int result;
        if (i <= 0) {
            result = (int) ((-i)*first + (1+i)*min(first,second));
        } else{
            result = (int) (i*second + (1-i)*min(first,second));
        }

        return result;
    }
}
