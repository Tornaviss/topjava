package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.*;
import static ru.javawebinar.topjava.util.MealsUtil.*;
@Controller
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    private MealService service;
    @Autowired
    public JspMealController(MealService service) {
        this.service = service;
    }

    @GetMapping("/meals")
    public String meals(Model model) {
        int userId = authUserId();
        log.info("meals - getAll for user {}", userId);
        model.addAttribute("meals",
                getWithExcess(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping("/meals/create")
    public String createForm(Model model) {
        log.info("meals - opening createForm for user {}", authUserId());
        model.addAttribute("meal",
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }
    @GetMapping("/meals/update/{id}")
    public String updateForm(Model model, @PathVariable int id) {
        int userId = authUserId();
        log.info("meals - opening updateForm of meal {} for user {}", id, userId);
        model.addAttribute("meal", service.get(id, userId));
        return "mealForm";
    }

    @PostMapping("/meals/save")
    public ModelAndView save(@ModelAttribute Meal meal) {
        int userId = authUserId();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/meals");
        if (meal.isNew()) {
            try {
                log.info("meals - save {} for user {}", meal, userId);
                service.create(meal, userId);
            } catch (DataIntegrityViolationException e) {
                meal.setId(null);
                log.debug("meals - creation of {} for user {} caused an exception." + System.lineSeparator()
                        + "Root exception message: {}", meal, userId, e.getRootCause().getMessage());
                modelAndView.addObject("exists", "true");
                modelAndView.addObject("meal", meal);
                modelAndView.setViewName("mealForm");
            }
        } else {
            try {
                log.info("meals - update of {} for user {}", meal, userId);
                service.update(meal, userId);
            } catch (DataIntegrityViolationException e) {
                log.debug("meals - update of {} for user {} caused an exception." + System.lineSeparator()
                        + "Root exception message: {}", meal, userId, e.getRootCause().getMessage());
                modelAndView.addObject("exists", "true");
                modelAndView.addObject("meal", meal);
                modelAndView.setViewName("mealForm");
            }
        }
        return modelAndView;
    }

    @GetMapping("/meals/delete/{id}")
    public String delete(@PathVariable int id) {
        int userId = authUserId();
        log.info("meals - delete meal by id {} for user {}", id, userId);
        service.delete(id, userId);
        return "redirect:/meals";
    }

    @PostMapping("/meals/filter")
    public String filter(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
                         Model model) {
        List<Meal> meals = service.getBetweenDates(startDate, endDate, authUserId());
        model.addAttribute("meals", MealsUtil.getFilteredWithExcess(meals, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        return "meals";
    }
}
