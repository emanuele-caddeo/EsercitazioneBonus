package com.example.esercitazionebonus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

public class GestisciUtentiActivity extends AppCompatActivity {


    public static ArrayList<Person> peopleListTemp = new ArrayList<>();
    ListView listaUtenti;
    PersonAdapter adapter;
    Person person, utente;
    LinearLayout searchTrigger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestisci_utenti);

        Intent intent = getIntent();
        Serializable obj = intent.getSerializableExtra(LoginActivity.PERSON_EXTRA);

        if (obj instanceof Person)
            person = (Person) obj;
        else
            person = new Person();

        searchTrigger = findViewById(R.id.searchTrigger);
        listaUtenti = findViewById(R.id.listaUtenti);
        adapter = new PersonAdapter(this, peopleListTemp);
        listaUtenti.setAdapter(adapter);

        peopleListTemp.clear();
        peopleListTemp.addAll(PersonManager.getUsers());
        peopleListTemp.remove(PersonManager.getPersonSpecified(person));
        SearchView searchView = findViewById(R.id.searchView);

        searchTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(false);
            }
        });

        searchView.setQueryHint("Cerca utente...");
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.navigationFilter(newText, person);
                return false;
            }
        });
    }
}