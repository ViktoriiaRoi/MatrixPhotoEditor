package com.example.matrixphotoeditor.simple_effects;

import android.graphics.ColorMatrix;

public class BrightnessEffect implements SimpleEffect {

    @Override
    public ColorMatrix getEffectMatrix(int value) {
        float bright = value / 1.25f;
        float[] brightnessMatrix = {
                1, 0, 0, 0, bright,
                0, 1, 0, 0, bright,
                0, 0, 1, 0, bright,
                0, 0, 0, 1, 0};
        return new ColorMatrix(brightnessMatrix);
    }
}
