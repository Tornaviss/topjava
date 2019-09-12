<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<nav class="navbar navbar-dark bg-dark py-0">
    <div class="container">
        <a href="meals" class="navbar-brand"><img src="resources/images/icon-meal.png"> <spring:message code="app.title"/></a>
        <sec:authorize access="isAuthenticated()">
            <form:form class="form-inline my-2" action="logout" method="post">
                <sec:authorize access="hasRole('ROLE_ADMIN')">
                    <a class="btn btn-info mr-1" href="users"><spring:message code="user.title"/></a>
                </sec:authorize>
                <a class="btn btn-info mr-1" href="profile">${userTo.name} <spring:message code="app.profile"/></a>
                <button class="btn btn-primary my-1" type="submit">
                    <span class="fa fa-sign-out"></span>
                </button>
                <div class="dropdown nav-item" style="padding-left: 2%;">
                    <button class="btn btn-primary dropdown-toggle" id="menu1" type="button" data-toggle="dropdown">${pageContext.response.locale}
                    </button>
                    <ul class="dropdown-menu" role="menu" aria-labelledby="menu1">
                        <li role="presentation">
                            <a role="menuitem" tabindex="-1" class="dropdown-item"
                               href="${requestScope['javax.servlet.forward.request_uri']}?locale=en">English</a>
                        </li>
                        <li role="presentation">
                            <a role="menuitem" tabindex="-1" class="dropdown-item"
                               href="${requestScope['javax.servlet.forward.request_uri']}?locale=ru">Русский</a>
                        </li>
                    </ul>
                </div>
            </form:form>
        </sec:authorize>
        <sec:authorize access="isAnonymous()">
            <form:form class="form-inline my-2" id="login_form" action="spring_security_check" method="post">
                <input class="form-control mr-1" type="text" placeholder="Email" name="username">
                <input class="form-control mr-1" type="password" placeholder="Password" name="password">
                <button class="btn btn-success" type="submit">
                    <span class="fa fa-sign-in"></span>
                </button>
                <div class="dropdown nav-item" style="padding-left: 1%;">
                    <button class="btn btn-primary dropdown-toggle" id="menu1" type="button" data-toggle="dropdown">${pageContext.response.locale}
                    </button>
                    <ul class="dropdown-menu" role="menu" aria-labelledby="menu1">
                        <li role="presentation">
                            <a role="menuitem" tabindex="-1" class="dropdown-item"
                               href="${requestScope['javax.servlet.forward.request_uri']}?locale=en">English</a>
                        </li>
                        <li role="presentation">
                            <a role="menuitem" tabindex="-1" class="dropdown-item"
                               href="${requestScope['javax.servlet.forward.request_uri']}?locale=ru">Русский</a>
                        </li>
                    </ul>
                </div>
            </form:form>
        </sec:authorize>
    </div>
</nav>
