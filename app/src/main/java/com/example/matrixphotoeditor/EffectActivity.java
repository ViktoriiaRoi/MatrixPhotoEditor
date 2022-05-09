package com.example.matrixphotoeditor;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

public class EffectActivity extends AppCompatActivity {
    private Uri imageUri;
    private ImageView userImage;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect);

        userImage = findViewById(R.id.user_image);
        seekBar = findViewById(R.id.seek_bar);

        imageUri = getIntent().getData();
        userImage.setImageURI(imageUri);

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

    void changeBrightness(int value) {
        float[] matrix = {1, 0, 0, 0, value,
                0, 1, 0, 0, value,
                0, 0, 1, 0, value,
                0, 0, 0, 1, 0 };
        userImage.setColorFilter(new ColorMatrixColorFilter(matrix));
    }
}