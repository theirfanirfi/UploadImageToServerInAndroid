package com.example.irfanullah.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.imageView);
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK)
        {
            java.io.ByteArrayOutputStream byteArray = new java.io.ByteArrayOutputStream();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArray);
            byte[] barray = byteArray.toByteArray();
            String encoded = android.util.Base64.encodeToString(barray,android.util.Base64.DEFAULT);
            android.util.Log.i("Base: ",encoded);
            iv.setImageBitmap(bitmap);
            Toast.makeText(MainActivity.this,data.toString(),Toast.LENGTH_LONG).show();
        }
    }
}
