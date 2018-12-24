<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.purchase']}</title>
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

        <h1>${localeValues['head.purchase']}</h1>
        <table border="1">
            <caption>Ticket info</caption>
            <tr align="center">
                <th>${localeValues['table.column.trainNumber']}</th>
                <th>${localeValues['table.column.route']}</th>
                <th>${localeValues['table.column.date']}</th>
                <th>${localeValues['table.column.time']}</th>
            </tr>
            <tr align="center">
                <td>${purchasedTicket.train.id}</td>
                <td>
                    ${purchasedTicket.train.departureRoute.station.nameUA}
                    <br>
                    ${purchasedTicket.train.arrivalRoute.station.nameUA}
                </td>
                <td>
                    ${purchasedTicket.train.departureRoute.formattedDepartureDate}
                    <br>
                    ${purchasedTicket.train.arrivalRoute.formattedArrivalDate}
                </td>
                <td>
                    ${purchasedTicket.train.departureRoute.formattedDepartureTime}
                    <br>
                    ${purchasedTicket.train.arrivalRoute.formattedArrivalTime}
                </td>
            </tr>
        </table>
        <br>
        <table border="1">
            <tr align="center">
                <th>Seat #</th>
                <th>Wagon #</th>
                <th>Departure station</th>
                <th>Arrival station</th>
                <th>Date</th>
            </tr>
            <tr align="center">
                <td>${purchasedTicket.seatId}</td>
                <td>${purchasedTicket.wagon.id}</td>
                <td>${purchasedTicket.departureStation.nameUA}</td>
                <td>${purchasedTicket.arrivalStation.nameUA}</td>
                <td>${purchasedTicket.travelDate}</td>
            </tr>
        </table>
        ${localeValues['text.cost']}: &nbsp; ${purchasedTicket.cost} &nbsp; ${localeValues['text.current.UAH']}

        <form method="POST">
            <input type="submit" name="payForTicket" value="${localeValues['btn.acceptPurchase']}"/>
            <input type="submit" name="declinePayment" value="${localeValues['btn.declinePurchase']}"/>
        </post>
    </body>
</html>
