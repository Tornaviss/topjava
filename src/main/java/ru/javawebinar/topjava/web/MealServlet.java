package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AbstractUserController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private static final ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");

    private MealRestController mealController;
    private AbstractUserController adminController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        mealController = appCtx.getBean(MealRestController.class);
        adminController = appCtx.getBean(AdminRestController.class);
    }

    @Override
    public void destroy() {
        super.destroy();
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("POST method in meals");
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        switch(action) {
            case "filter":
                mealController.applyFilters(request);
                break;
            case "save":
                mealController.save(request);
                break;
            case "authUser":
                log.info("authUser called in meals");
                SecurityUtil.setAuthUser(adminController.get(Integer.parseInt(request.getParameter("authUserId"))));
        }

        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                mealController.delete(request, response);
                break;
            case "create":
            case "update":
                mealController.forwardMealForm(request, response, action);
                break;
            case "clearFilters":
                mealController.clearAllFilters();
            case "all":
            default:
                mealController.getAll(request, response);
                break;
        }

    }
}
