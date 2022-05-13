package com.example.matrixphotoeditor.simple_effects;

import android.graphics.ColorMatrix;

public class ContrastEffect implements SimpleEffect{

    @Override
    public ColorMatrix getEffectMatrix(int value) {
        float contrast = value / 125.f;
        float scale = contrast + 1.f;
        float translate = (-.5f * scale + .5f) * 255.f;
        float[] contrastMatrix = {
                scale, 0, 0, 0, translate,
                0, scale, 0, 0, translate,
                0, 0, scale, 0, translate,
                0, 0, 0, 1, 0};
        return new ColorMatrix(contrastMatrix);
    }
}
