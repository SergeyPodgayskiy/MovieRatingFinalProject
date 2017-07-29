<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${cookie.userLanguage.value != null ? cookie.userLanguage.value : sessionScope.defaultLanguage}"/>
<c:set var="userRole" value="${cookie.role.value != null ? cookie.role.value : sessionScope.role}"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="localization" var="loc"/>
<!DOCTYPE html>
<html lang="${language}">
<head>
    <meta charset="UTF-8">
    <meta name="description" content="MovieRating">
    <meta name="keywords" content="Movie, MovieRating">
    <meta name="author" content="Sergey Podgaysky">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/registration-style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/font-awesome.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/sidebar-menu.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/login-modal-style.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap-show-password.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/loader.js"></script>
    <link href='https://fonts.googleapis.com/css?family=Passion+One' rel='stylesheet' type='text/css'>
    <link href="https://fonts.googleapis.com/css?family=Lato|Nunito|Open+Sans|Oxygen|Poppins|Roboto" rel="stylesheet">
    <title>Registration</title>
</head>
<body>
<div class="loader"></div>
<div class="container-fluid">
    <div class="row register-page-wrapper">
        <div class="col-md-3">
            <div class="logo-section">
                <a href="Controller?command=show-welcome-page">MovieRating</a>
            </div>
        </div>
        <div class="col-md-4 col-md-offset-5">
            <div class="registration-section">
                <div class="main-login main-center">
                    <div class="panel-title text-center">
                        <h1 class="title">
                            <fmt:message bundle="${loc}" key="registration.to.movierating"/>
                        </h1>
                        <hr/>
                    </div>
                    <form class="form-horizontal" action="${pageContext.request.contextPath}/Controller" method="post">
                        <input type="hidden" name="command" value="register">
                        <div class="form-group">
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-male fa-fw"></i></span>
                                <input type="text" class="form-control new-input-style" id="full-name"
                                       placeholder="<fmt:message bundle="${loc}" key="full.name"/>"
                                       name="fullName" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-group">
                                <span class="input-group-addon new-input-style"><i
                                        class="fa fa-envelope-o fa-fw"></i></span>
                                <input type="email" class="form-control new-input-style" id="email-reg"
                                       placeholder="<fmt:message bundle="${loc}" key="email"/>"
                                       name="email" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-group">
                                <span class="input-group-addon new-input-style"><i class="fa fa-user fa-fw"></i></span>
                                <input type="text" class="form-control new-input-style" id="username-reg"
                                       placeholder="<fmt:message bundle="${loc}" key="username"/>"
                                       name="login" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-group">
                                <span class="input-group-addon new-input-style"><i class="fa fa-key fa-fw"></i></span>
                                <input type="password" class="form-control new-input-style" id="password-reg"
                                       placeholder="<fmt:message bundle="${loc}" key="password"/>"
                                       name="password" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-group">
                                <span class="input-group-addon new-input-style"><i class="fa fa-key fa-fw"></i></span>
                                <input type="text" class="form-control new-input-style" id="confirm-psw-reg"
                                       placeholder="<fmt:message bundle="${loc}" key="confirm.password"/>"
                                       name="confirmPassword" required>
                            </div>
                        </div>
                        <div class="form-group ">
                            <input type="hidden" name="previousPageQuery" value="${pageContext.request.queryString}">
                            <button type="submit" class="btn btn-success btn-block active btn-new-style">
                                <fmt:message bundle="${loc}" key="register"/>
                            </button>
                        </div>
                    </form>
                    <div class="register-image">
                        <img src="../../image/register-movie-picture.png" alt="movie-icon">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
