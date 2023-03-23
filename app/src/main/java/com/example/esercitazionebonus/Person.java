package com.example.esercitazionebonus;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.core.content.ContextCompat;

import java.io.Serializable;
import java.util.Calendar;

public class Person implements Serializable {
    private String username, password, citta;
    private Calendar date;
    private Boolean isAdmin;
    private Boolean isFirstAdmin;
    private String immagineProfilo;

    public Person(String username, String password, String citta, Calendar date, Uri immagineProfilo) {
        this.username = username;
        this.password = password;
        this.citta = citta;
        this.date = date;
        this.immagineProfilo = immagineProfilo.toString();
        this.isAdmin = false;
        this.isFirstAdmin = false;
    }

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
        this.isAdmin = false;
    }

    public Person() {
        this.username = "admin";
        this.password = "admin";
        this.citta = "Undefined";
        this.immagineProfilo = "android.resource://com.example.esercitazionebonusEmanueleCaddeo/drawable/placeholder";
        this.isAdmin = true;
        this.isFirstAdmin = true;
    }

    public String getUsername() { return username; }

    public void setUsername(String name) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String surname) { this.password = password; }

    public String getCitta() { return citta; }

    public void setCitta(String citta) { this.citta = citta; }

    public Calendar getDate() { return date; }

    public void setDate(Calendar date) { this.date = date; }

    public String getImmagineProfilo() { return immagineProfilo; }

    public void setImmagineProfilo(Uri immagineProfilo) { this.immagineProfilo = immagineProfilo.toString(); }

    public Boolean isAdmin() { return isAdmin; }

    public Boolean isFirstAdmin() { return isFirstAdmin; }

    public void setAdmin(Boolean admin) { isAdmin = admin; }

    @Override
    public String toString(){
        return "Username: " + username + "\nPassword: " + password + "\nCittà: " + citta + "\nData di nascita: " + date + "\nImmagine profilo: " + immagineProfilo + "\nAmministratore: " + isAdmin;
    }
}
