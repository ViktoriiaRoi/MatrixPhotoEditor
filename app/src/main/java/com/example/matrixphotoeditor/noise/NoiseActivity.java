package com.example.matrixphotoeditor.noise;

import android.graphics.Color;
import android.os.Bundle;

import com.example.matrixphotoeditor.BitmapEffectActivity;
import com.example.matrixphotoeditor.R;

import java.util.Random;

public class NoiseActivity extends BitmapEffectActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noise);

        actionBar.setTitle("Noise");
        initializeElements(findViewById(R.id.user_image), findViewById(R.id.seek_bar));
    }

    @Override
    protected void previewEffect(int n) {
        int width = initialBitmap.getWidth();
        int height = initialBitmap.getHeight();

        Random rand = new Random();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int []rgb = new int[3];
                rgb[0] = Color.red(initialBitmap.getPixel(x, y));
                rgb[1] = Color.green(initialBitmap.getPixel(x, y));
                rgb[2] = Color.blue(initialBitmap.getPixel(x, y));
                // sigma 10 * n
                double noise = rand.nextGaussian() * 10 * n;
                for (int i = 0; i < rgb.length; i++) {
                    rgb[i] += noise;
                    if (rgb[i] < 0) {
                        rgb[i] = 0;
                    }
                    if (rgb[i] > 255) {
                        rgb[i] = 255;
                    }
                }
                resultBitmap.setPixel(x, y, Color.rgb(rgb[0], rgb[1], rgb[2]));
            }
        }
        matrixImage.setBitmap(resultBitmap);
    }
}