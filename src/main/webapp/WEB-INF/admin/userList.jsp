<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<html>
    <head>
        <title>${localeValues['head.userList']}</title>
        <style>
            <jsp:directive.include file="/styles/css/bootstrap.min.css"/>
        </style>

        <script>
            <jsp:directive.include file="/styles/js/bootstrap.min.js"/>
        </script>
    </head>

    <body>
        <jsp:include page="/styles/page_parts/navbar.jsp"/>

        <h1>${localeValues['head.userlist']}</h1>
        <table border="1" align="center" width="95%">
            <tr align="center">
                <th>${localeValues['table.column.login']}</th>
                <th>${localeValues['table.column.password']}</th>
                <th>${localeValues['table.column.name']}</th>
                <th>${localeValues['table.column.lastName']}</th>
                <th>${localeValues['table.column.nameUA']}</th>
                <th>${localeValues['table.column.lastNameUA']}</th>
                <th>${localeValues['table.column.isAdmin']}</th>
                <th>${localeValues['table.column.action']}</th>
            </tr>

            <form name="actionPerformForm" method="post">
                <c:forEach items="${userList}" var="user">
                    <tr align="center">
                        <td>${user.login}</td>
                        <td>${user.password}</td>
                        <td>${user.name}</td>
                        <td>${user.lastName}</td>
                        <td>${user.nameUA}</td>
                        <td>${user.lastNameUA}</td>
                        <td>${user.admin}</td>
                        <td>
                            <c:if test="${not user.admin}">
                                <select name="actionPicker" onchange="document.actionPerformForm.submit();">
                                    <option></option>
                                    <option value="update${user.login}">${localeValues['action.update']}</option>
                                    <option value="delete${user.login}">${localeValues['action.delete']}</option>
                                </select>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </form>
        </table>
        <br>
        <c:if test="${not empty selectedUser}">
            <form method="post">
                <table border="1" align="center" width="95%">
                    <tr align="center">
                        <th>${localeValues['table.column.login']}</th>
                        <th>${localeValues['table.column.password']}</th>
                        <th>${localeValues['table.column.name']}</th>
                        <th>${localeValues['table.column.lastName']}</th>
                        <th>${localeValues['table.column.nameUA']}</th>
                        <th>${localeValues['table.column.lastNameUA']}</th>
                        <th>${localeValues['table.column.isAdmin']}</th>
                    </tr>
                    <tr align="center">
                        <td>${selectedUser.login}</td>
                        <td>${selectedUser.password}</td>
                        <td>${selectedUser.name}</td>
                        <td>${selectedUser.lastName}</td>
                        <td>${selectedUser.nameUA}</td>
                        <td>${selectedUser.lastNameUA}</td>
                        <td>${selectedUser.admin}</td>
                    </tr>
                    <tr align="center">
                        <td>${selectedUser.login}</td>
                        <td><input type="text" name="updatePassword"/></td>
                        <td><input type="text" name="updateName"/></td>
                        <td><input type="text" name="updateLastName"/></td>
                        <td><input type="text" name="updateNameUA"/></td>
                        <td><input type="text" name="updateLastNameUA"/></td>
                        <td>${selectedUser.admin}</td>
                    </tr>
                </table>
            <input type="submit" name="changeUserData" value="${localeValues['action.update']}"/>
            </form>
            <c:if test="${nameInvalid}">
                <div class="alert alert-danger" role="alert">
                    ${localeValues['text.register.nameRegEx']}
                </div>
            </c:if>
            <c:if test="${lastNameInvalid}">
                <div class="alert alert-danger" role="alert">
                    ${localeValues['text.register.lastNameRegEx']}
                </div>
            </c:if>
            <c:if test="${nameUAInvalid}">
                <div class="alert alert-danger" role="alert">
                    ${localeValues['text.register.nameUARegEx']}
                </div>
            </c:if>
            <c:if test="${lastNameUAInvalid}">
                <div class="alert alert-danger" role="alert">
                    ${localeValues['text.register.lastNameUARegEx']}
                </div>
            </c:if>
            <c:if test="${passwordInvalid}">
                <div class="alert alert-danger" role="alert">
                    ${localeValues['text.register.passwordRegEx']}
                </div>
            </c:if>
        </c:if>
    </body>
</html>