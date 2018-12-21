<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.register']}</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/head_style.css" type="text/css">
        <style>
            .nav a{
                text-decoration:none;
            }
            .nav{
                height:70px;
                background:#025E10;
                position:relative;
            }
            .nav>ul{
                position:relative;
                list-style:none;
                padding:0;
                margin:0;
            }
            .nav>ul>li{
                float:left;
                position:relative;
            }

            .nav>ul>li>a{
                padding:0 20px;
                color:#fff;
                display:block;
                line-height:70px !important;
                font:400 15px 'PT Sans', sans-serif;
                text-transform:uppercase;
                text-decoration:none;
            }

            .nav a:hover{
                background: black;
            }
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
                    <td><input type="text" required placeholder="Name" name="name"/></td>
                    <td title="${localeValues['hint.register.name']}">?</td>
                </tr>
                <tr>
                    <td>Last name:</td>
                    <td><input type="text" required placeholder="Last name" name="lastName"/></td>
                    <td title="${localeValues['hint.register.lastName']}">?</td>
                </tr>
                <tr>
                    <td>Ім&rsquo;я:</td>
                    <td><input type="text" required placeholder="Ім'я" name="nameUA"/></td>
                    <td title="${localeValues['hint.register.nameUA']}">?</td>
                </tr>
                <tr>
                    <td>Прізвище:</td>
                    <td><input type="text" required placeholder="Прізвище" name="lastNameUA"/></td>
                    <td title="${localeValues['hint.register.lastNameUA']}">?</td>
                </tr>
                <tr>
                    <td>login:</td>
                    <td><input type="text" required placeholder="login" name="login"/></td>
                </tr>
                <tr>
                    <td>password:</td>
                    <td><input type="password" required placeholder="${localeValues['text.password']}" name="password"/></td>
                </tr>
            </table>
            <br>
            <p align="center"><input type="submit" value="${localeValues['btn.register']}" name="register"/></p>
        </form>
    </body>
</html>