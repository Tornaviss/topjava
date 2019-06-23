package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.Collection;

public interface MealService {
    Meal create(Meal meal, int userId);
    Meal update(Meal meal, int userId);
    void delete(int id, int userId);
    Meal get(int id, int userID);
    Collection<Meal> getAll(int userId);
    void applyDateFilter(LocalDate from, LocalDate to);

    void clearDateFilters();

    LocalDate getDateFilterFrom();

    LocalDate getDateFilterTo();
}