<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.trainlist']}</title>
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

        <table align="center" border="1">
            <tr>
                <th># train</th>
                <th>Route</th>
                <th>Departure</th>
                <th>Arrival</th>
            </tr>
            <c:forEach items="${trainList}" var="train">
              <tr>
                <td>train.id</td>
                <td>train.routeList[0].station.name</td>
                <td></td>
                <td></td>
              </tr>
            </c:forEach>
        </table>
    </body>
</html>