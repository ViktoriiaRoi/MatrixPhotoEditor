package com.example.matrixphotoeditor.simple_effects;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.matrixphotoeditor.MatrixImage;
import com.example.matrixphotoeditor.R;
import com.example.matrixphotoeditor.simple_effects.BrightnessEffect;
import com.example.matrixphotoeditor.simple_effects.ContrastEffect;
import com.example.matrixphotoeditor.simple_effects.SaturationMatrix;
import com.example.matrixphotoeditor.simple_effects.SimpleEffect;

public class SimpleEffectActivity extends AppCompatActivity {
    static final String MATRIX = "Matrix";
    static final String EFFECT = "Effect";
    static final String BITMAP_ARRAY = "Bitmap";

    static final String BRIGHTNESS = "Brightness";
    static final String CONTRAST = "Contrast";
    static final String SATURATION = "Saturation";

    private ImageView userImage;
    private ActionBar actionBar;
    private ColorMatrix filterMatrix;
    private SimpleEffect thisEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_effect);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        userImage = findViewById(R.id.user_image);
        SeekBar seekBar = findViewById(R.id.seek_bar);

        new MatrixImage(userImage, intent.getByteArrayExtra(BITMAP_ARRAY));
        chooseEffect(intent.getStringExtra(EFFECT));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                previewFilter(thisEffect.getEffectMatrix(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void chooseEffect(String effectName) {
        actionBar.setTitle(effectName);
        switch (effectName) {
            case BRIGHTNESS:
                thisEffect = new BrightnessEffect();
                break;
            case CONTRAST:
                thisEffect = new ContrastEffect();
                break;
            case SATURATION:
                thisEffect = new SaturationMatrix();
        }
    }

    private void previewFilter(ColorMatrix matrix) {
        userImage.setColorFilter(new ColorMatrixColorFilter(matrix));
        filterMatrix = matrix;
    }

    private void saveFilter() {
        Intent intent = new Intent();
        intent.putExtra(MATRIX, filterMatrix.getArray());
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
                saveFilter();
                break;
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}