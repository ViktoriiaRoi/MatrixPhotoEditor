package com.example.matrixphotoeditor.denoise;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;

public class MedianFilter implements DenoiseEffect {

    @Override
    public int applyEffect(int n, ArrayList<Integer> colorArray) {
        int size = colorArray.size();
        ArrayList<Integer> redArr = new ArrayList<>(size);
        ArrayList<Integer> greenArr = new ArrayList<>(size);
        ArrayList<Integer> blueArr = new ArrayList<>(size);

        int color;
        for (int i = 0; i < size; i++) {
            color = colorArray.get(i);
            redArr.add(Color.red(color));
            greenArr.add(Color.green(color));
            blueArr.add(Color.blue(color));
        }

        // O(n logn)
        Collections.sort(redArr);
        Collections.sort(greenArr);
        Collections.sort(blueArr);

        if (size % 2 != 0)
            return Color.rgb(redArr.get(size / 2), greenArr.get(size / 2), blueArr.get(size / 2));

        int red = (redArr.get((size - 1) / 2) + redArr.get((size / 2))) / 2;
        int green = (greenArr.get((size - 1) / 2) + greenArr.get((size / 2))) / 2;
        int blue = (blueArr.get((size - 1) / 2) + blueArr.get((size / 2))) / 2;
        return Color.rgb(red, green, blue);
    }
}
