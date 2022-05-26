package com.example.matrixphotoeditor.denoise;

import java.util.ArrayList;

public class MeanFilter implements DenoiseEffect {

    @Override
    public int applyEffect(int n, ArrayList<Integer> colorArray) {
        int convolution = 0;
        for (Integer color : colorArray) {
            convolution += color;
        }
        convolution /= n * n;
        return convolution;
    }
}
