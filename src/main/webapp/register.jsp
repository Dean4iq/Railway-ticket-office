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
        <jsp:include page="/styles/page_parts/navbar.jsp"/>

        <div class="container" style="margin:20px 100px 0 100px;">
            <h4 class="mb-3">${localeValues['head.register']}</h4>
            <form class="needs-validation" method="post">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="nameEN">${localeValues['text.register.nameEN']}</label>
                        <input type="text" class="form-control" id="nameEN" name="name" placeholder value required>
                        <div class="invalid-feedback">
                            ${localeValues['hint.required']}
                        </div>
                        <c:if test="${nameInvalid}">
                            <div class="alert alert-danger" role="alert">
                                ${localeValues['text.register.nameRegEx']}
                            </div>
                        </c:if>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="lastnameEN">${localeValues['text.register.lastNameEN']}</label>
                        <input type="text" class="form-control" id="lastnameEN" name="lastName" placeholder value required>
                        <div class="invalid-feedback">
                            ${localeValues['hint.required']}
                        </div>
                        <c:if test="${lastNameInvalid}">
                            <div class="alert alert-danger" role="alert">
                                ${localeValues['text.register.lastNameRegEx']}
                            </div>
                        </c:if>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="nameUA">${localeValues['text.register.nameUA']}</label>
                        <input type="text" class="form-control" id="nameUA" name="nameUA" placeholder value required>
                        <div class="invalid-feedback">
                            ${localeValues['hint.required']}
                        </div>
                        <c:if test="${nameUAInvalid}">
                            <div class="alert alert-danger" role="alert">
                                ${localeValues['text.register.nameUARegEx']}
                            </div>
                        </c:if>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="lastnameUA">${localeValues['text.register.lastNameUA']}</label>
                        <input type="text" class="form-control" id="lastnameUA" name="lastNameUA" placeholder value required>
                        <div class="invalid-feedback">
                            ${localeValues['hint.required']}
                        </div>
                        <c:if test="${lastNameUAInvalid}">
                            <div class="alert alert-danger" role="alert">
                                ${localeValues['text.register.lastNameUARegEx']}
                            </div>
                        </c:if>
                    </div>
                </div>
                <div class="mb-3">
                    <label for="login">Login</label>
                    <input type="text" class="form-control" id="login" name="login" placeholder value required>
                    <div class="invalid-feedback">
                        ${localeValues['hint.required']}
                    </div>
                    <c:if test="${notUniqueLogin}">
                        <div class="alert alert-danger" role="alert">
                            ${localeValues['text.register.usedLogin']}
                        </div>
                    </c:if>
                    <c:if test="${loginInvalid}">
                        <div class="alert alert-danger" role="alert">
                            ${localeValues['text.login.text.register.loginRegEx']}
                        </div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="pass">${localeValues['text.password']}</label>
                    <input type="password" class="form-control" id="pass" name="password" placeholder value required>
                    <div class="invalid-feedback">
                        ${localeValues['hint.required']}
                    </div>
                    <c:if test="${passwordInvalid}">
                        <div class="alert alert-danger" role="alert">
                            ${localeValues['text.register.passwordRegEx']}
                        </div>
                    </c:if>
                </div>
                <button class="btn btn-primary btn-lg btn-block" type="submit" name="register">${localeValues['btn.register']}</button>
            </form>
            <a href="${pageContext.request.contextPath}/login">${localeValues['text.register.oldie']}</a>
        </div>
    </body>
</html>