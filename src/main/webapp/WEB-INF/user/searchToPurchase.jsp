<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.search']}</title>
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

        <h1>${localeValues['head.search']}</h1>
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
                        ←<input type="submit" value="${localeValues['btn.switch']}" name="SwitchDirections"/>→
                    </td>
                    <td><input type="text" placeholder="destination" required name="destinationStation"/></td>
                </tr>
            </table>
            <br>
            <div align="center">
                <input type="date" name="tripStartDate" min="${minCalendarDate}" required max="${maxCalendarDate}">
            </div>
            <br>
            <p align="center"><input type="submit" value="${localeValues['btn.searchSubmit']}" name="ticketSearchSubmit"/></p>
        </form>
        <p style="border-bottom:1px solid;"></p>

        <c:if test="not empty ${trainList}">
            <h3 align="center">Train LIST</h3>
            <table border="1">
                <tr>
                    <th>${localeValues['table.column.trainNumber']}</th>
                    <th>${localeValues['table.column.route']}</th>
                    <th>${localeValues['table.column.date']}</th>
                    <th>${localeValues['table.column.time']}</th>
                    <th>${localeValues['table.column.seats']}</th>
                </tr>
                <c:forEach var="train" items="${trainList}">
                    <tr>
                        <td>${train.id}</td>
                        <td>${train.departureRoute.station.name} - ${train.arrivalRoute.station.name}</td>
                        <td>
                            ${localeValues['text.station.from']}:${train.departureRoute.formattedDepartureDate}
                            <br>
                            ${localeValues['text.station.to']}:${train.arrivalRoute.formattedArrivalDate}
                        </td>
                        <td>
                            ${localeValues['text.station.from']}: ${train.departureRoute.formattedDepartureTime}
                            <br>
                            ${localeValues['text.station.to']}: ${train.arrivalRoute.formattedArrivalTime}
                        </td>
                        <td>${localeValues['btn.lookSeat']}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </body>
</html>