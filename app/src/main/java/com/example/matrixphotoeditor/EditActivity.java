package com.example.matrixphotoeditor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class EditActivity extends AppCompatActivity {
    private Uri imageUri;
    private ImageView userImage;
    private Button effectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

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
        startActivity(intent);
    }
}