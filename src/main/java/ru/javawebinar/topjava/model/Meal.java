package ru.javawebinar.topjava.model;

import org.hibernate.validator.constraints.Range;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@NamedQueries({
        @NamedQuery(name = Meal.GET, query = "select m from Meal m left join fetch m.user " +
                "left join fetch m.user.roles where m.id = :id and m.user.id = :userId"),
        @NamedQuery(name = Meal.UPDATE, query = "update Meal m set m.dateTime = :dateTime, " +
                "m.description = :description, m.calories = :calories where m.id = :id and m.user.id = :userId"),
        @NamedQuery(name = Meal.DELETE, query = "delete from Meal m where m.id = :id and m.user.id = :userId"),
        @NamedQuery(name = Meal.ALL, query = "select m from Meal m left join fetch m.user left join fetch m.user.roles " +
                "where m.user.id = :userId order by m.dateTime desc"),
        @NamedQuery(name = Meal.ALL_BETWEEN, query = "select m from Meal m left join fetch m.user left join fetch m.user.roles " +
                "where m.user.id = :userId and m.dateTime between :start and :end order by m.dateTime desc")
})

@Entity
@Table(name = "meals", indexes = @Index(name = "meals_unique_user_datetime_idx", columnList = "user_id, date_time", unique = true))
public class Meal extends AbstractBaseEntity {
    public static final String GET = "Meal.get";
    public static final String UPDATE = "Meal.update";
    public static final String DELETE = "Meal.delete";
    public static final String ALL = "Meal.getAll";
    public static final String ALL_BETWEEN = "Meal.getAllBetween";

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(max = 100)
    private String description;

    @Column(name = "calories", nullable = false)
    @Range(min = 1)
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Nullable
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
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

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
