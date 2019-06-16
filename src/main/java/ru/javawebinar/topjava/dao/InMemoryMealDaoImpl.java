package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealDaoImpl implements MealDao {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryMealDaoImpl.class);

    private final AtomicInteger counter = new AtomicInteger(0);

    private static final List<Meal> meals = new CopyOnWriteArrayList<>();


    public InMemoryMealDaoImpl() {
        initWithHardcodedMeals();
    }


    private void initWithHardcodedMeals() {
        LOG.debug("initialising in-memory source:");
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
        LOG.debug("initialising completed.");
    }

    @Override
    public void add(Meal meal) {
        LOG.debug("trying to add a meal");
        meal.setId(counter.getAndIncrement());
        LOG.debug("adding " + meal);
        meals.add(meal);
        LOG.debug(meal + " has been successfully added");
    }

    @Override
    public void update(Meal meal) {
        LOG.debug("updating " + meal);
        meals.remove(meal.getId());
        meals.add(meal.getId(), meal);
        LOG.debug(meal + " has been successfully updated");
    }

    @Override
    public void remove(int mealId) {
        LOG.debug("removing meal with id = " + mealId);
        meals.remove(mealId);
        counter.set(0);
        for (Meal meal : meals) {
            meal.setId(counter.getAndIncrement());
        }
        LOG.debug("meal with id = " + mealId + " removed");

    }

    @Override
    public Meal getById(int mealId) {
        return meals.get(mealId);
    }

    @Override
    public List<Meal> getAll() {
        return meals;
    }
}
