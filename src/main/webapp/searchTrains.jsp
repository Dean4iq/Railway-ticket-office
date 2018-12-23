<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.search']}</title>

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

        <h1>${localeValues['head.search']}</h1>

        <form method="post">
            <table align="center">
                <caption><h3>${localeValues['text.train.searchByNumber']}</h3></caption>
                <tr>
                    <td>${localeValues['text.train.number']}:</td>
                    <td><input type="text" required oninvalid="this.setCustomValidity('${localeValues['hint.required']}')" placeholder="${localeValues['text.train.number']}" name="trainNumber"/></td>
                </tr>
            </table>
            <br>
            <p align="center"><input type="submit" value="${localeValues['btn.searchSubmit']}" name="trainNumberSubmit"/></p>
        </form>

        <p style="border-bottom:1px solid;"></p>

        <form method="post">
            <table align="center">
                <caption><h3>${localeValues['text.train.searchByRoute']}</h3></caption>
                <tr>
                    <th>${localeValues['text.station.from']}</th>
                    <th></th>
                    <th>${localeValues['text.station.to']}</th>
                </tr>
                <tr>
                    <td><input type="text" placeholder="${localeValues['text.station.from']}" required name="departureStation" oninvalid="this.setCustomValidity('${localeValues['hint.required']}')"/></td>
                    <td style="padding-left: 30px; padding-right: 30px;">
                        ←<input type="submit" value="Switch" name="SwitchDirections"/>→
                    </td>
                    <td><input type="text" placeholder="${localeValues['text.station.to']}" required name="destinationStation" oninvalid="this.setCustomValidity('${localeValues['hint.required']}')"/></td>
                </tr>
            </table>
            <br>
            <p align="center"><input type="submit" value="${localeValues['btn.searchSubmit']}" name="trainDestinationSubmit"/></p>
        </form>

        <p style="border-bottom:1px solid;"></p>

        <form method="post">
            <h3 align="center">${localeValues['text.train.searchAll']}</h3>
            <p align="center">
                <input type="submit" value="${localeValues['btn.searchSubmit']}" name="allTrainSubmit"/>
            </p>
        </form>

        <c:if test="${not empty trainList}">
            <h3 align="center">Train LIST</h3>
            <table align="center" border="1">
                <tr>
                    <th>${localeValues['table.column.trainNumber']}</th>
                    <th>${localeValues['table.column.route']}</th>
                    <th>${localeValues['table.column.date']}</th>
                    <th>${localeValues['table.column.time']}</th>
                </tr>
                <c:forEach items="${trainList}" var="train">
                    <tr>
                        <td>${train.id}</td>
                        <td>${train.departureRoute.station.name} - ${train.arrivalRoute.station.name}</td>
                        <td>
                            ${localeValues['table.column.departure']}:${train.departureRoute.formattedDepartureDate}
                            <br>
                            ${localeValues['table.column.arrival']}:${train.arrivalRoute.formattedArrivalDate}
                        </td>
                        <td>
                            ${localeValues['table.column.departure']}: ${train.departureRoute.formattedDepartureTime}
                            <br>
                            ${localeValues['table.column.arrival']}: ${train.arrivalRoute.formattedArrivalTime}
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </body>
</html>