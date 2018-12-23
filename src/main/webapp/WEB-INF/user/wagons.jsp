<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.search']}</title>
        <style>
            <jsp:directive.include file="/styles/css/head_style.css"/>
            <jsp:directive.include file="/styles/_variables.scss"/>
            <jsp:directive.include file="/styles/bootstrap.css"/>
            <jsp:directive.include file="/styles/bootstrap.min.css"/>
            <jsp:directive.include file="/styles/custom.sass"/>
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
                    <td>${trainInfo.departureRoute.station.name}</td>
                    <td>${trainInfo.arrivalRoute.station.name}</td>
                    <td>${trainInfo.departureRoute.departureTime}</td>
                    <td>${trainInfo.arrivalRoute.arrivalTime}</td>
                </tr>
            </tbody>
        </table>

        <h3 align="center" style="border-top:1px solid;">${localeValues['table.column.seats']}</h3>
        <c:forEach items="${wagonList}" var="wagon">
            <table align="center" border="1">
                <tr>
                    <th>Wagon id</th>
                    <th>Wagon type</th>
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
                            <input type="submit" name="${seat.id}" value="${localeValues['btn.purchaseTicket']}"/>
                        </c:if>
                    </li>
                </ul>
            </c:forEach>
        </c:forEach>
    </body>
</html>