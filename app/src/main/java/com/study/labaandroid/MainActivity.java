package com.study.labaandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public final String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/NIR/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void GalleryClick(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(directory);
        intent.setDataAndType(uri, "*/*");//specify your type
        startActivity(Intent.createChooser(intent, "Open folder"));
        //startActivity(Intent.createChooser(intent, "Open folder"));

    }
    public void ToolbarClick(View v){
        Intent intent = new Intent(MainActivity.this, ToolBarActivity.class);
        startActivity(intent);
    }
}