package com.example.rwstorage;

import static android.content.ContentValues.TAG;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//import com.elotouch.xmlconfig.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;
    Button read;
    TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = findViewById(R.id.output);
        read = findViewById(R.id.read);

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //below getExternal mthd to check external storage availaBLE for read  write
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    if (Build.VERSION.SDK_INT >= 23) {

                        if (checkPermission()) {

                            File sdcard = Environment.getExternalStorageDirectory();
                            sdcard.mkdirs();
                            File dir = new File(sdcard.getAbsolutePath() + "");
                            Log.d("viraj","" + dir);
                            if(dir.exists()) {
                                Log.d("viraj","dir exists");

                                File file = new File(dir, "sample.txt");
                                Log.d("viraj",file.getPath());
//                                FileOutputStream os = null;
                                StringBuilder text = new StringBuilder();
                                try {
                                    Log.d(TAG, "onClick: viraj");
                                    BufferedReader br = new BufferedReader(new FileReader(file));
                                    String line;
                                    while ((line = br.readLine()) != null) {
                                        text.append(line);
                                        text.append('\n');
                                        Log.d("viraj","catch in andy 10");

                                    }
                                    br.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Log.d("viraj",""+ e);
                                    //You'll need to add proper error handling here
                                }
                                output.setText(text);
                            }
                            else {
                                Toast.makeText(MainActivity.this, "File not exist", Toast.LENGTH_SHORT).show();
                                Log.d("viraj","file doesn't exist");
                            }

                        } else {
                            requestPermission(); // Code for permission
                            Log.d("viraj", "request permission");
                        }

                    } else {
                        File sdcard = Environment.getExternalStorageDirectory();
                        File dir = new File(sdcard.getAbsolutePath() + "/text/");
                        Log.d("viraj","sdk less than 23");
                        if(dir.exists()) {
                            File file = new File(dir, "sample.txt");
                            FileInputStream os = null;
                            StringBuilder text = new StringBuilder();
                            try {
                                BufferedReader br = new BufferedReader(new FileReader(file));
                                String line;
                                while ((line = br.readLine()) != null) {
                                    text.append(line);
                                    text.append('\n');
                                    Log.d("viraj","catch in andy 5");

                                }
                                br.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                //You'll need to add proper error handling here
                            }
                            output.setText(text);
                        }
                    }
                }
            }
        });

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to read files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

}