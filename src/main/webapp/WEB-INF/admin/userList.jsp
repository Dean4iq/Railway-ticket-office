<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.userList']}</title>
        <style>
            <jsp:directive.include file="/styles/css/bootstrap.min.css"/>
        </style>

        <script>
            <jsp:directive.include file="/styles/js/bootstrap.min.js"/>
        </script>
    </head>

    <body>
        <nav class="navbar navbar-expand-lg navbar-dark" style="background-color:#0c5e00;">
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
                                ${keyValue.key}
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

        <h1>${localeValues['head.userlist']}</h1>
        <table border="1" width="100%">
            <tr align="center">
                <th>${localeValues['table.column.login']}</th>
                <th>${localeValues['table.column.password']}</th>
                <th>${localeValues['table.column.name']}</th>
                <th>${localeValues['table.column.lastName']}</th>
                <th>${localeValues['table.column.nameUA']}</th>
                <th>${localeValues['table.column.lastNameUA']}</th>
                <th>${localeValues['table.column.isAdmin']}</th>
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