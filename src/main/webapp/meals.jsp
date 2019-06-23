<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
        .Row
        {
            display: table;
            min-width: 500px;
            width: 100%;
            table-layout: fixed;
            border-spacing: 10px;
            background-color:#FAFAFA;
        }
        .Column
        {
            display: table-cell;
        }
        .Column_bottom
        {
            padding-top:2%;
        }
    </style>
    <script>
        function clearFilter() {
            document.location.assign("meals?action=clearFilters");
        }
    </script>
</head>
<body>
<header>
    <h3><a href="index.html">Home</a></h3>
</header>
<hr/>
<nav style="min-width:500px;max-width:80%;">
    <h2>Meals</h2>
    <div style="position:center;">
        <form name="filter" id="filter" action="meals?action=filter" method="post">
            <section class="Row">
                <section class="Column">
                    <div>
                        <label for="dateFrom">От даты</label>
                        <br />
                        <input type="date" name="dateFrom" id="dateFrom" value="${dateFrom}" />
                    </div>
                    <div class="Column_bottom">
                        <label for="dateTo">До даты</label>
                        <br />
                        <input type="date" name="dateTo" id="dateTo" value="${dateTo}"/>
                    </div>
                </section>
                <section class="Column">
                    <div>
                        <label for="timeFrom">От времени</label>
                        <br />
                        <input type="time" name="timeFrom" id="timeFrom" value="${timeFrom}" />
                    </div>
                    <div class="Column_bottom">
                        <label for="timeTo">До времени</label>
                        <br />
                        <input type="time" name="timeTo" id="timeTo" value="${timeTo}">
                    </div>
                </section>
            </section>
        </form>
    </div>
    <div align="right">
        <button type="submit" onclick="clearFilter()">Отменить</button>
        <button type="submit" form="filter">Фильтровать</button>
    </div>

</nav>
<section>
    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>