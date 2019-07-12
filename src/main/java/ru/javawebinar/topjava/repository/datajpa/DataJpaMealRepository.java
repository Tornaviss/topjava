package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    @Autowired
    private CrudMealRepository crudRepository;

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            return crudRepository.saveByNativeQuery(meal.getDateTime(), meal.getDescription(),
                    meal.getCalories(), userId) == 1 ?
                    crudRepository.getByDateTimeAndUserId(meal.getDateTime(), userId) : null;
        } else {
            return crudRepository.update(meal.getDateTime(), meal.getDescription(), meal.getCalories(), meal.getId(), userId)
                    == 1 ? crudRepository.getByDateTimeAndUserId(meal.getDateTime(), userId) : null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.deleteByIdAndUserId(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.getByIdAndUserId(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.getByUserIdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return crudRepository.getByUserIdAndDateTimeBetweenOrderByDateTimeDesc(userId, startDate, endDate);
    }
}
