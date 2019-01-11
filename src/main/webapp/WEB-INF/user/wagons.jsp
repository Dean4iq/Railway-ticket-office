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
        <jsp:include page="/styles/page_parts/navbar.jsp"/>

        <h3 align="center">${localeValues['table.header.travelInfo']}</h3>
        <table class="table" align="center" border="1" style="width: 90%">
            <thead class="thread-light">
                <tr>
                    <th>${localeValues['text.train.number']}</th>
                    <th>${localeValues['text.station.from']}</th>
                    <th>${localeValues['text.station.to']}</th>
                    <th>${localeValues['table.column.departure']}</th>
                    <th>${localeValues['table.column.arrival']}</th>
                </tr>
            </thead>
            </tbody>
                <tr>
                    <th scope="row">${trainInfo.id}</td>
                    <td>
                        <c:if test="${langVariable=='en'}">
                            ${trainInfo.userDepartureRoute.station.nameEN}
                        </c:if>
                        <c:if test="${langVariable=='uk'}">
                            ${trainInfo.userDepartureRoute.station.nameUA}
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${langVariable=='en'}">
                            ${trainInfo.userArrivalRoute.station.nameEN}
                        </c:if>
                        <c:if test="${langVariable=='uk'}">
                            ${trainInfo.userArrivalRoute.station.nameUA}
                        </c:if>
                    </td>
                    <td>
                        ${trainInfo.userDepartureRoute.formattedDepartureDate}
                        ${trainInfo.userDepartureRoute.formattedDepartureTime}
                    </td>
                    <td>
                        ${trainInfo.userArrivalRoute.formattedArrivalDate}
                        ${trainInfo.userArrivalRoute.formattedArrivalTime}
                    </td>
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
                        <td>
                            <c:if test="${langVariable=='en'}">
                                ${wagon.typeEN}
                            </c:if>
                            <c:if test="${langVariable=='uk'}">
                                ${wagon.typeUA}
                            </c:if>
                        </td>
                    </tr>
                </table>
                <c:forEach items="${wagon.seatList}" var="seat" varStatus="place">
                    <ul style="list-style-type: none;border-bottom:1px solid;">
                        <li>
                            ${localeValues['text.seatNumber']}:
                            ${place.count}
                            Id:
                            ${seat.id}
                            ${localeValues['text.cost']}:
                            ${wagon.train.localeCost}
                            ${localeValues['text.current.UAH']}
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