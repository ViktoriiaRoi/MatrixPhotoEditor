package com.example.matrixphotoeditor.noise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.matrixphotoeditor.MatrixImage;
import com.example.matrixphotoeditor.R;

import java.util.Random;

public class NoiseActivity extends AppCompatActivity {
    static final String BITMAP_ARRAY = "Bitmap";

    private Bitmap initialBitmap, resultBitmap;
    private MatrixImage matrixImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noise);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        ImageView userImage = findViewById(R.id.user_image);
        SeekBar seekBar = findViewById(R.id.seek_bar);

        matrixImage = new MatrixImage(userImage, getIntent().getByteArrayExtra(BITMAP_ARRAY));
        initialBitmap = matrixImage.getBitmap();
        resultBitmap = initialBitmap.copy(initialBitmap.getConfig(), true);;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                noiseImage(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void noiseImage(int n) {
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