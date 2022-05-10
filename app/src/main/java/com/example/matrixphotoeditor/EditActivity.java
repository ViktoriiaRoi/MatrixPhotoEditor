package com.example.matrixphotoeditor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class EditActivity extends AppCompatActivity {
    private Uri imageUri;
    private ImageView userImage;
    private Button effectBtn;

    private final int APPLY_EFFECT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userImage = findViewById(R.id.user_image);
        effectBtn = findViewById(R.id.effect_btn);

        imageUri = getIntent().getData();
        userImage.setImageURI(imageUri);

        effectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEffectActivity();
            }
        });
    }

    private void startEffectActivity() {
        Intent intent = new Intent(this, EffectActivity.class);
        intent.setData(imageUri);
        startActivityForResult(intent, APPLY_EFFECT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == APPLY_EFFECT) {
                // TODO: update image
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