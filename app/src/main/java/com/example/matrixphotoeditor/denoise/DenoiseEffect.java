package com.example.matrixphotoeditor.denoise;

import java.util.ArrayList;

public interface DenoiseEffect {

    int applyFilter(int n, ArrayList<Integer> colorArray);
}
