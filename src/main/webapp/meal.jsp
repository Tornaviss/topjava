<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Serzh
  Date: 15-Jun-19
  Time: 12:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="meal" value='<%=request.getAttribute("meal")%>' />
<html>
<head>
    <title>Meal</title>
</head>
<body>
<h2>${empty meal ? 'Add' : 'Update'}</h2>
<form action="meals" method="POST">
    <input type="hidden" name="id" value="${meal.id}"/>
    <label for="description">Description:</label>
    <input id="description" name="description" type="text" value="${!empty meal.description ? meal.description : ''}" required="required" />
    <label for="calories">Calories:</label>
    <input type="number" name="calories" id="calories" min="0" maxlength="10" value="${!empty meal.calories ? meal.calories : 0}" />
    <label for="dateTime">Date and time</label>
    <input type="datetime-local" name="dateTime" id="dateTime" value="${meal.dateTime}" required="required" />
    <button type="submit">Submit</button>
</form>

</body>
</html>
