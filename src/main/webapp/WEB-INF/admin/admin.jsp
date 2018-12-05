<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>Main</title>
        <link href="${pageContext.request.contextPath}/head.css" rel="stylesheet" type="text/css"/>
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
                <li><a href="logout">Logout</a></li>
            </ul>
        </div>

        <h1>Admin</h1>
    </body>
</html>