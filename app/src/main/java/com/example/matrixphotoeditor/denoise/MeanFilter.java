package com.example.matrixphotoeditor.denoise;

import android.graphics.Color;

import java.util.ArrayList;

public class MeanFilter implements DenoiseEffect {

    @Override
    public int applyEffect(int n, ArrayList<Integer> colorArray) {
        int red=0, green=0, blue=0;
        for (Integer color : colorArray) {
            red += Color.red(color);
            green += Color.green(color);
            blue += Color.blue(color);
        }
        red /= n * n;
        green /= n * n;
        blue /= n * n;
        return Color.rgb(red, green, blue);
    }
}
