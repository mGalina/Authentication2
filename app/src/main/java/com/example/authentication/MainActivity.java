package com.example.authentication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
    private static final String save_key = "save_key";
    private static final String FILENAME = "login.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(MainActivity.this, R.string.toast1, Toast.LENGTH_LONG).show();

        init();
        saveUser();
    }

    private void init() {
        userName = findViewById(R.id.edit_login);
        userPassword = findViewById(R.id.edit_password);
        externalStorage = findViewById(R.id.checkBox_external_storage);

        pref = getSharedPreferences("myLogin", MODE_PRIVATE);
    }

    public void onClickCheck(View view) {
        externalStorage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(MainActivity.this, R.string.toast6, Toast.LENGTH_LONG).show();
                    savePrefsData();
                } else {
                    registrationUser();
                }
            }
        });
    }

    private void savePrefsData() {
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(save_key, Boolean.parseBoolean(externalStorage.getText().toString()));
        edit.apply();
    }

    private void saveUser() {
        Button login = findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameText = userName.getText().toString();
                String passwordText = userPassword.getText().toString();
                if (userNameText.isEmpty() || passwordText.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.toast1, Toast.LENGTH_LONG).show();
                    return;
                }

                try (FileInputStream fileInputStream = openFileInput(FILENAME);
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
        });
    }

    private void registrationUser() {
        Button registration = findViewById(R.id.btn_registration);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameText = userName.getText().toString();
                String passwordText = userPassword.getText().toString();

                if (userNameText.isEmpty() || passwordText.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.toast1, Toast.LENGTH_LONG).show();
                    return;
                }

                try (FileOutputStream fileOutputStream = openFileOutput(FILENAME, MODE_PRIVATE);
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
        });
    }
}