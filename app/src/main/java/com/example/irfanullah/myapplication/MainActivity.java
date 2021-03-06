package com.example.irfanullah.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ImageView iv;
    Button ci,ui;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ci = (Button) findViewById(R.id.captureImg);
        iv = (ImageView) findViewById(R.id.imageView);
        ui = (Button) findViewById(R.id.uploadImage);
        ci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,1);
            }
        });

        ui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ByteArrayStream
                java.io.ByteArrayOutputStream byteArray = new java.io.ByteArrayOutputStream();
                //compress the image into PNG format and into the bytearraystream
               bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArray);
                //bytearraystream saved into bytearray
                byte[] barray = byteArray.toByteArray();
                //encode it into base 64

                String encoded = android.util.Base64.encodeToString(barray,android.util.Base64.DEFAULT);
                uploadImage(encoded);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK)
        {

            //getting the bitmap image taken by camera
            this.bitmap = (Bitmap) data.getExtras().get("data");



            iv.setImageBitmap(bitmap);
        }
    }

    void uploadImage(final String image)
    {
       com.android.volley.toolbox.StringRequest request = new com.android.volley.toolbox.StringRequest(com.android.volley.Request.Method.POST, "http://192.168.10.5/android/image.php", new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               try {
                   JSONObject object = new JSONObject(response);
                   String msg = object.getString("msg");
                   Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();

               } catch (JSONException e) {
                   Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();

               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
           }
       }
       ){
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String, String> params = new HashMap<>();
               params.put("image",image);
               return params;
           }
       };

        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(request);
    }
}
