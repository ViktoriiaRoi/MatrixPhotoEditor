package com.example.matrixphotoeditor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;

public class SimpleEffectActivity extends AppCompatActivity {
    static final String MATRIX = "Matrix";
    static final String EFFECT = "Effect";
    static final String BRIGHTNESS = "Brightness";
    static final String CONTRAST = "Contrast";

    private Uri imageUri;
    private ImageView userImage;
    private SeekBar seekBar;
    private ActionBar actionBar;
    private ColorMatrix globalMatrix;
    private int preValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_effect);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        userImage = findViewById(R.id.user_image);
        seekBar = findViewById(R.id.seek_bar);

        Intent intent = getIntent();
        imageUri = intent.getData();
        userImage.setImageURI(imageUri);
        globalMatrix = new ColorMatrix(intent.getFloatArrayExtra(MATRIX));
        userImage.setColorFilter(new ColorMatrixColorFilter(globalMatrix));
        chooseEffect(intent.getStringExtra(EFFECT));

    }

    void chooseEffect(String effect) {
        switch (effect) {
            case BRIGHTNESS:
                actionBar.setTitle(BRIGHTNESS);
                seekbarForBright();
                break;
            case CONTRAST:
                actionBar.setTitle(CONTRAST);
                seekbarForContrast();
        }
    }

    void seekbarForBright() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                changeBrightness(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    void seekbarForContrast() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                changeContrast(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    void changeBrightness(int value) {
        float f = value - preValue;
        float[] brightnessMatrix =
                {1, 0, 0, 0, f,
                 0, 1, 0, 0, f,
                 0, 0, 1, 0, f,
                 0, 0, 0, 1, 0 };
        globalMatrix.postConcat(new ColorMatrix(brightnessMatrix));
        userImage.setColorFilter(new ColorMatrixColorFilter(globalMatrix));
        preValue = value;
    }

    void changeContrast(int value) {
        float f = value - preValue;
        f = (259 * (f + 255)) / (255 * (259 - f));
        float[] contrastStep1 =
                {f, 0, 0, 0, -128,
                 0, f, 0, 0, -128,
                 0, 0, f, 0, -128,
                 0, 0, 0, 1, 0 };
        float[] contrastStep2 =
                {1, 0, 0, 0, 128,
                 0, 1, 0, 0, 128,
                 0, 0, 1, 0, 128,
                 0, 0, 0, 1, 0 };
        globalMatrix.postConcat(new ColorMatrix(contrastStep1));
        globalMatrix.postConcat(new ColorMatrix(contrastStep2));
        userImage.setColorFilter(new ColorMatrixColorFilter(globalMatrix));
        preValue = value;
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

    private void saveChanges() {
        Intent intent = new Intent();
        intent.putExtra("Matrix", globalMatrix.getArray());
        setResult(RESULT_OK, intent);
        finish();
    }

}