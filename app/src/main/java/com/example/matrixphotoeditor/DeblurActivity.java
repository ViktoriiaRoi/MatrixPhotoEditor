package com.example.matrixphotoeditor;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class DeblurActivity extends AppCompatActivity {
    static final String BITMAP_ARRAY = "Bitmap";

    private ImageView userImage;
    private Bitmap initialBitmap, resultBitmap;
    private MatrixImage matrixImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deblur);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        userImage = findViewById(R.id.user_image);
        SeekBar seekBar = findViewById(R.id.seek_bar);

        matrixImage = new MatrixImage(userImage, getIntent().getByteArrayExtra(BITMAP_ARRAY));

        initialBitmap = matrixImage.getCurrentBitmap();
        resultBitmap = initialBitmap.copy(initialBitmap.getConfig(), true);;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                deblurImage(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void applyDeblur(int n) {
    }

    public static int findMedian(ArrayList<Integer> colorArray) {
        // O(n logn)
        Collections.sort(colorArray);
        int n = colorArray.size();
        if (n % 2 != 0)
            return colorArray.get(n / 2);
        return (colorArray.get((n - 1) / 2) + colorArray.get((n / 2))) / 2;
    }

    public int[] getMedianColors(int x, int y, int stepX, int stepY, int n) {
        ArrayList<ArrayList<Integer>> rgbBlockColors = new ArrayList<>(3);
        for(int i = 0; i < 3; i++) {
            rgbBlockColors.add(new ArrayList<>());
        }
        for (int i = y; i < y + stepY; i++) {
            for (int j = x; j < x + stepX; j++) {
                int r, g, b;
                r = Color.red(initialBitmap.getPixel(j, i));
                g = Color.green(initialBitmap.getPixel(j, i));
                b = Color.blue(initialBitmap.getPixel(j, i));
                rgbBlockColors.get(0).add(r);
                rgbBlockColors.get(1).add(g);
                rgbBlockColors.get(2).add(b);
            }
        }
        int []rgbMeanColors = {0, 0, 0};
        for (int i = 0; i < rgbMeanColors.length; i++) {
            rgbMeanColors[i] = findMedian(rgbBlockColors.get(i));
        }
        return rgbMeanColors;
    }

    public ArrayList<ArrayList<Integer>> getNeighboringColors(int centerX, int centerY, int n,
                                                              int width, int height) {
        ArrayList<ArrayList<Integer>> rgbBlockColors = new ArrayList<>(3);
        for(int i = 0; i < 3; i++) {
            rgbBlockColors.add(new ArrayList<>());
        }
        int s = (n - 1) / 2;
        for (int y = centerY - s; y < centerY + s; y++) {
            for (int x = centerX - s; x < centerX + s; x++) {
                int []rgb = new int[3];
                if (0 <= x && x < width && 0 <= y && y < height) {
                    rgb[0] = Color.red(initialBitmap.getPixel(x, y));
                    rgb[1] = Color.green(initialBitmap.getPixel(x, y));
                    rgb[2] = Color.blue(initialBitmap.getPixel(x, y));
                } else {
                    rgb[0] = 0;
                    rgb[1] = 0;
                    rgb[2] = 0;
                }
                for (int k = 0; k < 3; k++) {
                    rgbBlockColors.get(k).add(rgb[k]);
                }
            }
        }
        return rgbBlockColors;
    }

    public int applyMeanKernel(int n, ArrayList<Integer> colorArray) {
        int convolution = 0;
        for (Integer color : colorArray) {
            convolution += color;
        }
        convolution /= n * n;
        return convolution;
    }

    public void deblurImage(int n) {
        int width = initialBitmap.getWidth();
        int height = initialBitmap.getHeight();

        n = 3;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ArrayList<ArrayList<Integer>> neighboringRGB = getNeighboringColors(x, y, n, width, height);
                int []colors = new int[3];
//                for (int i = 0; i < 3; i++) {
//                    colors[i] = findMedian(neighboringRGB.get(i));
//                }
                for (int i = 0; i < colors.length; i++) {
                    colors[i] = applyMeanKernel(n, neighboringRGB.get(i));
                }
                resultBitmap.setPixel(x, y, Color.rgb(colors[0], colors[1], colors[2]));
            }
        }
        userImage.setImageBitmap(resultBitmap);

    private void saveChanges() {
        //TODO

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.effect_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.apply_btn:
                saveChanges();
                break;
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}