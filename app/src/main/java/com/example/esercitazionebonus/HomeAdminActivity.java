package com.example.esercitazionebonus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class HomeAdminActivity extends AppCompatActivity {

    Person person;
    TextView txtTitolo, resultUsername, resultPassword, resultDate, resultCitta, resultPrivilegi, txtBenvenuto;
    ImageView resultImmagineProfilo;
    TextView cambiaPasswordLink;
    Button logoutButton, gestisciUtentiButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        txtTitolo = findViewById(R.id.txtTitolo);
        resultImmagineProfilo = findViewById(R.id.imgUser);
        resultUsername = findViewById(R.id.txtUsername);
        resultPassword = findViewById(R.id.txtPassword);
        resultDate = findViewById(R.id.txtDataNascita);
        resultCitta = findViewById(R.id.txtCitta);
        resultPrivilegi = findViewById(R.id.txtPrivilegi);
        txtBenvenuto = findViewById(R.id.txtBenvenuto);
        cambiaPasswordLink = findViewById(R.id.cambiaPasswordLink);
        logoutButton = findViewById(R.id.logoutButton);
        gestisciUtentiButton = findViewById(R.id.gestisciUtentiButton);
        Intent intent = getIntent();
        Serializable obj = intent.getSerializableExtra(LoginActivity.PERSON_EXTRA);

        if (obj instanceof Person)
            this.person = (Person) obj;
        else
            person = new Person();

        updateTextView();
        if(person.isFirstAdmin())
            cambiaPasswordLink.setVisibility(View.INVISIBLE);
        else
            cambiaPasswordLink.setVisibility(View.VISIBLE);

        if(person.isAdmin())
            gestisciUtentiButton.setVisibility(View.VISIBLE);
        else
            gestisciUtentiButton.setVisibility(View.INVISIBLE);

        gestisciUtentiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAdminActivity.this, GestisciUtentiActivity.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAdminActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        cambiaPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAdminActivity.this, CambiaPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    void updateTextView() {
        if (person.isAdmin())
            this.txtTitolo.setText("Home Admin");
        else
            this.txtTitolo.setText("Home User");

        this.txtBenvenuto.setText("Benvenuto " + person.getUsername()+"!");
        this.resultUsername.setText(person.getUsername());
        this.resultPassword.setText(person.getPassword());
        if(person.getDate() != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            this.resultDate.setText(format.format(person.getDate().getTime()));
        }
        if(person.getCitta() != null)
            this.resultCitta.setText(person.getCitta().toString());
        if(person.isAdmin()) {
            if (person.isFirstAdmin())
                this.resultPrivilegi.setText("Amministratore principale");
            else
                this.resultPrivilegi.setText("Amministratore");
        }
        else
            this.resultPrivilegi.setText("Utente");

        if(person.getImmagineProfilo()!=null)
            this.resultImmagineProfilo.setImageURI(Uri.parse(person.getImmagineProfilo()));

    }
}