package ru.javawebinar.topjava.util;

import org.postgresql.util.PSQLException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

public class ExceptionUtil {

    /**
     * Collects all the rejected fields and corresponding validation messages into single {@code String}
     * from the given {@code Throwable}. Can process only {@code BindException},
     * {@code MethodArgumentNotValidException} or their descendants.
     * @param e exception to look into
     * @return {@code String} containing validation info without details
     * @throws IllegalArgumentException if the {@code e} is not the instance of {@code BindException} or
     * {@code MethodArgumentNotValidException} class
     */
    public static String collectFieldErrors(Throwable e) throws IllegalArgumentException {
        BindingResult bindingResult;
        if (e instanceof BindException) {
            bindingResult = ((BindException) e).getBindingResult();
        } else if (e instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
        } else throw new IllegalArgumentException("Only BindException or MethodArgumentNotValidException exception type allowed");
        return bindingResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("</br>"));
    }

    private final static String USER_EMAIL_DUPLICATION_MESSAGE = "User with this email already exists";
    private final static String MEAL_DATETIME_DUPLICATION_MESSAGE = "Meal with this date and time already exists";
    private final static String UNDEFINED_VIOLATION_MESSAGE = "Undefined field constraint violation";

    public static String collectConstraintViolations(Throwable e) {
        if (e instanceof PSQLException) {
            String rawMessage = e.getMessage();
            if (rawMessage.contains("users_unique_email_idx")) {
                 return USER_EMAIL_DUPLICATION_MESSAGE;
            } else if (rawMessage.contains("meals_unique_user_datetime_idx")) {
                return MEAL_DATETIME_DUPLICATION_MESSAGE;
            }
        } else if (e instanceof ConstraintViolationException){
            return ((ConstraintViolationException) e).getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("</br>"));
        }
        return UNDEFINED_VIOLATION_MESSAGE;
    }
}
