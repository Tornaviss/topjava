package ru.javawebinar.topjava.web.constraint;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    UserService service;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        if (value == null) return false;
        AuthorizedUser authUser = SecurityUtil.safeGet();
        if (authUser != null && authUser.toString().contains(value)) return true;
        return !service.isEmailPresented(value);
    }
}
