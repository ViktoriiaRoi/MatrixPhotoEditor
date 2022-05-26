package com.example.matrixphotoeditor;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.os.Bundle;

import com.example.matrixphotoeditor.simple_effects.BrightnessEffect;
import com.example.matrixphotoeditor.simple_effects.ContrastEffect;
import com.example.matrixphotoeditor.simple_effects.SaturationMatrix;
import com.example.matrixphotoeditor.simple_effects.SimpleEffect;

public class SimpleEffectActivity extends EffectActivity {
    static final String MATRIX = "Matrix";
    static final String EFFECT = "Effect";

    static final String BRIGHTNESS = "Brightness";
    static final String CONTRAST = "Contrast";
    static final String SATURATION = "Saturation";

    private ColorMatrix filterMatrix = new ColorMatrix();
    private SimpleEffect thisEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_effect);

        initializeElements(findViewById(R.id.user_image), findViewById(R.id.seek_bar));
        chooseEffect(getIntent().getStringExtra(EFFECT));
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

    @Override
    protected void previewEffect(int i) {
        ColorMatrix matrix = thisEffect.getEffectMatrix(i);
        matrixImage.previewFilter(matrix);
        filterMatrix = matrix;
    }

    @Override
    protected void saveEffect() {
        Intent intent = new Intent();
        intent.putExtra(MATRIX, filterMatrix.getArray());
        setResult(RESULT_OK, intent);
        finish();
    }

}