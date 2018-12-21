<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.search']}</title>
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

        <h1>${localeValues['head.search']}</h1>
        <form method="post">
            <table align="center">
                <caption><h3>${localeValues['text.train.searchByNumber']}</h3></caption>
                <tr>
                    <td>${localeValues['text.train.number']}:</td>
                    <td><input type="text" placeholder="${localeValues['text.train.number']}" name="trainNumber"/></td>
                </tr>
            </table>
            <br>
            <p align="center"><input type="submit" value="${localeValues['btn.searchSubmit']}" name="trainNumberSubmit"/></p>
        </form>
        <p style="border-bottom:1px solid;"></p>
        <form method="post">
            <table align="center">
                <caption><h3>${localeValues['text.train.searchByRoute']}</h3></caption>
                <tr>
                    <th>${localeValues['text.station.from']}</th>
                    <th></th>
                    <th>${localeValues['text.station.to']}</th>
                </tr>
                <tr>
                    <td><input type="text" placeholder="${localeValues['text.station.from']}" required name="departureStation"/></td>
                    <td style="padding-left: 30px; padding-right: 30px;">
                        ←<input type="submit" value="Switch" name="SwitchDirections"/>→
                    </td>
                    <td><input type="text" placeholder="${localeValues['text.station.to']}" required name="destinationStation"/></td>
                </tr>
            </table>
            <br>
            <p align="center"><input type="submit" value="${localeValues['btn.searchSubmit']}" name="trainDestinationSubmit"/></p>
        </form>
        <p style="border-bottom:1px solid;"></p>
        <form method="post">
            <h3 align="center">${localeValues['text.train.searchAll']}</h3>
            <p align="center">
                <input type="submit" value="${localeValues['btn.searchSubmit']}" name="allTrainSubmit"/>
            </p>
        </form>

        <c:if test="not empty ${trainList}">
            <h3 align="center">Train LIST</h3>
            <table align="center" border="1">
                <tr>
                    <th># train</th>
                    <th>Route</th>
                    <th>Departure</th>
                    <th>Arrival</th>
                </tr>
                <c:forEach items="${trainList}" var="train">
                    <tr>
                        <td>${train.id}</td>
                        <td>${train.departureRoute.station.name} - ${train.arrivalRoute.station.name}</td>
                        <td>
                            Відправлення:${train.departureRoute.formattedDepartureDate}
                            <br>
                            Прибуття:${train.arrivalRoute.formattedArrivalDate}
                        </td>
                        <td>
                            Відправлення: ${train.departureRoute.formattedDepartureTime}
                            <br>
                            Прибуття: ${train.arrivalRoute.formattedArrivalTime}
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </body>
</html>