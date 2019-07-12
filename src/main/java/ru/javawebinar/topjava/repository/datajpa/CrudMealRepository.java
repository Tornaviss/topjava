package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    @Modifying
    //Only optimal on this layer (no access to CrudUserRepo inside MealRepository to make a reference to concrete user).
    @Query(value = "INSERT INTO meals (date_time, description, calories, user_id) VALUES(?1,?2,?3,?4)",
            nativeQuery = true)
    int saveByNativeQuery(LocalDateTime dateTime, String description, int calories, int userId);

    @Transactional
    @Modifying
    //Only optimal on this layer (no access to a user repo inside MealRepository)
    @Query(value = "UPDATE Meal m SET m.dateTime = :dateTime, m.description = :description, " +
            "m.calories= :calories where m.id= :id and m.user.id= :userId")
    int update(@Param("dateTime") LocalDateTime dateTime,@Param("description") String description,
               @Param("calories") int calories,@Param("id") int id,@Param("userId") int userId);

    @Transactional
    int deleteByIdAndUserId(@Param("id") int id, @Param("userId") int userId);

    Meal getByDateTimeAndUserId(LocalDateTime dateTime, int userId);

    List<Meal> getByUserIdOrderByDateTimeDesc(@Param("userId") int userId);

    Meal getByIdAndUserId(@Param("id") int id, @Param("userId") int userId);

    List<Meal> getByUserIdAndDateTimeBetweenOrderByDateTimeDesc
            (@Param("userId") int userId, @Param("from") LocalDateTime from,
             @Param("to") LocalDateTime to);


}
