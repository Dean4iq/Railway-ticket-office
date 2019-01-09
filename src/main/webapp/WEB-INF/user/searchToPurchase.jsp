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

        <c:if test="${noTrainInList}">
            <div class="alert alert-warning" role="alert">
                ${localeValues['text.search.noSuchTrain']}
            </div>
        </c:if>
        <c:if test="${not empty trainList}">
            <h3 align="center">${localeValues['head.trainList']}</h3>
            <table align="center" border="1">
                <tr style="text-align: center;">
                    <form method="post">
                        <th>
                            ${localeValues['table.column.trainNumber']}
                            <input type="submit" name="sortTrainNumAsc" value="↑"/>
                            <input type="submit" name="sortTrainNumDesc" value="↓"/>
                        </th>
                        <th>${localeValues['table.column.trainRoute']}</th>
                        <th>${localeValues['table.column.route']}</th>
                        <th>${localeValues['table.column.date']}</th>
                        <th>
                            ${localeValues['table.column.time']}
                            <input type="submit" name="sortTimeAsc" value="↑"/>
                            <input type="submit" name="sortTimeDesc" value="↓"/>
                        </th>
                        <th>${localeValues['table.column.seats']}</th>
                    </form>
                </tr>
                <form method="post">
                    <c:forEach var="train" items="${trainList}">
                        <tr style="text-align: center;">
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
                                <c:if test="${langVariable=='en'}">
                                    ${train.userDepartureRoute.station.nameEN} - ${train.userArrivalRoute.station.nameEN}
                                </c:if>
                                <c:if test="${langVariable=='uk'}">
                                    ${train.userDepartureRoute.station.nameUA} - ${train.userArrivalRoute.station.nameUA}
                                </c:if>
                            </td>
                            <td align="left">
                                <div>
                                    <div style="display: inline;">
                                        ${localeValues['table.column.departure']}:
                                    </div>
                                    <div style="float: right; margin-left: 15px;">
                                        ${train.userDepartureRoute.formattedDepartureDate}
                                    </div>
                                </div>
                                <div>
                                    <div style="display: inline;">
                                        ${localeValues['table.column.arrival']}:
                                    </div>
                                    <div style="float: right; margin-left: 15px;">
                                        ${train.userArrivalRoute.formattedArrivalDate}
                                    </div>
                                </div>
                            </td>
                            <td align="left">
                                <div>
                                    <div style="display: inline;">
                                        ${localeValues['table.column.departure']}:
                                    </div>
                                    <div style="float: right; margin-left: 15px;">
                                        ${train.userDepartureRoute.formattedDepartureTime}
                                    </div>
                                </div>
                                <div>
                                    <div style="display: inline;">
                                        ${localeValues['table.column.arrival']}:
                                    </div>
                                    <div style="float: right; margin-left: 15px;">
                                        ${train.userArrivalRoute.formattedArrivalTime}
                                    </div>
                                </div>
                            </td>
                            <td><input type="submit" value="${localeValues['btn.lookSeat']}" name="wagonInTrain${train.id}"/></td>
                        </tr>
                    </c:forEach>
                </form>
            </table>
        </c:if>
    </body>
</html>