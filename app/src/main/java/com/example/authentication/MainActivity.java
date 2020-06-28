package com.example.authentication;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private EditText userName;
    private EditText userPassword;
    private CheckBox externalStorage;
    private SharedPreferences pref;
    private static final String FILE_NAME = "login.txt";
    private static final String EXTERNAL_KEY = "EXTERNAL_KEY";
    private static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(MainActivity.this, R.string.toast1, Toast.LENGTH_LONG).show();

        init();
        setCheckBoxListener();
        registrationUser();
        saveUser();
    }

    private void init() {
        userName = findViewById(R.id.edit_login);
        userPassword = findViewById(R.id.edit_password);
        externalStorage = findViewById(R.id.checkBox_external_storage);

        pref = getSharedPreferences("myLogin", MODE_PRIVATE);
        externalStorage.setChecked(pref.getBoolean(EXTERNAL_KEY, false));
    }

    private void setCheckBoxListener() {
        externalStorage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (externalStorage.isChecked()) {
                    pref.edit().putBoolean(EXTERNAL_KEY, isChecked).apply();
                    Toast.makeText(MainActivity.this, R.string.toast6, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, R.string.toast7, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registrationUser() {
        Button registration = findViewById(R.id.btn_registration);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (externalStorage.isChecked()) {
                    try {
                        saveExternalStorage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    saveInternalStorage();
                }
            }
        });
    }

    private void saveUser() {
        Button login = findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (externalStorage.isChecked()) {
                    readExternalStorage();
                } else {
                    readInternalStorage();
                }
            }
        });
    }

    private void saveInternalStorage() {
        String userNameText = userName.getText().toString();
        String passwordText = userPassword.getText().toString();

        if (userNameText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(MainActivity.this, R.string.toast1, Toast.LENGTH_LONG).show();
            return;
        }

        try (FileOutputStream fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
             BufferedWriter bw = new BufferedWriter(outputStreamWriter)) {
            bw.write(userNameText);
            bw.write("\n");
            bw.write(passwordText);

            Toast.makeText(MainActivity.this, R.string.toast3, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readInternalStorage() {
        String userNameText = userName.getText().toString();
        String passwordText = userPassword.getText().toString();

        if (userNameText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(MainActivity.this, R.string.toast1, Toast.LENGTH_LONG).show();
            return;
        }

        try (FileInputStream fileInputStream = openFileInput(FILE_NAME);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            String login = reader.readLine();
            String password = reader.readLine();

            if (userNameText.equals(login) || passwordText.equals(password)) {
                Toast.makeText(MainActivity.this, R.string.toast2, Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(MainActivity.this, R.string.toast4, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, R.string.toast5, Toast.LENGTH_LONG).show();
        }
    }

    private void saveExternalStorage() throws IOException {
        int permissionStatus = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            File loginFile = new File (getApplicationContext().getExternalFilesDir(null), "login.txt");
            FileWriter writer = new FileWriter(loginFile, true);
//            writer.append();
            writer.close();
            Toast.makeText(MainActivity.this, R.string.toast8, Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_WRITE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readExternalStorage();
            } else {
                Toast.makeText(MainActivity.this, R.string.toast9, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void readExternalStorage() {
        if (isExternalStorageReadable()) {

        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}