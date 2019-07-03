package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Iterator;

public class MealTestData {

    public static final Meal meal1 = new Meal(100002, LocalDateTime.of(1999, 1, 8, 10, 0), "Завтрак", 1000);
    public static final Meal meal2 = new Meal(100003,LocalDateTime.of(1999, 1, 8, 14, 0), "Обед", 1000);

    public static void assertMatch(Meal actual, Meal expected) throws AssertionError {
        equalsByPrimaryKey(actual, expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) throws AssertionError {
        Iterator<Meal> iterator = actual.iterator();
        for (Meal meal : expected) {
            equalsByPrimaryKey(meal, iterator.next());
        }
    }

    private static void equalsByPrimaryKey(AbstractBaseEntity first, AbstractBaseEntity second) throws AssertionError {
        if (!first.getId().equals(second.getId())) throw new AssertionError();
    }
}
