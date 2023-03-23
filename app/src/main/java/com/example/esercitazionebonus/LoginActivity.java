package com.example.esercitazionebonus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    EditText editUsername, editPassword;
    Button logButton;
    TextView signLink;
    public static final String PERSON_EXTRA = "com.example.esercitazionebonus.Person";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
        }

        if (PersonManager.getUsers().size() == 0)
            PersonManager.addPerson(new Person());

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        logButton = findViewById(R.id.logButton);
        signLink = findViewById(R.id.signLink);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editUsername.getText().toString().length() == 0 && editPassword.getText().toString().length() == 0) {
                    editPassword.setError("Inserire la password");
                    editUsername.setError("Inserire l'username");
                } else {
                    if (editUsername.getText().toString().length() == 0)
                        editUsername.setError("Inserire l'username");
                    else if (editPassword.getText().toString().length() == 0)
                        editPassword.setError("Inserire la password");
                    else
                        checkPerson(editUsername.getText().toString(), editPassword.getText().toString());
                }
            }
        });

        signLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });
    }

    public void checkPerson(String username, String password) {
        Person person = new Person(username, password);
        Intent showResult;
        boolean esitoUser = false;
        boolean esitoPass = false;
        for (Person p : PersonManager.getUsers()) {
            if (p.getUsername().equals(person.getUsername())) {
                esitoUser = true;
                if (p.getPassword().equals(person.getPassword())) {
                    esitoPass = true;
                    showResult = new Intent(LoginActivity.this, HomeAdminActivity.class);
                    showResult.putExtra(PERSON_EXTRA, p);
                    startActivity(showResult);
                }
            }
        }

        if(!esitoUser) {
            editUsername.setError("Nome utente errato");
        }

        if(!esitoPass && esitoUser) {
            editPassword.setError("Password errata");
        }
    }
}