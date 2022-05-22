package com.example.matrixphotoeditor;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    static final String MATRIX = "Matrix";
    static final String EFFECT = "Effect";
    static final String BITMAP_ARRAY = "Bitmap";

    static final String BRIGHTNESS = "Brightness";
    static final String CONTRAST = "Contrast";
    static final String SATURATION = "Saturation";

    private final int SIMPLE_EFFECT = 1;
    private final int DEBLUR_EFFECT = 2;

    private MatrixImage matrixImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Edit photo");

        ImageView imageView = findViewById(R.id.user_image);
        Uri imageUri = getIntent().getData();
        matrixImage = new MatrixImage(imageView, imageUri);

        initializeButtons();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bright_btn:
                startSimpleEffect(BRIGHTNESS);
                break;
            case R.id.contrast_btn:
                startSimpleEffect(CONTRAST);
                break;
            case R.id.saturation_btn:
                startSimpleEffect(SATURATION);
                break;
            case R.id.deblur_btn:
                startDeblurEffect();
        }
    }

    private void initializeButtons() {
        Button brightBtn = findViewById(R.id.bright_btn);
        Button contrastBtn = findViewById(R.id.contrast_btn);
        Button saturationBtn = findViewById(R.id.saturation_btn);
        Button deblurBtn = findViewById(R.id.deblur_btn);

        brightBtn.setOnClickListener(this);
        contrastBtn.setOnClickListener(this);
        deblurBtn.setOnClickListener(this);
        saturationBtn.setOnClickListener(this);
    }

    private void startSimpleEffect(String effect) {
        Intent intent = new Intent(this, SimpleEffectActivity.class);
        intent.putExtra(EFFECT, effect);
        intent.putExtra(BITMAP_ARRAY, matrixImage.getBitmapArray());
        startActivityForResult(intent, SIMPLE_EFFECT);
    }

    private void startDeblurEffect() {
        Intent intent = new Intent(this, DeblurActivity.class);
        intent.putExtra(BITMAP_ARRAY, matrixImage.getBitmapArray());
        startActivityForResult(intent, DEBLUR_EFFECT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SIMPLE_EFFECT:
                    ColorMatrix filterMatrix = new ColorMatrix(data.getFloatArrayExtra(MATRIX));
                    matrixImage.applyFilter(filterMatrix);
                    break;

                case DEBLUR_EFFECT:
                    //TODO
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_btn:
                matrixImage.saveToGallery(getApplicationContext());
                break;
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}