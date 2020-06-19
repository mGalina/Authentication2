package com.example.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.BreakIterator;

public class MainActivity extends AppCompatActivity {

    private EditText userName;
    private EditText userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.edit_login);
        userPassword = findViewById(R.id.edit_password);

        Toast.makeText(MainActivity.this, R.string.toast1, Toast.LENGTH_LONG).show();

        saveUser();
        registrationUser();
    }

    private void saveUser() {
        Button login = findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userName.getText().toString().isEmpty() || userPassword.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.toast1, Toast.LENGTH_LONG).show();
                } else {
                    String filename = "login.txt";
                    // Получим входные байты из файла которых нужно прочесть.
                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = openFileInput(filename);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // Декодируем байты в символы
                    assert fileInputStream != null;
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    // Читаем данные из потока ввода, буферизуя символы так, чтобы обеспечить эффективную запись отдельных символов.
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    try {
                        reader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this, R.string.toast2, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registrationUser() {
        Button registration = findViewById(R.id.btn_registration);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userName.getText().toString().isEmpty() || !userPassword.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.toast1, Toast.LENGTH_LONG).show();
                } else {
                    // Создадим файл и откроем поток для записи данных
                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = openFileOutput("file_login", MODE_PRIVATE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // Обеспечим переход символьных потока данных к байтовым потокам.
                    assert fileOutputStream != null;
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                    // Запишем текст в поток вывода данных, буферизуя символы так, чтобы обеспечить эффективную запись отдельных символов.
                    BufferedWriter bw = new BufferedWriter(outputStreamWriter);
                    // запись данных
                    try {
                        bw.write("Содержимое файла");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // закрытие потока
                    try {
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this, R.string.toast3, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}