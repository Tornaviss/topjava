package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.Collection;

public interface MealRepository {
    Meal save(Meal meal, Integer userId);

    // false if not found
    boolean delete(int id, Integer userId);

    // null if not found
    Meal get(int id, Integer userId);

    Collection<Meal> getAll(Integer userId);

    void applyDateFilter(LocalDate from, LocalDate to);

    void clearDateFilters();

    LocalDate getDateFilterFrom();

    LocalDate getDateFilterTo();
}
