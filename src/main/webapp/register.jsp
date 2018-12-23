<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.register']}</title>

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
                    <td>password:</td>
                    <td><input type="password" placeholder="${localeValues['text.password']}" name="password" required oninvalid="this.setCustomValidity('${localeValues['hint.required']}')"/></td>
                </tr>
            </table>
            <br>
            <p align="center"><input type="submit" value="${localeValues['btn.register']}" name="register"/></p>
        </form>
    </body>
</html>