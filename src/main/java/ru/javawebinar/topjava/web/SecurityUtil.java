package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    private static User authUser;

    public static void setAuthUser(User user) {
        authUser = user;
    }

    public static int authUserId() {
        return (authUser != null && authUser.getId() != null) ? authUser.getId() : 1;
    }

    public static int authUserCaloriesPerDay() {
        return authUser == null ? DEFAULT_CALORIES_PER_DAY : authUser.getCaloriesPerDay();
    }
}