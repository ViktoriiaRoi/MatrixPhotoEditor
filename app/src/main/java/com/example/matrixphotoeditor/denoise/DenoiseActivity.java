package com.example.matrixphotoeditor.denoise;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.matrixphotoeditor.BitmapEffectActivity;
import com.example.matrixphotoeditor.R;

import java.util.ArrayList;

public class DenoiseActivity extends BitmapEffectActivity {
    private DenoiseEffect thisEffect;
    private int lastValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denoise);

        actionBar.setTitle("Denoise");
        initializeElements(findViewById(R.id.user_image), findViewById(R.id.seek_bar));

        RadioGroup radioGroup = findViewById(R.id.radio_group);
        RadioButton meanRadio = findViewById(R.id.radio_mean);
        radioGroup.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_mean:
                    thisEffect = new MeanFilter();
                    break;
                case R.id.radio_median:
                    thisEffect = new MedianFilter();
                    break;
                case R.id.radio_gauss:
                    thisEffect = new GaussianFilter();
                    break;
            }
            if (lastValue != 0) {
                previewEffect(lastValue);
            }
        });
        meanRadio.setChecked(true);
    }

    @Override
    protected void previewEffect(int n) {
        int width = initialBitmap.getWidth();
        int height = initialBitmap.getHeight();

        n = 2 * n - 1;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ArrayList<Integer> neighboringRGB = getNeighboringColors(x, y, n, width, height);
                resultBitmap.setPixel(x, y, thisEffect.applyEffect(n, neighboringRGB));
            }
        }
        matrixImage.setBitmap(resultBitmap);
        lastValue = n;
    }

    public ArrayList<Integer> getNeighboringColors(int centerX, int centerY, int n,
                                                              int width, int height) {
        ArrayList<Integer> rgbBlockColors = new ArrayList<>(n*n);
        int s = (n - 1) / 2;
        for (int y = centerY - s; y <= centerY + s; y++) {
            for (int x = centerX - s; x <= centerX + s; x++) {
                if (0 <= x && x < width && 0 <= y && y < height) {
                    rgbBlockColors.add(initialBitmap.getPixel(x,y));
                } else {
                    rgbBlockColors.add(0);
                }
            }
        }
        return rgbBlockColors;
    }
}