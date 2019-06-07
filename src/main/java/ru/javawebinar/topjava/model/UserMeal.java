package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.SortedMap;
import java.util.TreeMap;

public class UserMeal {

    private static final SortedMap<LocalDate, Integer> caloriesSumByDay = new TreeMap<>();

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    public UserMeal(LocalDateTime dateTime, String description, int calories) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        if (!caloriesSumByDay.containsKey(dateTime.toLocalDate())) caloriesSumByDay.put(dateTime.toLocalDate(), calories);
        caloriesSumByDay.put(dateTime.toLocalDate(), caloriesSumByDay.get(dateTime.toLocalDate()) + calories);
    }

    public static SortedMap<LocalDate, Integer> getCaloriesSumByDay() {
        return caloriesSumByDay;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

}
