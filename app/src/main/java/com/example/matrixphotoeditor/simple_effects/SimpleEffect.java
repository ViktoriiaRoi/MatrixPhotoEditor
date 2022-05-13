package com.example.matrixphotoeditor.simple_effects;

import android.graphics.ColorMatrix;

public interface SimpleEffect {
    ColorMatrix getEffectMatrix(int value);
}
