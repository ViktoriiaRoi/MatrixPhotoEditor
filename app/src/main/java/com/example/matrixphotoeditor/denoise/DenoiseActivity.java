package com.example.matrixphotoeditor.denoise;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.matrixphotoeditor.MatrixImage;
import com.example.matrixphotoeditor.R;

public class DenoiseActivity extends AppCompatActivity{
    static final String BITMAP_ARRAY = "Bitmap";

    private Bitmap initialBitmap, resultBitmap;
    private MatrixImage matrixImage;
    private DenoiseEffect thisEffect;
    private int lastValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denoise);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        ImageView userImage = findViewById(R.id.user_image);
        SeekBar seekBar = findViewById(R.id.seek_bar);
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        RadioButton meanRadio = findViewById(R.id.radio_mean);

        matrixImage = new MatrixImage(userImage, getIntent().getByteArrayExtra(BITMAP_ARRAY));
        initialBitmap = matrixImage.getBitmap();
        resultBitmap = initialBitmap.copy(initialBitmap.getConfig(), true);;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                deblurImage(i);
                lastValue = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
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
                    deblurImage(lastValue);
                }
            }
        });
        meanRadio.setChecked(true);
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

    public void deblurImage(int n) {
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
    }

    private void saveChanges() {
        Intent intent = new Intent();
        intent.putExtra(BITMAP_ARRAY, matrixImage.getBitmapArray());
        setResult(RESULT_OK, intent);
        finish();
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