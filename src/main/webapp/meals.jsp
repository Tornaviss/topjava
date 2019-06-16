<%--
  Created by IntelliJ IDEA.
  User: Serzh
  Date: 14-Jun-19
  Time: 1:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h2>Meals:</h2>
<table>
    <th>Description</th>
    <th>Date</th>
    <th>Calories</th>
    <c:forEach var="meal" items='<%= request.getAttribute("meals")%>'>
    <tr style="background-color:${meal.excess ? 'lightcoral' : 'lightgreen'}">
        <td>${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}</td>
        <td>${meal.description}</td>
        <td>${meal.calories}</td>
        <td><a href="meals?action=edit&&id=${meal.id}">edit</a></td>
        <td><a href="meals?action=delete&&id=${meal.id}">delete</a></td>
    </tr>
    </c:forEach>
</table>
<a href="meals?action=add">Add new meal</a>
</body>
</html>
