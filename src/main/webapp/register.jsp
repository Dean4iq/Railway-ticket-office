<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.register']}</title>

        <style>
            <jsp:directive.include file="/styles/css/bootstrap.min.css"/>
        </style>

        <script>
            <jsp:directive.include file="/styles/js/bootstrap.min.js"/>
        </script>
    </head>

    <body>
        <nav class="navbar navbar-expand-lg navbar-dark" style="background-color:#0c5e00;margin:10px">
            <a class="navbar-brand" href=""></a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item active">
                        <a class="nav-link" href="${pageContext.request.contextPath}">
                            ${localeValues['btn.main']}
                        </a>
                    </li>
                    <li class="nav-item active">
                        <a class="nav-link" href="search">
                            ${localeValues['btn.search']}
                        </a>
                    </li>
                    <c:forEach items="${userbar}" var="keyValue">
                        <li class="nav-item active">
                            <a class="nav-link" href="${keyValue.value}">
                                <c:out value="${localeValues[keyValue.key]}"/>
                            </a>
                        </li>
                    </c:forEach>
                </ul>
                <ul class="navbar-nav">
                    <form name="langForm" method="post" align="right">
                        <select name="langSelect" onchange="document.langForm.submit();">
                            <option ${langVariable=="en"?"selected":""} value="en">English</option>
                            <option ${langVariable=="uk"?"selected":""} value="uk">Українська</option>
                        </select>
                    </form>
                </ul>
            </div>
        </nav>

        <div class="container" style="margin:20px 100px 0 100px;">
            <h4 class="mb-3">${localeValues['head.register']}</h4>
            <form class="needs-validation" method="post">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="nameEN">Name</label>
                        <input type="text" class="form-control" id="nameEN" name="name" placeholder required>
                        <div class="invalid-feedback">
                            ${localeValues['hint.required']}
                        </div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="lastnameEN">Name</label>
                        <input type="text" class="form-control" id="lastnameEN" name="lastName" placeholder required>
                        <div class="invalid-feedback">
                            ${localeValues['hint.required']}
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="nameUA">Ім &rsquo; я'</label>
                        <input type="text" class="form-control" id="nameUA" name="nameUA" placeholder required>
                        <div class="invalid-feedback">
                            ${localeValues['hint.required']}
                        </div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="lastnameUA">Прізвище</label>
                        <input type="text" class="form-control" id="lastnameUA" name="lastNameUA" placeholder required>
                        <div class="invalid-feedback">
                            ${localeValues['hint.required']}
                        </div>
                    </div>
                </div>
                <div class="mb-3">
                    <label for="login">Login</label>
                    <input type="text" class="form-control" id="login" name="login" placeholder required>
                    <div class="invalid-feedback">
                        ${localeValues['hint.required']}
                    </div>
                </div>
                <div class="mb-3">
                    <label for="pass">${localeValues['text.password']}</label>
                    <input type="text" class="form-control" id="pass" name="password" placeholder required>
                    <div class="invalid-feedback">
                        ${localeValues['hint.required']}
                    </div>
                </div>
                <button class="btn btn-primary btn-lg btn-block" type="submit" name="register">${localeValues['btn.register']}</button>
            </form>
            <a href="${pageContext.request.contextPath}/login">${localeValues['text.register.oldie']}</a>
        </div>
    </body>
</html>