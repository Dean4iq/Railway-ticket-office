<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.search']}</title>
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

        <table border="1">
            <td>
                <th>${localeValues['text.train.number']}</th>
                <th>${localeValues['text.station.from']}</th>
                <th>${localeValues['text.station.to']}</th>
                <th>${localeValues['table.column.date']}</th>
                <th>${localeValues['table.column.time']}</th>
            </td>
            <td>
                <tr>${trainInfo.id}</tr>
                <tr>${trainInfo.departureRoute.station.name}</tr>
                <tr>${trainInfo.arrivalRoute.station.name}</tr>
                <tr>${trainInfo.id}</tr>
                <tr>${trainInfo.id}</tr>
            </td>
        </table>
    </body>
</html>