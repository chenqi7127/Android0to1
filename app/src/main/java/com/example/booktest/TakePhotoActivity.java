package com.example.booktest;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TakePhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private Uri imageUri;
    private ImageView myPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        Button takePhoto = findViewById(R.id.take_photo_take);
        takePhoto.setOnClickListener(this);
        myPhoto = findViewById(R.id.take_photo_photo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.take_photo_take:
                File file = new File(getExternalCacheDir(),"output.jpg");
                try {
                    if (file.exists())
                        file.delete();
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(this,"wodetianna",file);
                else
                    imageUri = Uri.fromFile(file);
                Intent photoIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                photoIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(photoIntent,1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode)
        {
            case 1:
                if (resultCode == RESULT_OK)
                {
                    try {
                        myPhoto.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
