<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language"
       value="${cookie.userLanguage.value != null ? cookie.userLanguage.value : sessionScope.defaultLanguage}"/>
<c:set var="userRole" value="${cookie.role.value != null ? cookie.role.value : sessionScope.role}"/>
<%@ taglib prefix="pgn" uri="/WEB-INF/tld/pagination.tld" %>
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
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/font-awesome.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/top-navbar.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/sidebar-menu.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/login-modal-style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/add-and-edit-participant.js"></script>
    <script src="${pageContext.request.contextPath}/js/loader.js"></script>
    <link href="https://fonts.googleapis.com/css?family=Lato|Nunito|Open+Sans|Oxygen|Poppins|Roboto" rel="stylesheet">
    <title>MovieRating</title>
</head>
<body>
<div class="container-fluid container-wrapper">
    <div class="loader"></div>
    <div class="row content-wrapper">
        <div class="col-sm-2 col__padding_0">
            <c:choose>
                <c:when test="${userRole eq 'admin'}">
                    <c:import url="../template/admin-navside-menu.jsp"/>
                </c:when>
                <c:otherwise>
                    <c:import url="../template/user-navside-menu.jsp"/>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="col-sm-10 col__padding_0">

            <div class="row row__margin_0 top-nav__wrapper">
                <c:import url="../template/header.jsp"/>
                <div class="col-sm-12 <%--col__padding_0--%> inner-page__wrapper">
                    <div class="row row__margin_0">
                        <div class="col-sm-12">
                            <h1><fmt:message bundle="${loc}" key="Participants"/></h1>
                        </div>
                    </div>
                    <div class="row row__margin_0">
                        <div class="col-sm-12">
                            <div class="card">
                                <ul class="nav nav-tabs" role="tablist">
                                    <c:if test="${userRole eq 'admin'}">
                                        <li class="active">
                                            <a href="Controller?command=redirect&redirectPage=addParticipantPage"
                                               role="tab">
                                                <fmt:message bundle="${loc}" key="add.participant.page"/>
                                            </a>
                                        </li>
                                        <li class="">
                                            <a href="Controller?command=show-all-actors" role="tab">
                                                <fmt:message bundle="${loc}" key="ACTORS"/>
                                            </a>
                                        </li>
                                    </c:if>
                                    <c:if test="${userRole ne 'admin'}">
                                        <li class="active">
                                            <a href="Controller?command=show-all-actors" role="tab">
                                                <fmt:message bundle="${loc}" key="ACTORS"/>
                                            </a>
                                        </li>
                                    </c:if>
                                    <li class="">
                                        <a href="Controller?command=show-other-participants" role="tab">
                                            <fmt:message bundle="${loc}" key="WRITERS"/>
                                        </a>
                                    </li>
                                    <li class="">
                                        <a href="Controller?command=show-other-participants" role="tab">
                                            <fmt:message bundle="${loc}" key="DIRECTORS"/>
                                        </a>
                                    </li>
                                </ul>
                                <!-- Tab panes -->
                                <div class="tab-content">
                                    <div role="tabpanel" class="tab-pane active" id="add-participant-tab">
                                        <c:forEach var="participant" items="${requestScope.participants}"
                                                   varStatus="rank">
                                            <div class="row__margin_0">
                                                <div class="panel panel-default">
                                                    <div class="panel-heading">
                                                        <a href="Controller?command=show-participant-page&participantId=${participant.key.id}">
                                                                ${participant.key.name}
                                                                ${participant.key.surname}
                                                        </a>
                                                    </div>
                                                    <div class="panel-body row__margin_0">
                                                        <div class="col-sm-2 col__padding_0">
                                                            <img class="img-responsive participant-photo"
                                                                 src="/${participant.key.photoURL}" width="180"
                                                                 height="270"
                                                                 alt="${participant.key.name} ${participant.key.surname}">
                                                        </div>
                                                        <div class="col-sm-5">
                                                            <ul class="list-group">
                                                                <li class="list-group-item list-group-item-success">
                                                                    <fmt:message bundle="${loc}"
                                                                                 key="Personal.information"/>
                                                                </li>
                                                                <c:choose>
                                                                    <c:when test="${not empty participant.key.country.name}">
                                                                        <li class="list-group-item">
                                                                            <fmt:message bundle="${loc}"
                                                                                         key="Country"/>:
                                                                                ${participant.key.country.name}</li>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <li class="list-group-item">
                                                                            <fmt:message bundle="${loc}"
                                                                                         key="Country"/>:
                                                                            <fmt:message bundle="${loc}"
                                                                                         key="no.information"/>
                                                                        </li>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                <c:choose>
                                                                    <c:when test="${not empty participant.key.birthDate}">
                                                                        <li class="list-group-item">
                                                                            <fmt:message bundle="${loc}"
                                                                                         key="Birthdate"/>:
                                                                                ${participant.key.birthDate}</li>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <li class="list-group-item">
                                                                            <fmt:message bundle="${loc}"
                                                                                         key="Birthdate"/>:
                                                                            <fmt:message bundle="${loc}"
                                                                                         key="no.information"/>
                                                                        </li>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                <c:choose>
                                                                    <c:when test="${not empty participant.key.amountOfMovies}">

                                                                        <li class="list-group-item">
                                                                            <fmt:message bundle="${loc}"
                                                                                         key="Amount.of.movies"/>:
                                                                            <span class="badge">${participant.key.amountOfMovies}</span>
                                                                        </li>
                                                                    </c:when>
                                                                    <c:otherwise>

                                                                        <li class="list-group-item">
                                                                            <fmt:message bundle="${loc}"
                                                                                         key="country"/>:
                                                                            <fmt:message bundle="${loc}"
                                                                                         key="no.information"/>
                                                                        </li>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </ul>
                                                        </div>
                                                        <div class="col-sm-5">
                                                            <ul class="list-group">
                                                                <c:choose>
                                                                    <c:when test="${not empty participant.value}">
                                                                        <li class="list-group-item list-group-item-success">
                                                                            <fmt:message bundle="${loc}"
                                                                                         key="TOP.5.Movies"/>
                                                                        </li>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <li class="list-group-item list-group-item-success">
                                                                            <fmt:message bundle="${loc}"
                                                                                         key="No.movies"/>
                                                                        </li>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                <c:forEach items="${participant.value}" var="movie"
                                                                           varStatus="loop">
                                                                    <li class="list-group-item">
                                                                        <a href="Controller?command=show-movie-page&movieId=${movie.id}">${loop.count}. ${movie.title}</a>
                                                                    </li>
                                                                </c:forEach>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>


                                        <div class="row__margin_0">
                                            <pgn:paginate uri="Controller?command=show-all-actors"
                                                          currentPage="${curPageNumber}"
                                                          pageCount="3"
                                                          next="&raquo;" previous="&laquo;"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <c:import url="/WEB-INF/jsp/template/footer.jsp"/>
</div>
</body>
</html>

