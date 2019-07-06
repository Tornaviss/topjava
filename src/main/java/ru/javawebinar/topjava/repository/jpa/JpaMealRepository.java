package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setUser(em.getReference(User.class, userId));
            em.persist(meal);
            return meal;
        } else {
            Query query = em.createNamedQuery(Meal.UPDATE).setParameter("dateTime", meal.getDateTime())
                    .setParameter("description", meal.getDescription()).setParameter("calories", meal.getCalories())
                    .setParameter("id", meal.getId()).setParameter("userId", userId);
            return query.executeUpdate() != 0 ? meal : null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        Query query = em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id).setParameter("userId", userId);
        return query.executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        TypedQuery<Meal> query = em.createNamedQuery(Meal.GET, Meal.class)
                .setParameter("id", id).setParameter("userId", userId);
        Meal result = null;
        try {
            result = query.getSingleResult();
        } catch(NoResultException e) {

        }
        return result;
    }

    @Override
    public List<Meal> getAll(int userId) {
        TypedQuery<Meal> query = em.createNamedQuery(Meal.ALL, Meal.class).setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        TypedQuery<Meal> query = em.createNamedQuery(Meal.ALL_BETWEEN, Meal.class)
                .setParameter("start", startDate).setParameter("end", endDate)
                .setParameter("userId", userId);
        return query.getResultList();
    }
}