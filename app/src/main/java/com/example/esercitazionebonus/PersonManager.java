package com.example.esercitazionebonus;

import java.util.ArrayList;

public class PersonManager {
    private static final ArrayList<Person> users = new ArrayList<>();


    public static void addPerson(Person p) {
        users.add(p);
    }


    public static ArrayList<Person> getUsers() {
        return users;
    }

    public static ArrayList<Person> getAdmins() {
        ArrayList<Person> admins = new ArrayList<>();
        for (Person p : users) {
            if (p.isAdmin())
                admins.add(p);
        }
        return admins;
    }

    public static ArrayList<Person> getUsersByName(String name) {
        ArrayList<Person> usersByName = new ArrayList<>();
        for (Person p : users) {
            if (p.getUsername().equals(name))
                usersByName.add(p);
        }
        return usersByName;
    }

    public static void printUsers() {
        for (Person p : users) {
            System.out.println(p.getUsername());
        }
    }

    public static Person getPersonSpecified(Person person) {for (Person p : PersonManager.getUsers()) {
        if (p.getUsername().equals(person.getUsername())) {
            return p;
        }
    }
        return null;
    }
}
