<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.purchase']}</title>
        <style>
            <jsp:directive.include file="/styles/css/bootstrap.min.css"/>
        </style>

        <script>
            <jsp:directive.include file="/styles/js/bootstrap.min.js"/>
        </script>

        <script>
            var countDownDate = new Date(${purchaseTime}+10*60*1000);

            var x = setInterval(function() {
              var now = new Date().getTime();
              var distance = countDownDate - now;

              var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
              var seconds = Math.floor((distance % (1000 * 60)) / 1000);

            if (minutes < 10){
                minutes = '0' + minutes;
            }
            if (seconds < 10){
                seconds = '0' + seconds;
            }

              document.getElementById("timer").innerHTML = minutes + ":" + seconds;

              if (distance < 0) {
                clearInterval(x);
                document.getElementById("timer").innerHTML = "00:00";
                document.getElementById('timeOutAlarm').style.display = 'block';
              }
            }, 1000);
        </script>
    </head>

    <body>
        <jsp:include page="/styles/page_parts/navbar.jsp"/>
        <div class="alert alert-danger" id="timeOutAlarm" role="alert" style="display: none; width: 100%; text-align: center; position:relative;">
            ${localeValues['text.timer.expired']}
        </div>
        <h1>${localeValues['head.purchase']}</h1>
        <div style="font-size: 28px;" align="center">${localeValues['text.purchase.timer']}: <p id="timer" style="font-size: 28px; display:inline;"></p></div>
        <table border="1">
            <caption style="caption-side: top">${localeValues['table.header.travelInfo']}</caption>
            <tr align="center">
                <th>${localeValues['table.column.trainNumber']}</th>
                <th>${localeValues['table.column.trainRoute']}</th>
                <th>${localeValues['table.column.route']}</th>
                <th>${localeValues['table.column.date']}</th>
                <th>${localeValues['table.column.time']}</th>
            </tr>
            <tr align="center">
                <td>${purchasedTicket.train.id}</td>
                <td>
                    <c:if test="${langVariable=='en'}">
                        ${purchasedTicket.train.departureRoute.station.nameEN} - ${purchasedTicket.train.arrivalRoute.station.nameEN}
                    </c:if>
                    <c:if test="${langVariable=='uk'}">
                        ${purchasedTicket.train.departureRoute.station.nameUA} - ${purchasedTicket.train.arrivalRoute.station.nameUA}
                    </c:if>
                </td>
                <td>
                    <c:if test="${langVariable=='en'}">
                        ${purchasedTicket.train.userDepartureRoute.station.nameEN} - ${purchasedTicket.train.userArrivalRoute.station.nameEN}
                    </c:if>
                    <c:if test="${langVariable=='uk'}">
                        ${purchasedTicket.train.userDepartureRoute.station.nameUA} - ${purchasedTicket.train.userArrivalRoute.station.nameUA}
                    </c:if>
                </td>
                <td>
                    ${purchasedTicket.train.userDepartureRoute.formattedDepartureDate}
                    <br>
                    ${purchasedTicket.train.userArrivalRoute.formattedArrivalDate}
                </td>
                <td>
                    ${purchasedTicket.train.userDepartureRoute.formattedDepartureTime}
                    <br>
                    ${purchasedTicket.train.userArrivalRoute.formattedArrivalTime}
                </td>
            </tr>
        </table>
        <br>
        <table border="1">
            <tr align="center">
                <th>${localeValues['table.column.seatId']}</th>
                <th>${localeValues['table.column.wagonId']}</th>
                <th>${localeValues['table.column.departure']}</th>
                <th>${localeValues['table.column.arrival']}</th>
                <th>${localeValues['table.column.travelDate']}</th>
            </tr>
            <tr align="center">
                <td>${purchasedTicket.seatId}</td>
                <td>${purchasedTicket.wagonId}</td>
                <td>
                    <c:if test="${langVariable=='en'}">
                        ${purchasedTicket.departureStation.nameEN}
                    </c:if>
                    <c:if test="${langVariable=='uk'}">
                        ${purchasedTicket.departureStation.nameUA}
                    </c:if>
                </td>
                <td>
                    <c:if test="${langVariable=='en'}">
                        ${purchasedTicket.arrivalStation.nameEN}
                    </c:if>
                    <c:if test="${langVariable=='uk'}">
                        ${purchasedTicket.arrivalStation.nameUA}
                    </c:if>
                </td>
                <td>${purchasedTicket.formattedTravelDate}</td>
            </tr>
        </table>
        ${localeValues['text.cost']}: &nbsp; ${purchasedTicket.localeCost} &nbsp; ${localeValues['text.current.UAH']}

        <form method="POST">
            <input type="submit" name="payForTicket" value="${localeValues['btn.acceptPurchase']}"/>
            <input type="submit" name="declinePayment" value="${localeValues['btn.declinePurchase']}"/>
        </post>
    </body>
</html>
