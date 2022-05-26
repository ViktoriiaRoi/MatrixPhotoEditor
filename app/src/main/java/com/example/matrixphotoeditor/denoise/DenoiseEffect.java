package com.example.matrixphotoeditor.denoise;

import java.util.ArrayList;

public interface DenoiseEffect {

    int applyEffect(int n, ArrayList<Integer> colorArray);
}
