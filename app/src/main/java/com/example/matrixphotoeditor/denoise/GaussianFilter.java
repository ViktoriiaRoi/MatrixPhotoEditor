package com.example.matrixphotoeditor.denoise;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;

public class GaussianFilter implements DenoiseEffect {
    private double gaussianFunction(int x, int y, double sigma) {
        double variance = Math.pow(sigma, 2);
        double power = -(Math.pow(x, 2) + Math.pow(y, 2)) / (2 * variance);
        return Math.pow(Math.E, power) / (2 * Math.PI * variance);
    }

    @Override
    public int applyEffect(int n, ArrayList<Integer> colorArray) {
        int s = (n - 1) / 2;

        double red = 0, green = 0, blue = 0;
        double kernel, kernel_sum = 0;
        int color, idx = 0;

        for (int y = -s; y <= s; y++) {
            for (int x = -s; x <= s; x++) {
                kernel = gaussianFunction(x, y, (double) (n - 1) / 4);
                color = colorArray.get(idx);
                red += Color.red(color) * kernel;
                green += Color.green(color) * kernel;
                blue += Color.blue(color) * kernel;
                kernel_sum += kernel;
                idx++;
            }
        }
        red /= kernel_sum;
        green /= kernel_sum;
        blue /= kernel_sum;

        return Color.rgb((int) red, (int) green, (int) blue);
    }
}

