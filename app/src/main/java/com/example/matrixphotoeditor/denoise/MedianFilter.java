package com.example.matrixphotoeditor.denoise;

import java.util.ArrayList;
import java.util.Collections;

public class MedianFilter implements DenoiseEffect {

    @Override
    public int applyEffect(int n, ArrayList<Integer> colorArray) {
        // O(n logn)
        Collections.sort(colorArray);
        n = colorArray.size();
        if (n % 2 != 0)
            return colorArray.get(n / 2);
        return (colorArray.get((n - 1) / 2) + colorArray.get((n / 2))) / 2;
    }
}
