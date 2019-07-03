package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }
    @Test
    public void create() {
        Meal expected = new Meal(LocalDateTime.now(), "newMeal", 1001);
        Meal created = service.create(expected, USER_ID);
        expected.setId(created.getId());
        assertMatch(service.get(created.getId(), USER_ID), expected);
    }

    @Test(expected = DataAccessException.class)
    public void createDuplicateDateTime() {
        service.create(new Meal(meal1.getDateTime(), "Duplicate", 200), USER_ID);
    }

    @Test
    public void update() {
        Meal expected = new Meal(meal1);
        expected.setDescription("Changed description");
        service.update(expected, USER_ID);
        assertMatch(service.get(expected.getId(), USER_ID), expected);
    }
    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        service.update(meal1, USER_ID + 1);
    }

    @Test
    public void delete() {
        service.delete(meal1.getId(), USER_ID);
        assertMatch(service.getAll(USER_ID), meal2);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(100004, USER_ID);
    }

    @Test
    public void get() {
        assertMatch(service.get(meal1.getId(), USER_ID), meal1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(100004, USER_ID);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), meal2, meal1);
    }

    @Test
    public void getBetweenDates() {
        LocalDate start = LocalDate.of(1999, 1, 8);
        LocalDate end = LocalDate.of(1999, 1, 8);
        assertMatch(service.getBetweenDates(start, end, USER_ID), meal1);
    }

    @Test
    public void getBetweenDateTimes() {
        LocalDateTime start = LocalDateTime.of(1999, 1, 8, 14, 0);
        LocalDateTime end = LocalDateTime.of(1999, 1, 9, 14, 0);
        assertMatch(service.getBetweenDateTimes(start, end, USER_ID), meal2);
    }
}