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

        <h3 align="center">${localeValues['text.trainInfo']}</h3>
        <table class="table" align="center" border="1">
            <thead class="thread-light">
                <tr>
                    <th>${localeValues['text.train.number']}</th>
                    <th>${localeValues['text.station.from']}</th>
                    <th>${localeValues['text.station.to']}</th>
                    <th>${localeValues['table.column.date']}</th>
                    <th>${localeValues['table.column.time']}</th>
                </tr>
            </thead>
            </tbody>
                <tr>
                    <th scope="row">${trainInfo.id}</td>
                    <td>${trainInfo.departureRoute.station.nameUA}</td>
                    <td>${trainInfo.arrivalRoute.station.nameUA}</td>
                    <td>${trainInfo.departureRoute.departureTime}</td>
                    <td>${trainInfo.arrivalRoute.arrivalTime}</td>
                </tr>
            </tbody>
        </table>

        <h3 align="center" style="border-top:1px solid;">${localeValues['table.column.seats']}</h3>
        <form method="post">
            <c:forEach items="${wagonList}" var="wagon">
                <table align="center" border="1">
                    <tr>
                        <th>${localeValues['table.column.wagonId']}</th>
                        <th>${localeValues['table.column.wagonType']}</th>
                    </tr>
                    <tr>
                        <td>${wagon.id}</td>
                        <td>${wagon.type}</td>
                    </tr>
                </table>
                <c:forEach items="${wagon.seatList}" var="seat">
                    <ul style="list-style-type: none;border-bottom:1px solid;">
                        <li>
                            ${localeValues['text.seatNumber']}:${seat.id}
                            ${localeValues['text.cost']}:${wagon.train.cost}${localeValues['text.current.UAH']}
                            <c:if test="${not seat.occupied}">
                                <input type="submit" name="PurchaseSeat${seat.id}" value="${localeValues['btn.purchaseTicket']}"/>
                            </c:if>
                        </li>
                    </ul>
                </c:forEach>
            </c:forEach>
        </form>
    </body>
</html>