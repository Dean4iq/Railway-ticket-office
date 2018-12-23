<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.userList']}</title>
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

        <h1>${localeValues['head.userlist']}</h1>
        <table border="1" width="100%">
            <tr align="center">
                <th>Login</th>
                <th>Password</th>
                <th>Name</th>
                <th>Last name</th>
                <th>Name UA</th>
                <th>Last name UA</th>
                <th>Is admin</th>
            </tr>

            <c:forEach items="${userList}" var="user">
                <tr align="center">
                    <td>${user.login}</td>
                    <td>${user.password}</td>
                    <td>${user.name}</td>
                    <td>${user.lastName}</td>
                    <td>${user.nameUA}</td>
                    <td>${user.lastNameUA}</td>
                    <td>${user.admin}</td>
                </tr>
            </c:forEach>

        </table>
    </body>
</html>