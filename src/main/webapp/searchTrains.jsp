<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>Search</title>
        <link href="style/head.css" rel="stylesheet" type="text/css"/>
    </head>

    <body>
        <form name="langForm" method="post">
            <select name="langSelect" onchange="document.langForm.submit();">
                <option value="en" ${langVariable=="en"?"selected":""}>English</option>
                <option value="ua" ${langVariable=="ua"?"selected":""}>Українська</option>
            </select>
        </form>
        <div class="nav">
            <ul>
                <li><a href="${pageContext.request.contextPath}">Головна сторінка</a></li>
                <li><a href="search">Пошук поїзда</a></li>
                <c:forEach items="${userbar}" var="keyValue">
                    <li><a href="${keyValue.value}">${keyValue.key}</a></li>
                </c:forEach>
            </ul>
        </div>

        <h1>Search page</h1>
        <form method="post">
            <table align="center">
                <caption><h3>Пошук за № поїзда</h3></caption>
                <tr>
                    <td>№ поїзда:</td>
                    <td><input type="text" placeholder="№ поїзда" name="trainNumber"/></td>
                </tr>
            </table>
            <br>
            <p align="center"><input type="submit" value="Search" name="trainNumberSubmit"/></p>
        </form>
        <p style="border-bottom:1px solid;"></p>
        <form method="post">
            <table align="center">
                <caption><h3>Пошук за напрямом поїзда</h3></caption>
                <tr>
                    <th>Станція відправлення</th>
                    <th></th>
                    <th>Станція прибуття</th>
                </tr>
                <tr>
                    <td><input type="text" placeholder="departure" required name="departureStation"/></td>
                    <td style="padding-left: 30px; padding-right: 30px;">
                        ←<input type="submit" value="Switch" name="SwitchDirections"/>→
                    </td>
                    <td><input type="text" placeholder="destination" required name="destinationStation"/></td>
                </tr>
            </table>
            <br>
            <p align="center"><input type="submit" value="Search" name="trainDestinationSubmit"/></p>
        </form>
        <p style="border-bottom:1px solid;"></p>
        <form method="post">
            <h3 align="center">Вивести всі поїзда</h3>
            <p align="center">
                <input type="submit" value="Search" name="allTrainSubmit"/>
            </p>
        </form>

        <h1 align="center">Train LIST</h1>
        <c:forEach var="train" items="${trainList}">
            ${train.id}
            ${train.cost}
        </c:forEach>
    </body>
</html>