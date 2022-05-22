package com.example.matrixphotoeditor;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class DeblurActivity extends AppCompatActivity {
    static final String BITMAP_ARRAY = "Bitmap";

    private ImageView userImage;
    private Bitmap initialBitmap, resultBitmap;
    private MatrixImage matrixImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deblur);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        userImage = findViewById(R.id.user_image);
        SeekBar seekBar = findViewById(R.id.seek_bar);

        matrixImage = new MatrixImage(userImage, getIntent().getByteArrayExtra(BITMAP_ARRAY));

        initialBitmap = matrixImage.getCurrentBitmap();
        resultBitmap = initialBitmap;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                applyDeblur(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void applyDeblur(int n) {
    }

    private void saveChanges() {
        //TODO
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