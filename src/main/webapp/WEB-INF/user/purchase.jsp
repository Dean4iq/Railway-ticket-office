<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.purchase']}</title>
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
