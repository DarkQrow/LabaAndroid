package com.study.labaandroid;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.study.labaandroid.data.RetrofitAPI;
import com.study.labaandroid.data.loginModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("java:S125")
public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener, Camera.PictureCallback, Camera.PreviewCallback, Camera.AutoFocusCallback {

    private static final String DIRECTORY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/NIR/";
    private static final int NOTIFICATION_ID = 25;
    private static final String CHANNEL_ID = "chanelID";

    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private SurfaceView preview;
    public final String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/NIR/";
    Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("+79045095433", null, "Вы успешно открыли приложение. Зарегистрируетесь как я сделаю регистрацию", null, null);
        postData("userName", "password");
        dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Заголовок диалога");
        dialog.setContentView(R.layout.dialog_view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        preview = (SurfaceView) findViewById(R.id.SurfaceView01);


        surfaceHolder = preview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @Override
    protected void onResume() {
        super.onResume();
        camera = Camera.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.setPreviewCallback(this);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        float aspect = (float) previewSize.width / previewSize.height;
        int previewSurfaceWidth = preview.getWidth();
        int previewSurfaceHeight = preview.getHeight();
        ViewGroup.LayoutParams lp = preview.getLayoutParams();
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            // портретный вид
            camera.setDisplayOrientation(90);
            lp.height = previewSurfaceHeight;
            lp.width = (int) (previewSurfaceHeight / aspect);
            ;
        } else {
            camera.setDisplayOrientation(0);
            lp.width = previewSurfaceWidth;
            lp.height = (int) (previewSurfaceWidth / aspect);
        }
        preview.setLayoutParams(lp);
        camera.startPreview();
    }
    public void galleryClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        Uri uri = Uri.parse(DIRECTORY);
        intent.setDataAndType(uri, "image/*");//specify your type
        startActivity(Intent.createChooser(intent, "Open folder"));
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void onClick(View v) {
        camera.autoFocus(this);
    }

    @Override
    public void onPictureTaken(byte[] paramArrayOfByte, Camera paramCamera) {
        try {
            File saveDir = new File(directory);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            Bitmap image = BitmapFactory.decodeByteArray(paramArrayOfByte, 0, paramArrayOfByte.length);
            image = RotateBitmap(image, 90);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            image.recycle();
            FileOutputStream t = new FileOutputStream(String.format(directory + "%d.jpg", System.currentTimeMillis()));
            t.write(byteArray);
        } catch (Exception ignored) {
        }
        paramCamera.startPreview();
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public void onAutoFocus(boolean paramBoolean, Camera paramCamera) {
    }


    @Override
    public void onPreviewFrame(byte[] paramArrayOfByte, Camera paramCamera) {
    }


    public void toolbarClick(View v) {
        Intent intent = new Intent(MainActivity.this, ToolBarActivity.class);
        startActivity(intent);
    }


    public void photoClick(View v) {

        camera.takePicture(null, null, null, this);
        dialog.show();


        Log.d("Photo", "Click!");

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(android.R.drawable.ic_menu_camera).setContentTitle("NILFULLSCREEN").setContentText("Photo is made.Coding...").setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent).setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
    private void postData(String name, String password) {

        // below line is for displaying our progress bar.

        // on below line we are creating a retrofit
        // builder and passing our base url
        Retrofit retrofit = new Retrofit.Builder()
                // .baseUrl("https://login1.requestcatcher.com")
                .baseUrl("https://reqres.in/api/")
                // as we are sending data in json format so
                // we have to add Gson converter factory
                .addConverterFactory(GsonConverterFactory.create())
                // at last we are building our retrofit builder.
                .build();
        // below line is to create an instance for our retrofit api class.
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        // passing data from our text fields to our modal class.
        loginModel modal = new loginModel(name, password);

        // calling a method to create a post and passing our modal class.
        Call<loginModel> call = retrofitAPI.createPost(modal);

        // on below line we are executing our method.
        call.enqueue(new Callback<loginModel>() {
            @Override
            public void onResponse(Call<loginModel> call, Response<loginModel> response) {
                // this method is called when we get response from our api.

                // on below line we are setting empty text
                // to our both edit text.

                // we are getting response from our body
                // and passing it to our modal class.
                loginModel responseFromAPI = response.body();

                // on below line we are getting our data from modal class and adding it to our string.
                String responseString = "Response Code : " + response.code()
                        + "\nlogin : " + responseFromAPI.getLogin() + "\n"
                        + "password : " + responseFromAPI.getPassword()+"\n"
                        + "botToken : "+ responseFromAPI.getBotToken();

                // below line we are setting our
                // string to our text view.
                Toast.makeText(MainActivity.this,responseString,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<loginModel> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                Toast.makeText(MainActivity.this,"Error found is : " + t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "PhotoClick";
            String description = "Photo is codding";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
