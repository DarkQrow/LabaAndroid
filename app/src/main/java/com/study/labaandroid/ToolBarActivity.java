package com.study.labaandroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;

public class ToolBarActivity extends Activity {
    private boolean backgraoundCheck = false;
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

    @SuppressLint("UseCompatLoadingForDrawables")
    public void changeBackground(View v) {
        LinearLayout layout = findViewById(R.id.toolbarlayout);
        if (!backgraoundCheck) {
            layout.setBackground(getDrawable(R.drawable.toolbarbackground_2));
            backgraoundCheck = true;
        } else {
            layout.setBackground(getDrawable(R.drawable.toolbarbackground_1));
            backgraoundCheck = false;
        }
    }

}