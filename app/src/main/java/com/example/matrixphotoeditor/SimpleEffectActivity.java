package com.example.matrixphotoeditor;

import static com.example.matrixphotoeditor.EditActivity.EFFECT;
import static com.example.matrixphotoeditor.EditActivity.MATRIX;

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

import com.example.matrixphotoeditor.simple_effects.BrightnessEffect;
import com.example.matrixphotoeditor.simple_effects.ContrastEffect;
import com.example.matrixphotoeditor.simple_effects.SaturationMatrix;
import com.example.matrixphotoeditor.simple_effects.SimpleEffect;

public class SimpleEffectActivity extends AppCompatActivity {
    static final String BRIGHTNESS = "Brightness";
    static final String CONTRAST = "Contrast";
    static final String SATURATION = "Saturation";

    private ImageView userImage;
    private ActionBar actionBar;
    private ColorMatrix initialMatrix, resultMatrix;
    private SimpleEffect thisEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_effect);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        userImage = findViewById(R.id.user_image);
        SeekBar seekBar = findViewById(R.id.seek_bar);

        Intent intent = getIntent();
        Uri imageUri = intent.getData();
        userImage.setImageURI(imageUri);
        initialMatrix = new ColorMatrix(intent.getFloatArrayExtra(MATRIX));
        userImage.setColorFilter(new ColorMatrixColorFilter(initialMatrix));
        chooseEffect(intent.getStringExtra(EFFECT));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                applyMatrix(thisEffect.getEffectMatrix(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }

    void chooseEffect(String effectName) {
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

    void applyMatrix(ColorMatrix matrix) {
        resultMatrix = new ColorMatrix(initialMatrix);
        resultMatrix.postConcat(matrix);
        userImage.setColorFilter(new ColorMatrixColorFilter(resultMatrix));
    }

    void saveChanges() {
        Intent intent = new Intent();
        intent.putExtra(MATRIX, resultMatrix.getArray());
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