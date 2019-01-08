<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.trainList']}</title>
        <style>
            <jsp:directive.include file="/styles/css/bootstrap.min.css"/>
        </style>

        <script>
            <jsp:directive.include file="/styles/js/bootstrap.min.js"/>
        </script>
    </head>

    <body>
        <jsp:include page="/styles/page_parts/navbar.jsp"/>

        <h1>${localeValues['head.trainlist']}</h1>
        <table border="1">
            <tr>
                <th>${localeValues['table.column.trainNumber']}</th>
                <th>${localeValues['table.column.route']}</th>
                <th>${localeValues['table.column.departure']}</th>
                <th>${localeValues['table.column.arrival']}</th>
                <th>${localeValues['table.column.action']}</th>
            </tr>
            <form method="post">
                <c:forEach items="${trainList}" var="train">
                  <tr>
                    <td><c:out value="${train.id}"/></td>
                    <td>
                        <c:if test="${langVariable=='en'}">
                            <c:out default="null" value="${train.departureRoute.station.nameEN}"/>
                            -
                            <c:out default="null" value="${train.arrivalRoute.station.nameEN}"/>
                        </c:if>
                        <c:if test="${langVariable=='uk'}">
                            <c:out default="null" value="${train.departureRoute.station.nameUA}"/>
                            -
                            <c:out default="null" value="${train.arrivalRoute.station.nameUA}"/>
                        </c:if>
                    </td>
                    <td>
                        <c:out default="null" value="${train.departureRoute.formattedDepartureTime}"/>
                    </td>
                    <td>
                        <c:out default="null" value="${train.arrivalRoute.formattedArrivalTime}"/>
                    </td>
                    <td>
                        <input type="submit" name="del${train.id}" value="${localeValues['action.delete']}"/>
                    </td>
                  </tr>
                </c:forEach>
            </form>
        </table>
    </body>
</html>