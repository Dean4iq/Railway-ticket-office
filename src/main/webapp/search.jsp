<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>Search</title>
        <link href="/head.css" rel="stylesheet" type="text/css"/>
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
        <div class="nav">
            <ul>
                <li><a href="${pageContext.request.contextPath}">Головна сторінка</a></li>
                <li><a href="search">Пошук поїзда</a></li>
                <li><a href="login">Login</a></li>
                <li><a href="register">Реєстрація</a></li>
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
                    <td><input type="text" placeholder="departure" name="departure"/></td>
                    <td style="padding-left: 30px; padding-right: 30px;">
                        ←<input type="submit" value="Switch" name="SwitchDirections"/>→
                    </td>
                    <td><input type="text" placeholder="destination" name="destination"/></td>
                </tr>
            </table>
            <br>
            <p align="center"><input type="submit" value="Search" name="trainDestinationSubmit"/></p>
        </form>
    </body>
</html>