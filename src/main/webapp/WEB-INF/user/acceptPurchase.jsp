<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>Train list</title>
        <style>
            <jsp:directive.include file="/styles/css/head_style.css" />
        </style>
    </head>
    <body>
        <form name="langForm" method="post">
            <select name="langSelect" onchange="document.langForm.submit();">
                <option value="en" ${langVariable=="en"?"selected":""}>English</option>
                <option value="uk" ${langVariable=="uk"?"selected":""}>Українська</option>
            </select>
        </form>
        <div class="nav">
            <ul>
                <li><a href="${pageContext.request.contextPath}"><c:out value="${localeValues['btn.main']}"/></a></li>
                <li><a href="search">${localeValues['btn.search']}</a></li>
                <c:forEach items="${userbar}" var="keyValue">
                    <li><a href="${keyValue.value}">${keyValue.key}</a></li>
                </c:forEach>
            </ul>
        </div>

        <h1>${localeValues['head.purchase']}</h1>
        <table border="1">
            <caption>${localeValues['text.ticket']}</caption>
            <tr>
                <th>${localeValues['table.column.trainNumber']}</th>
                <th>${localeValues['table.column.route']}</th>
                <th>${localeValues['table.column.departure']}</th>
                <th>${localeValues['table.column.arrival']}</th>
            </tr>
            <c:forEach items="${trainList}" var="train">
              <tr>
                <td><c:out value="${train.id}"/></td>
                <td><c:out value="${train.departureRoute.station.name} - ${train.arrivalRoute.station.name}"/></td>
                <td><c:out value="${train.departureRoute.formattedDepartureTime}"/></td>
                <td><c:out value="${train.arrivalRoute.formattedArrivalTime}"/></td>
              </tr>
            </c:forEach>
        </table>

        <form method="post">
            <input type="submit" name="accept" value="${localeValues['btn.acceptPurchase']}"/>
            <input type="submit" name="decline" value="${localeValues['btn.declinePurchase']}"/>
        </form>
    </body>
</html>