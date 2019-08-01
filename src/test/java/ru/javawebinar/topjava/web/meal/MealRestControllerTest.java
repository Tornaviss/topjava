package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.web.SecurityUtil.*;

public class MealRestControllerTest extends AbstractControllerTest {

    @Autowired
    private MealService service;

    private static final String REST_URL = "/rest/meals" + "/";

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL1));
    }

    @Test
    void testCreate() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Meal expected = new Meal(now, "newMeal", 500);
        ResultActions actions = mockMvc.perform(post(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());
        Meal returned = TestUtil.readFromJson(actions, Meal.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
    }

    @Test
    void testUpdate() throws Exception {
        Meal expected = new Meal(MEAL1_ID, MEAL1.getDateTime(), "changed", MEAL1.getCalories());
        mockMvc.perform(post(REST_URL + MEAL1_ID).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isNoContent());
        MealTestData.assertMatch(service.get(MEAL1_ID, authUserId()), expected);
    }

    @Test
    void testDelete() throws Exception {
        List<Meal> expected = List.of(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
        mockMvc.perform(delete(REST_URL + MEAL1_ID))
                .andExpect(status().isNoContent());
        assertMatch(service.getAll(authUserId()), expected);

    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MealsUtil.getWithExcess(MEALS, authUserCaloriesPerDay())));
    }

    @Test
    void testGetBetween() throws Exception {
        DateTimeFormatter ft = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startDateTime = MEAL1.getDateTime();
        LocalDateTime endDateTime = MEAL3.getDateTime();
        List<Meal> expected = List.of(MEAL3, MEAL2, MEAL1);
        mockMvc.perform(post(REST_URL + "filter")
                .param("startDateTime", startDateTime.format(ft))
                .param("endDateTime", endDateTime.format(ft)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MealsUtil.getFilteredWithExcess(
                        expected,
                        authUserCaloriesPerDay(), startDateTime.toLocalTime(), endDateTime.toLocalTime())));
    }
}
