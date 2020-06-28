package com.example.authentication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(MainActivity.this, R.string.fill_in_the_fields, Toast.LENGTH_LONG).show();

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
                    Toast.makeText(MainActivity.this, R.string.external_storage, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, R.string.internal_storage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registrationUser() {
        final Button registration = findViewById(R.id.btn_registration);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (externalStorage.isChecked()) {
                    register(getExternalFilesDir(null));
                } else {
                    register(getFilesDir());
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
                    login(getExternalFilesDir(null));
                } else {
                    login(getFilesDir());
                }
            }
        });
    }

    private void register(File rootDir) {
        String userNameText = userName.getText().toString();
        String passwordText = userPassword.getText().toString();

        if (userNameText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(MainActivity.this, R.string.fill_in_the_fields, Toast.LENGTH_LONG).show();
            return;
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(rootDir, FILE_NAME));
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
             BufferedWriter bw = new BufferedWriter(outputStreamWriter)) {
            bw.write(userNameText);
            bw.write("\n");
            bw.write(passwordText);

            Toast.makeText(MainActivity.this, R.string.registration_completed, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login(File rootDir) {
        String userNameText = userName.getText().toString();
        String passwordText = userPassword.getText().toString();

        if (userNameText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(MainActivity.this, R.string.fill_in_the_fields, Toast.LENGTH_LONG).show();
            return;
        }

        try (FileInputStream fileInputStream = new FileInputStream(new File(rootDir, FILE_NAME));
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            String login = reader.readLine();
            String password = reader.readLine();

            if (userNameText.equals(login) || passwordText.equals(password)) {
                Toast.makeText(MainActivity.this, R.string.authorization_complete, Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(MainActivity.this, R.string.wrong_data, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, R.string.no_user, Toast.LENGTH_LONG).show();
        }
    }
}