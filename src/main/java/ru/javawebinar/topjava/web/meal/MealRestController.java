package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ru.javawebinar.topjava.web.SecurityUtil.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);
    private MealService service;
    private LocalTime from;
    private LocalTime to;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public void save(HttpServletRequest request) {
        String id = request.getParameter("id");
        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        if (meal.isNew()) {
            service.create(meal, authUserId());
        } else {
            service.update(meal, authUserId());
        }
    }

    public void forwardMealForm(HttpServletRequest request, HttpServletResponse response, String action) throws ServletException, IOException {
        final Meal meal = "create".equals(action) ?
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                service.get(getId(request), authUserId());
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
    }

    public void getAll(HttpServletRequest request, ServletResponse response) throws ServletException, IOException {
        log.info("getAll");
        request.setAttribute("meals", isTimeFilterPresented() ?
                        MealsUtil.getWithExcess(service.getAll(authUserId()), authUserCaloriesPerDay()).stream()
                                .filter(x -> DateTimeUtil.isBetween(
                                        x.getDateTime().toLocalTime(), from == null ? LocalTime.MIN : from, to == null ? LocalTime.MAX : to))
                                .collect(Collectors.toCollection(ArrayList::new)) :
                MealsUtil.getWithExcess(service.getAll(authUserId()), SecurityUtil.authUserCaloriesPerDay())
        );
        setFilterAttributes(request);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
    private void setFilterAttributes(HttpServletRequest request) {
        request.setAttribute("timeFrom", from == null ? "" : from);
        request.setAttribute("timeTo", to == null ? "" : to);
        request.setAttribute("dateFrom", service.getDateFilterFrom() == null ? "" : service.getDateFilterFrom());
        request.setAttribute("dateTo", service.getDateFilterTo() == null ? "" : service.getDateFilterTo());
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = getId(request);
        log.info("Delete {}", id);
        service.delete(id, authUserId());
        response.sendRedirect("meals");
    }

    private boolean isTimeFilterPresented() {
        return from != null || to != null;
    }

    public void applyFilters(HttpServletRequest request) {
        String fromDate = request.getParameter("dateFrom");
        String toDate = request.getParameter("dateTo");

        service.applyDateFilter(fromDate == null || fromDate.isEmpty() ? null : LocalDate.parse(fromDate),
                toDate == null || toDate.isEmpty() ? null : LocalDate.parse(toDate));
        applyTimeFilter(request.getParameter("timeFrom"), request.getParameter("timeTo"));
    }

    private void applyTimeFilter(String from, String to) {
        log.info("Applying time filtration, from {} to {}", from, to);
        if (from != null && !from.isEmpty()) this.from = LocalTime.parse(from);
        if (to != null && !to.isEmpty()) this.to = LocalTime.parse(to);
    }

    public void clearAllFilters() {
        service.clearDateFilters();
        from = null;
        to = null;
    }


    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }


}