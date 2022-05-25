package com.example.matrixphotoeditor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MatrixImage {
    private final ImageView imageView;
    private Bitmap bitmap;
    private final int MAX_SIZE = 1000;

    private MatrixImage(ImageView imageView) {
        this.imageView = imageView;
    }

    public MatrixImage(ImageView view, Uri uri) {
        this(view);
        view.setImageURI(uri);

        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap originalBitmap = bitmapDrawable.getBitmap();

        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();

        if (width > MAX_SIZE && width > height) {
            height = (MAX_SIZE * height) / width;
            width = MAX_SIZE;
        } else if (height > MAX_SIZE) {
            width = (MAX_SIZE * width) / height;
            height = MAX_SIZE;
        }

        this.bitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
    }

    public MatrixImage(ImageView view, byte[] byteArray) {
        this(view);
        setBitmapArray(byteArray);
    }

    public byte[] getBitmapArray() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmapArray(byte[] byteArray) {
        Bitmap newBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        setBitmap(newBitmap);
    }

    public void setBitmap(Bitmap newBitmap) {
        this.bitmap = newBitmap;
        imageView.setImageBitmap(newBitmap);
    }

    public void applyFilter(ColorMatrix colorMatrix) {
        Bitmap filterBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(filterBitmap);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        bitmap = filterBitmap;
        imageView.setImageBitmap(bitmap);
    }

    public void saveToGallery(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Toast.makeText(context, "Sorry, we can't save picture on this device", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MatrixPhotoEditor");
        values.put(MediaStore.Images.Media.IS_PENDING, true);

        ContentResolver resolver = context.getContentResolver();
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try {
            OutputStream stream = resolver.openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();
            values.put(MediaStore.Images.Media.IS_PENDING, false);
            resolver.update(uri, values, null, null);
            Toast.makeText(context, "Picture was successfully saved on your device", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "There are some problems with saving. Try again", Toast.LENGTH_SHORT).show();
        }
    }
}