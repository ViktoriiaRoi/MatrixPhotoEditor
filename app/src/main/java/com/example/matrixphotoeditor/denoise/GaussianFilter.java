package com.example.matrixphotoeditor.denoise;

import java.util.ArrayList;
import java.util.Collections;

public class GaussianFilter implements DenoiseEffect {
    public double gaussianFunction(int x, int y, double sigma) {
        double variance = Math.pow(sigma, 2);
        double power = -(Math.pow(x, 2) + Math.pow(y, 2)) / (2 * variance);
        return Math.pow(Math.E, power) / (2 * Math.PI * variance);
    }

    @Override
    public int applyEffect(int n, ArrayList<Integer> colorArray) {
        double[] kernel = new double[n * n];
        int idx = 0;
        int s = (n - 1) / 2;
        for (int y = -s; y <= s; y++) {
            for (int x = -s; x <= s; x++) {
                kernel[idx] = gaussianFunction(x, y, (double) (n - 1) / 4);
                idx++;
            }
        }
        double convolution = 0;
        double kernel_sum = 0;
        for (int i = 0; i < n * n; i++) {
            convolution += colorArray.get(i) * kernel[i];
            kernel_sum += kernel[i];
        }
        convolution /= kernel_sum;
        return (int) convolution;
    }
}
