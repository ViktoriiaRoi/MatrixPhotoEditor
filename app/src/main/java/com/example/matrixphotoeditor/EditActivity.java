package com.example.matrixphotoeditor;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.matrixphotoeditor.denoise.DenoiseActivity;
import com.example.matrixphotoeditor.double_exp.DoubleExpActivity;
import com.example.matrixphotoeditor.noise.NoiseActivity;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    static final String MATRIX = "Matrix";
    static final String EFFECT = "Effect";
    static final String BITMAP_ARRAY = "Bitmap";

    static final String BRIGHTNESS = "Brightness";
    static final String CONTRAST = "Contrast";
    static final String SATURATION = "Saturation";

    private final int SIMPLE_EFFECT = 1;
    private final int BITMAP_EFFECT = 2;

    private MatrixImage matrixImage;
    private AlertDialog alertDialog;
    private boolean saved = true;

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
        alertDialog = buildAlertDialog();
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
            case R.id.denoise_btn:
                startBitmapEffect(DenoiseActivity.class);
                break;
            case R.id.noise_btn:
                startBitmapEffect(NoiseActivity.class);
                break;
            case R.id.double_exp_btn:
                startBitmapEffect(DoubleExpActivity.class);
        }
    }

    private void initializeButtons() {
        Button brightBtn = findViewById(R.id.bright_btn);
        Button contrastBtn = findViewById(R.id.contrast_btn);
        Button saturationBtn = findViewById(R.id.saturation_btn);
        Button denoiseBtn = findViewById(R.id.denoise_btn);
        Button noiseBtn = findViewById(R.id.noise_btn);
        Button doubleExpBtn = findViewById(R.id.double_exp_btn);

        brightBtn.setOnClickListener(this);
        contrastBtn.setOnClickListener(this);
        saturationBtn.setOnClickListener(this);
        denoiseBtn.setOnClickListener(this);
        noiseBtn.setOnClickListener(this);
        doubleExpBtn.setOnClickListener(this);
    }

    private AlertDialog buildAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("All unsaved changes will be lost")
                .setCancelable(false)
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("Discard changes?");
        return alert;
    }

    private void startSimpleEffect(String effect) {
        Intent intent = new Intent(this, SimpleEffectActivity.class);
        intent.putExtra(EFFECT, effect);
        intent.putExtra(BITMAP_ARRAY, matrixImage.getBitmapArray());
        startActivityForResult(intent, SIMPLE_EFFECT);
    }

    private void startBitmapEffect(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        intent.putExtra(BITMAP_ARRAY, matrixImage.getBitmapArray());
        startActivityForResult(intent, BITMAP_EFFECT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SIMPLE_EFFECT:
                    ColorMatrix filterMatrix = new ColorMatrix(data.getFloatArrayExtra(MATRIX));
                    matrixImage.applyFilter(filterMatrix);
                    break;

                case BITMAP_EFFECT:
                    matrixImage.setBitmapArray(data.getByteArrayExtra(BITMAP_ARRAY));
                    break;
            }
            saved = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void discardChanges(){
        if (saved) {
            finish();
        } else {
            alertDialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_btn:
                matrixImage.saveToGallery(getApplicationContext());
                saved = true;
                break;
            case android.R.id.home:
                discardChanges();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        discardChanges();
    }
}