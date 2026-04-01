package utils;

import model.User;

public class UserGenerator {

    public static User getRandomUser() {
        String email = "user" + System.currentTimeMillis() + "@yandex.ru";
        String password = "password123";
        String name = "Andrey";

        return new User(email, password, name);
    }
}
