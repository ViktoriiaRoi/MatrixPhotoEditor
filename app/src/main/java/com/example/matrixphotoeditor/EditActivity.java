package com.example.matrixphotoeditor;

import static com.example.matrixphotoeditor.SimpleEffectActivity.BRIGHTNESS;
import static com.example.matrixphotoeditor.SimpleEffectActivity.CONTRAST;
import static com.example.matrixphotoeditor.SimpleEffectActivity.EFFECT;
import static com.example.matrixphotoeditor.SimpleEffectActivity.MATRIX;

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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    private Uri imageUri;
    private ImageView userImage;
    private Button brightBtn, contrastBtn;
    private ActionBar actionBar;
    private ColorMatrix globalMatrix;

    private final int APPLY_EFFECT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Edit photo");

        userImage = findViewById(R.id.user_image);
        brightBtn = findViewById(R.id.bright_btn);
        contrastBtn = findViewById(R.id.contrast_btn);

        imageUri = getIntent().getData();
        userImage.setImageURI(imageUri);
        globalMatrix = new ColorMatrix();

        brightBtn.setOnClickListener(this);
        contrastBtn.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bright_btn:
                startEffectActivity(BRIGHTNESS);
                break;
            case R.id.contrast_btn:
                startEffectActivity(CONTRAST);
        }
    }

    private void startEffectActivity(String effect) {
        Intent intent = new Intent(this, SimpleEffectActivity.class);
        intent.putExtra(EFFECT, effect);
        intent.putExtra(MATRIX, globalMatrix.getArray());
        intent.setData(imageUri);
        startActivityForResult(intent, APPLY_EFFECT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == APPLY_EFFECT) {
                globalMatrix = new ColorMatrix(data.getFloatArrayExtra("Matrix"));
                userImage.setColorFilter(new ColorMatrixColorFilter(globalMatrix));
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
                //TODO: save image to gallery
                break;
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}