package com.example.saveimagetosqlite;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;

    private GridView simpleGrid;
    protected DATABASE controller;
    private ArrayList<PostData_image> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });


        controller = new DATABASE(this);
        simpleGrid = (GridView) findViewById(R.id.gridview); // init GridView
        refreshGrid();
    }

    protected void refreshGrid(){
        images = controller.select_image_from_database("SELECT IMAGE_ID, IMAGE FROM Image");

        Double datasize = (double)(controller.getDatabaseSize());
        getSupportActionBar().setSubtitle(images.size() + " images : " +  String.format("%.2f", datasize / 1048576.00) + " MB");

        // Create an object of CustomAdapter and set Adapter to GirdView
        GridViewAdapter gridViewAdapter = new GridViewAdapter(getApplicationContext(), images);
        simpleGrid.setAdapter(gridViewAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            if (bitmap != null){
                PostData_image image = new PostData_image();
                image.image = getBytes(bitmap);
                controller.Insert_into_image(image);
            }else {
                Toast.makeText(this, "Something was wrrong ", Toast.LENGTH_LONG).show();
            }

            refreshGrid();

        }
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
}
