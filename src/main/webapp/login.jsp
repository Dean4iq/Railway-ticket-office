<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.login']}</title>
        <style>
            <jsp:directive.include file="/styles/css/bootstrap.min.css" />
            <jsp:directive.include file="/styles/css/signin.css" />
        </style>
    </head>

    <body class="text-center">
        <form class="form-signin" method="post">
            <h1 class="h3 mb-3 font-weight-normal">${localeValues['head.login']}</h1>
            <label for="inputLogin" class="sr-only">${localeValues['text.login']}</label>
            <input type="text" id="inputLogin" name="login" class="form-control" placeholder="Login" required autofocus oninvalid="this.setCustomValidity('${localeValues['hint.required']}')">
            <label for="inputPassword" class="sr-only">${localeValues['text.password']}</label>
            <input type="password" name="pass" id="inputPassword" class="form-control" placeholder="${localeValues['text.password']}" required oninvalid="this.setCustomValidity('${localeValues['hint.required']}')">
            <button name="login" class="btn btn-lg btn-primary btn-block" type="submit">${localeValues['btn.login']}</button>
        </form>
    </body>
</html>