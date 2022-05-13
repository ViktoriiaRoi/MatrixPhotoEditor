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
import com.example.matrixphotoeditor.simple_effects.SimpleEffect;

public class SimpleEffectActivity extends AppCompatActivity {
    static final String BRIGHTNESS = "Brightness";
    static final String CONTRAST = "Contrast";

    private Uri imageUri;
    private ImageView userImage;
    private SeekBar seekBar;
    private ActionBar actionBar;
    private ColorMatrix globalMatrix;
    private SimpleEffect thisEffect;
    private int value, preValue = 0;

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

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                value = i - preValue;
                applyMatrix(thisEffect.getEffectMatrix(value));
                preValue = value;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }

    void chooseEffect(String effectName) {
        switch (effectName) {
            case BRIGHTNESS:
                actionBar.setTitle(BRIGHTNESS);
                thisEffect = new BrightnessEffect();
                break;
            case CONTRAST:
                actionBar.setTitle(CONTRAST);
                thisEffect = new ContrastEffect();
        }
    }

    void applyMatrix(ColorMatrix matrix) {
        globalMatrix.postConcat(matrix);
        userImage.setColorFilter(new ColorMatrixColorFilter(globalMatrix));
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
        intent.putExtra(MATRIX, globalMatrix.getArray());
        setResult(RESULT_OK, intent);
        finish();
    }

}