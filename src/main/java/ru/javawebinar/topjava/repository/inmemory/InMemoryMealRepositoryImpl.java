package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    private LocalDate from;
    private LocalDate to;

    {
        MealsUtil.MEALS.forEach(x -> this.save(x, 1));
        MealsUtil.MEALS.forEach(x -> this.save(new Meal(x.getDateTime(), x.getDescription(), x.getCalories()), 2));
        MealsUtil.MEALS.forEach(x -> this.save(new Meal(x.getDateTime(), x.getDescription(), x.getCalories()), 3));

    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        log.info("save {}", meal);
        meal.setUserId(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, Integer userId) {
        log.info("delete {}", id);
        Meal target = repository.get(id);
        if (target != null) {
            if (target.getUserId().equals(userId)) {
                return repository.remove(id) != null;
            }
        }
        return false;
    }

    @Override
    public Meal get(int id, Integer userId) {
        Meal target = repository.get(id);
        if (target != null && target.getUserId().equals(userId)) return repository.get(id);
        return null;
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        log.info("getAll");
        return repository.values().stream()
                .filter(x -> userId.equals(x.getUserId()) && (from == null || x.getDate().isEqual(from) || x.getDate().isAfter(from))
                        && (to == null || x.getDate().isBefore(to) || x.getDate().isEqual(to)))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toCollection(ArrayList::new));
    }
    public void applyDateFilter(LocalDate from, LocalDate to) {
        log.info("applying filter from {} to {}", from, to);
        if (from != null) this.from = from;
        if (to != null) this.to = to;
    }

    @Override
    public void clearDateFilters() {
        from = null;
        to = null;
    }

    @Override
    public LocalDate getDateFilterFrom() {
        return from;
    }

    @Override
    public LocalDate getDateFilterTo() {
        return to;
    }
}

