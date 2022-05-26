package com.example.matrixphotoeditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

public abstract class BitmapEffectActivity extends EffectActivity {
    protected Bitmap initialBitmap, resultBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initializeElements(ImageView imageView, SeekBar seekBar) {
        super.initializeElements(imageView, seekBar);

        initialBitmap = matrixImage.getBitmap();
        resultBitmap = initialBitmap.copy(initialBitmap.getConfig(), true);
    }

    @Override
    protected void saveEffect() {
        Intent intent = new Intent();
        intent.putExtra(BITMAP_ARRAY, matrixImage.getBitmapArray());
        setResult(RESULT_OK, intent);
        finish();
    }

}
