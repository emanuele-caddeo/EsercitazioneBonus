package com.example.esercitazionebonus;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class PersonAdapter extends ArrayAdapter<Person> {

    public PersonAdapter(Context context, ArrayList<Person> people) {
        super(context, 0, people);
    }

    public void navigationFilter(String textSearched, Person person) {
        ArrayList<Person> tempList = new ArrayList<>(PersonManager.getUsers());
        textSearched = textSearched.toLowerCase(Locale.getDefault());
        GestisciUtentiActivity.peopleListTemp.clear();

        if(textSearched.length() == 0) {
            GestisciUtentiActivity.peopleListTemp.addAll(tempList);
        }
        else {
            for (Person p : tempList) {
                if (p.getUsername().toLowerCase(Locale.getDefault()).contains(textSearched))
                    GestisciUtentiActivity.peopleListTemp.add(p);
            }
        }
        GestisciUtentiActivity.peopleListTemp.remove(PersonManager.getPersonSpecified(person)); //rimuovo l'utente corrente, ma forse non è necessario
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_elemento_lista, parent, false);
        }

        Person person = getItem(position);

        ImageView immagine = convertView.findViewById(R.id.immagine);
        immagine.setImageURI(Uri.parse(person.getImmagineProfilo()));
        TextView username = convertView.findViewById(R.id.username);
        username.setText(person.getUsername());
        Switch switchAdmin = convertView.findViewById(R.id.switchAdmin);
        TextView text = convertView.findViewById(R.id.text);

        if (person.isFirstAdmin()) {
            switchAdmin.setVisibility(View.GONE);
            text.setText("admin principale");
            text.setTextSize(20);
        }
        else {
            switchAdmin.setVisibility(View.VISIBLE);
            text.setText("admin");
            text.setTextSize(14);
        }

        switchAdmin.setChecked(person.isAdmin());

        switchAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonManager.getPersonSpecified(person).setAdmin(!person.isAdmin());
            }
        });

        return convertView;
    }
}

