package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.data.util.Pair;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;

import java.util.List;

import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.MealTestData.*;
@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Test
    public void getWithMealsFetched() {
        List<Meal> actual = service.getWithMealsFetched(ADMIN_ID).getMeals();
        List<Meal> expected = List.of(ADMIN_MEAL2, ADMIN_MEAL1);
        assertMatch(actual, expected);
    }

    @Test
    public void getUserWithMeal() {
        Pair<User, Meal> actual = service.getUserWithMeal(ADMIN_ID, ADMIN_MEAL_ID);
        Pair<User, Meal> expected = Pair.of(ADMIN, ADMIN_MEAL1);
        assertMatch(actual.getFirst(), expected.getFirst());
        assertMatch(actual.getSecond(), expected.getSecond());
    }
}
