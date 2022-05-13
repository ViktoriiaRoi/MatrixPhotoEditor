package com.example.matrixphotoeditor.simple_effects;

import android.graphics.ColorMatrix;

public class SaturationMatrix implements SimpleEffect{
    @Override
    public ColorMatrix getEffectMatrix(int value) {
        float saturation = value / 100.f + 1;
        float a = (2 * saturation + 1) / 3.f;
        float b = (1 - saturation) / 3.f;
        float[] saturationMatrix = {
            a, b, b, 0, 0,
            b, a, b, 0, 0,
            b, b, a, 0, 0,
            0, 0, 0, 1, 0};
        return new ColorMatrix(saturationMatrix);
    }
}
