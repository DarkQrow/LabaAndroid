package com.study.labaandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.IOException;

public class ToolBarActivity extends Activity {
    public final String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/NIR/";

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_activity);
        //присвоили кнопку к кнопке на леяуте

        //повесили на него листенера
    }

    public void BacktoMainClick(View v) {
        Intent intent = new Intent(ToolBarActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void CheckPhoto(View v) throws IOException {


    }

}