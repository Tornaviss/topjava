package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UserUtil {
    public static final List<User> USERS = Arrays.asList(
            new User(null, "Valera", "valera@gmail.com", "111111", Role.ROLE_USER),
            new User(null, "Kostyan", "kostyan@gmail.com", "222222", Role.ROLE_USER),
            new User(null, "Miha", "miha@gmail.com", "333333", Role.ROLE_USER)
    );
}
