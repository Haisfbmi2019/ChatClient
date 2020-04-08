package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.LinkedList;
import java.util.List;

public class UserList {
    private final List<User> listOfUsers = new LinkedList<>();

    public static UserList fromJSON(String s) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(s, UserList.class);
    }

    public void show() {
        for (User user: listOfUsers) {
            System.out.println(user.toString());
        }
    }

}