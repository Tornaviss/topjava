package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.InMemoryMealDaoImpl;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MealServlet extends HttpServlet {
    private MealDao dao = new InMemoryMealDaoImpl();
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);
    private static final String ADD_OR_EDIT = "meal.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("redirect to meals");
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action != null) {
            if (action.equalsIgnoreCase("delete")) {
                int id = Integer.parseInt(req.getParameter("id"));
                LOG.debug("action invoked -- " + action + ", target id -- " + id);
                dao.remove(id);
                resp.sendRedirect("meals");
            } else if (action.equalsIgnoreCase("edit")) {
                int id = Integer.parseInt(req.getParameter("id"));
                LOG.debug("action invoked -- " + action + ", target id -- " + id);
                req.setAttribute("meal" , dao.getById(id));
                req.getRequestDispatcher(ADD_OR_EDIT).forward(req, resp);
            } else if (action.equalsIgnoreCase("add")){
                resp.sendRedirect(ADD_OR_EDIT);
            }
            return;
        }
        List<Meal> meals = dao.getAll();
        req.setAttribute("meals", MealsUtil.getFilteredWithExcess(meals, LocalTime.MIN, LocalTime.MAX, 2000));
        req.getRequestDispatcher("meals.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("invoking POST method in meals");
        req.setCharacterEncoding("UTF-8");
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"), dtf);
        Meal meal = new Meal(dateTime, description, calories);
        String id = req.getParameter("id");
        if (id == null || id.isEmpty()) {
            dao.add(meal);
        } else {
            meal.setId(Integer.parseInt(id));
            dao.update(meal);
        }
        resp.sendRedirect("meals");
    }
}
