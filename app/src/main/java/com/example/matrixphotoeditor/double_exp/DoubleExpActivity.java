package com.example.matrixphotoeditor.double_exp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.matrixphotoeditor.MatrixImage;
import com.example.matrixphotoeditor.R;
import com.example.matrixphotoeditor.denoise.DenoiseEffect;

import java.io.IOException;

public class DoubleExpActivity extends AppCompatActivity {
    static final String BITMAP_ARRAY = "Bitmap";
    private final int SELECT_PICTURE = 200;

    private RadioButton defaultRadio;
    private Bitmap firstBitmap, secondBitmap, resultBitmap;
    private MatrixImage matrixImage;
    private DoubleExpEffect thisEffect;
    private int lastValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_exp);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        ImageView userImage = findViewById(R.id.user_image);
        SeekBar seekBar = findViewById(R.id.seek_bar);
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        defaultRadio = findViewById(R.id.radio_default);

        matrixImage = new MatrixImage(userImage, getIntent().getByteArrayExtra(BITMAP_ARRAY));
        firstBitmap = matrixImage.getBitmap();
        resultBitmap = firstBitmap.copy(firstBitmap.getConfig(), true);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                combineImages(i);
                lastValue = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_default:
                        thisEffect = new DefaultEffect();
                        break;
                }
                combineImages(lastValue);
            }
        });
        imageChooser();
    }

    private void combineImages(int i) {
        int width = firstBitmap.getWidth();
        int height = firstBitmap.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int first = firstBitmap.getPixel(x, y);
                int second = secondBitmap.getPixel(x, y);
                resultBitmap.setPixel(x, y, Color.rgb(
                        thisEffect.combinePixels(Color.red(first), Color.red(second), i),
                        thisEffect.combinePixels(Color.green(first), Color.green(second), i),
                        thisEffect.combinePixels(Color.blue(first), Color.blue(second), i)
                ));
            }
        }
        matrixImage.setBitmap(resultBitmap);
    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    private Bitmap scaleBitmap(Bitmap bitmap) {
        int firstWidth = firstBitmap.getWidth();
        int firstHeight = firstBitmap.getHeight();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (firstWidth < firstHeight) {
            height = (firstWidth * height) / width;
            width = firstWidth;
        } else {
            width = (firstHeight * width) / height;
            height = firstHeight;
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    try {
                        secondBitmap = scaleBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri));
                        defaultRadio.setChecked(true);

                    } catch (IOException e) {
                        Toast.makeText(this, "Cannot open this image", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        }
    }

    private void saveChanges() {
        Intent intent = new Intent();
        intent.putExtra(BITMAP_ARRAY, matrixImage.getBitmapArray());
        setResult(RESULT_OK, intent);
        finish();
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