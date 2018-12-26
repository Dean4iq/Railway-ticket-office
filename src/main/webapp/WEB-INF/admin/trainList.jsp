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