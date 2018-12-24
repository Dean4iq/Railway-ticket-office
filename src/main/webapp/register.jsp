<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.register']}</title>

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

        <h1>${localeValues['head.register']}</h1>
        <form method="post">
            <table>
                <tr>
                    <td>Name:</td>
                    <td><input type="text" placeholder="Name" name="name" required oninvalid="this.setCustomValidity('${localeValues['hint.required']}')"/></td>
                    <td title="${localeValues['hint.register.name']}">?</td>
                </tr>
                <tr>
                    <td>Last name:</td>
                    <td><input type="text" placeholder="Last name" name="lastName" required oninvalid="this.setCustomValidity('${localeValues['hint.required']}')"/></td>
                    <td title="${localeValues['hint.register.lastName']}">?</td>
                </tr>
                <tr>
                    <td>Ім&rsquo;я:</td>
                    <td><input type="text" placeholder="Ім'я" name="nameUA" required oninvalid="this.setCustomValidity('${localeValues['hint.required']}')"/></td>
                    <td title="${localeValues['hint.register.nameUA']}">?</td>
                </tr>
                <tr>
                    <td>Прізвище:</td>
                    <td><input type="text" placeholder="Прізвище" name="lastNameUA" required oninvalid="this.setCustomValidity('${localeValues['hint.required']}')"/></td>
                    <td title="${localeValues['hint.register.lastNameUA']}">?</td>
                </tr>
                <tr>
                    <td>login:</td>
                    <td><input type="text" placeholder="login" name="login" required oninvalid="this.setCustomValidity('${localeValues['hint.required']}')"/></td>
                </tr>
                <tr>
                    <td>${localeValues['text.password']}:</td>
                    <td><input type="password" placeholder="${localeValues['text.password']}" name="password" required oninvalid="this.setCustomValidity('${localeValues['hint.required']}')"/></td>
                </tr>
            </table>
            <br>
            <p align="center"><input type="submit" value="${localeValues['btn.register']}" name="register"/></p>
        </form>
    </body>
</html>