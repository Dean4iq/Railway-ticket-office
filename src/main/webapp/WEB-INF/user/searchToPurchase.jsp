<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.search']}</title>
        <style>
            <jsp:directive.include file="/styles/css/bootstrap.min.css"/>
        </style>

        <script>
            <jsp:directive.include file="/styles/js/bootstrap.min.js"/>
        </script>
    </head>

    <body>
        <nav class="navbar navbar-expand-lg navbar-dark" style="background-color:#0c5e00;margin:10px">
            <a class="navbar-brand" href=""></a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item active">
                        <a class="nav-link" href="${pageContext.request.contextPath}">
                            ${localeValues['btn.main']}
                        </a>
                    </li>
                    <li class="nav-item active">
                        <a class="nav-link" href="search">
                            ${localeValues['btn.search']}
                        </a>
                    </li>
                    <c:forEach items="${userbar}" var="keyValue">
                        <li class="nav-item active">
                            <a class="nav-link" href="${keyValue.value}">
                                <c:out value="${localeValues[keyValue.key]}"/>
                            </a>
                        </li>
                    </c:forEach>
                </ul>
                <ul class="navbar-nav">
                    <form name="langForm" method="post" align="right">
                        <select name="langSelect" onchange="document.langForm.submit();">
                            <option ${langVariable=="en"?"selected":""} value="en">English</option>
                            <option ${langVariable=="uk"?"selected":""} value="uk">Українська</option>
                        </select>
                    </form>
                </ul>
            </div>
        </nav>

        <h1>${localeValues['head.search']}</h1>
        <form method="post">
            <table align="center">
                <caption><h3>${localeValues['text.train.searchByRoute']}</h3></caption>
                <tr>
                    <th>${localeValues['text.station.from']}</th>
                    <th>${localeValues['text.station.to']}</th>
                </tr>
                <tr>
                    <td><input type="text" list="stations" placeholder="${localeValues['text.station.from']}" required name="departureStation"/></td>
                    <td><input type="text" list="stations" placeholder="${localeValues['text.station.to']}" required name="destinationStation"/></td>
                    <datalist id="stations">
                        <c:if test="${langVariable=='en'}">
                            <c:forEach items="${stationList}" var="station">
                                <option>${station.nameEN}</option>
                            </c:forEach>
                        </c:if>
                        <c:if test="${langVariable=='uk'}">
                            <c:forEach items="${stationList}" var="station">
                                <option>${station.nameUA}</option>
                            </c:forEach>
                        </c:if>
                    </datalist>
                </tr>
            </table>
            <br>
            <div align="center">
                <input type="date" name="tripStartDate" min="${minCalendarDate}" required max="${maxCalendarDate}">
            </div>
            <br>
            <p align="center"><input type="submit" value="${localeValues['btn.searchSubmit']}" name="ticketSearchSubmit"/></p>
        </form>
        <p style="border-bottom:1px solid;"></p>

        <c:if test="${not empty trainList}">
            <h3 align="center">${localeValues['head.trainList']}</h3>
            <table border="1">
                <tr>
                    <th>
                        ${localeValues['table.column.trainNumber']}
                        <form method="post">
                            <input type="submit" name="sortTrainNumAsc" value="↑"/>
                            <input type="submit" name="sortTrainNumDesc" value="↓"/>
                        </form>
                    </th>
                    <th>${localeValues['table.column.route']}</th>
                    <th>${localeValues['table.column.date']}</th>
                    <th>${localeValues['table.column.time']}</th>
                    <th>${localeValues['table.column.seats']}</th>
                </tr>
                <form method="post">
                    <c:forEach var="train" items="${trainList}">
                        <tr>
                            <td>${train.id}</td>
                            <td>
                                <c:if test="${langVariable=='en'}">
                                    ${train.departureRoute.station.nameEN} - ${train.arrivalRoute.station.nameEN}
                                </c:if>
                                <c:if test="${langVariable=='uk'}">
                                    ${train.departureRoute.station.nameUA} - ${train.arrivalRoute.station.nameUA}
                                </c:if>
                            </td>
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
                            <td><input type="submit" value="${localeValues['btn.lookSeat']}" name="wagonInTrain${train.id}"/></td>
                        </tr>
                    </c:forEach>
                </form>
            </table>
        </c:if>
    </body>
</html>