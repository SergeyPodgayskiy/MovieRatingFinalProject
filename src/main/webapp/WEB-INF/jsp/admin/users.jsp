<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentLanguage"
       value="${cookie.userLanguage.value != null ? cookie.userLanguage.value : sessionScope.defaultLanguage}"/>
<fmt:setLocale value="${currentLanguage}"/>
<fmt:setBundle basename="localization" var="loc"/>
<!DOCTYPE html>
<html lang="${currentLanguage}">
<head>
    <meta charset="UTF-8">
    <meta name="description" content="MovieRating">
    <meta name="keywords" content="Movie, MovieRating">
    <meta name="author" content="Sergey Podgaysky">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/font-awesome.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/top-navbar.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/sidebar-menu.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/login-modal-style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/datatables.min.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/datatables.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/movie-table.js"></script>
    <link href="https://fonts.googleapis.com/css?family=Lato|Nunito|Open+Sans|Oxygen|Poppins|Roboto" rel="stylesheet">
    <title>MovieRating</title>
</head>
<body>
<div class="container-fluid container-wrapper">
    <div class="row content-wrapper">
        <div class="col-sm-2 col__padding_0">
            <c:import url="../template/admin-navside-menu.jsp"/>
        </div>
        <div class="col-sm-10 col__padding_0">

            <div class="row row__margin_0 top-nav__wrapper">
                <c:import url="../template/header.jsp"/>
                <div class="col-sm-12 <%--col__padding_0--%> inner-page__wrapper">
                    <div class="row row__margin_0">
                        <div class="col-sm-12">
                            <h1><fmt:message bundle="${loc}" key="users"/></h1>
                        </div>
                    </div>
                    <div class="row row__margin_0">
                        <div class="col-sm-12">
                            <div class="card">
                                <div class="table-responsive  table-wrapper">
                                    <table id="movies-table" class="table table-striped top-movie-table">
                                        <thead>
                                        <tr>
                                            <th>#</th>
                                            <th class="no-sort"></th>
                                            <th><fmt:message bundle="${loc}" key="login"/></th>
                                            <th><fmt:message bundle="${loc}" key="full.name"/></th>
                                            <th><fmt:message bundle="${loc}" key="register.date"/></th>
                                            <th><fmt:message bundle="${loc}" key="role"/></th>
                                            <th><fmt:message bundle="${loc}" key="ban.status"/></th>
                                            <th><fmt:message bundle="${loc}" key="delete.date"/></th>
                                            <th class="no-sort"></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="user" items="${requestScope.users}"
                                                   varStatus="loop">
                                            <tr>
                                                <td class="number-column">
                                                        ${loop.count}
                                                </td>
                                                <td class="avatar-column">
                                                    <a href="">
                                                        <img class="img" src="./${user.photoURL}">
                                                    </a>
                                                </td>
                                                <td class="login-column">
                                                    <a href="Controller?command=show-user&userId=${user.id}">
                                                            ${user.login}
                                                    </a>
                                                </td>
                                                <td class="full-name-column">
                                                    <a href="Controller?command=show-user&userId=${user.id}">
                                                            ${user.fullName}
                                                    </a>
                                                </td>
                                                <td class="register-date-column">
                                                        ${user.registerDate}
                                                </td>
                                                <td class="is-admin-column">
                                                        ${user.isAdmin()}
                                                </td>
                                                <td class="is-banned-column">
                                                        ${user.isBanned()}
                                                </td>
                                                <td class="deleted-at-column">
                                                        ${user.deletedAt}
                                                </td>
                                                <td>
                                                    .
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

