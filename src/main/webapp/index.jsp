<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
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
            .nav>ul>li>ul{
              position:absolute;
              left:0;
              padding:0;
              margin:0;
              list-style:none;
            }
            .nav>ul>li:hover>ul li a{
              opacity:1;
              height:50px;
            }
            .nav>ul>li>ul a{
              display:block;
              color:#eee;
              width:150px;
              line-height:50px;
              font:700 14px;
              background:#08B832;
              border-bottom:1px solid #ddd;
              text-align:center;
              padding:0 5px;
               height:0;
              overflow:hidden;
              opacity:0;
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
                <li><a href="main.html">Головна сторінка</a></li>
                <li><a href="weaptypes.html">Види зброї</a>
                    <ul>
                        <li><a href="guns.html">Стрілецька зброя</a></li>
                        <li><a href="land.html">Сухопутна техніка</a></li>
                        <li><a href="air.html">Повітряна техніка</a></li>
                        <li><a href="navy.html">Морська техніка</a></li>
                        <li><a href="space.html">Космічні сили</a></li>
                    </ul>
                </li>
                <li><a href="#">Країни</a>
                    <ul>
                        <li><a href="deutschland.html">Німеччина</a></li>
                        <li><a href="russia.html">Росія</a></li>
                        <li><a href="usa.html">США</a></li>
                        <li><a href="ukraine.html">Україна</a></li>
                        <li><a href="france.html">Франція</a></li>
                    </ul>
                </li>
                <li><a href="backup.html">Зворотній звязок</a></li>
                <li><a href="test.html">***</a></li>
            </ul>
        </div>
    </body>
</html>
