<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="pag" uri="/WEB-INF/tld/paginate.tld" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.search']}</title>

        <style>
            <jsp:directive.include file="/styles/css/bootstrap.min.css"/>
            <jsp:directive.include file="/styles/css/pagination.css"/>
        </style>

        <script>
            <jsp:directive.include file="/styles/js/bootstrap.min.js"/>
        </script>
    </head>

    <body>
        <jsp:include page="/styles/page_parts/navbar.jsp"/>

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
            <p align="center"><input type="submit" value="${localeValues['btn.searchSubmit']}" name="trainDestinationSubmit"/></p>
        </form>

        <p style="border-bottom:1px solid;"></p>

        <form method="post">
            <h3 align="center">${localeValues['text.train.searchAll']}</h3>
            <p align="center">
                <input type="submit" value="${localeValues['btn.searchSubmit']}" name="allTrainSubmit"/>
            </p>
        </form>

        <c:if test="${noTrainInList}">
            <div class="alert alert-warning" role="alert">
                ${localeValues['text.search.noSuchTrain']}
            </div>
        </c:if>
        <c:if test="${not empty trainList}">
            <h3 align="center">${localeValues['head.trainList']}</h3>
            <table align="center" style="text-align:center;" border="1">
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
                        <td align="left">
                            <div>
                                <div style="display: inline;">
                                    ${localeValues['table.column.departure']}:
                                </div>
                                <div style="float: right; margin-left: 15px;">
                                    ${train.departureRoute.formattedDepartureDate}
                                </div>
                            </div>
                            <div>
                                <div style="display: inline;">
                                    ${localeValues['table.column.arrival']}:
                                </div>
                                <div style="float: right; margin-left: 15px;">
                                    ${train.arrivalRoute.formattedArrivalDate}
                                </div>
                            </div>
                        </td>
                        <td align="left">
                            <div>
                                <div style="display: inline;">
                                    ${localeValues['table.column.departure']}:
                                </div>
                                <div style="float: right; margin-left: 15px;">
                                    ${train.departureRoute.formattedDepartureTime}
                                </div>
                            </div>
                            <div>
                                <div style="display: inline;">
                                    ${localeValues['table.column.arrival']}:
                                </div>
                                <div style="float: right; margin-left: 15px;">
                                    ${train.arrivalRoute.formattedArrivalTime}
                                </div>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </table>

            <pag:pagination pageNumber="${pageNumber}" currentPage="${currentPage}"/>
        </c:if>
    </body>
</html>