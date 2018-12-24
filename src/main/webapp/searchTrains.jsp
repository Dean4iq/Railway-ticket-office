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
        <nav class="navbar navbar-expand-lg navbar-dark" style="background-color:#0c5e00;">
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
                                ${keyValue.key}
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
            <h3 align="center">${localeValues['text.train.searchByNumber']}</h3>
            <table align="center">
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
            <h3 align="center">${localeValues['text.train.searchByRoute']}</h3>
            <table align="center">
                <tr>
                    <th>${localeValues['text.station.from']}</th>
                    <th>${localeValues['text.station.to']}</th>
                </tr>
                <tr>
                    <td><input type="text" placeholder="${localeValues['text.station.from']}" required name="departureStation" oninvalid="this.setCustomValidity('${localeValues['hint.required']}')"/></td>
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
            <h3 align="center">${localeValues['head.trainList']}</h3>
            <table align="center" border="1">
                <tr>
                    <th>${localeValues['table.column.trainNumber']}</th>
                    <th>${localeValues['table.column.route']}</th>
                    <th>${localeValues['table.column.departure']}</th>
                    <th>${localeValues['table.column.arrival']}</th>
                </tr>
                <c:forEach items="${trainList}" var="train">
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
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </body>
</html>