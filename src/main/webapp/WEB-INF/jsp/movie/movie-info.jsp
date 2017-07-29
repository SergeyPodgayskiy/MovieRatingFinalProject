<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language"
       value="${cookie.userLanguage.value != null ? cookie.userLanguage.value : sessionScope.defaultLanguage}"/>
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
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/font-awesome.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/top-navbar.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/sidebar-menu.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/login-modal-style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/movie-info-style.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/css/star-rating.min.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/js/star-rating.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/movie-info.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/loader.js"></script>
    <link href="https://fonts.googleapis.com/css?family=Lato|Nunito|Open+Sans|Oxygen|Poppins|Roboto" rel="stylesheet">
    <title>MovieRating</title>
</head>
<body>
<div class="loader"></div>
<div class="container-fluid container-wrapper">
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
                        <div class="col-sm-12 content-header">
                            <h1>
                                ${movie.title}
                                    <c:if test="${userRole eq 'admin'}">
                                        <a href="Controller?command=show-edit-movie-page&movieId=${movie.id}&previousPageQuery=${pageContext.request.queryString}"
                                            class="btn btn-default edit-movie" href="#">
                                            <i class="fa fa-pencil fa-lg"></i>
                                            <fmt:message bundle="${loc}" key="edit"/>
                                        </a>
                                        <a class="btn btn-danger delete-movie" >
                                            <i class="fa fa-times fa-lg"></i>
                                            <fmt:message bundle="${loc}" key="delete"/>
                                        </a>
                                    </c:if>
                            </h1>
                        </div>
                    </div>
                    <hr class="movie-title-divider">
                    <div class="row row__margin_0 inner-content">
                        <div class="col-sm-12">
                            <div class="col-sm-3 poster-side">
                                <img class="img-responsive movie-poster" src="/${movie.posterURL}" width="255"
                                     height="350" alt="${movie.title}">
                                <div class="row__margin_0 movie-status-side">
                                    <div class="row">
                                        <div class="col-sm-8 col-sm-offset-2 col-xs-10 col-xs-offset-2">
                                     <span>
                                         <a href=""><i class="fa fa-heart-o fa-3x"></i></a>
                                     </span>
                                      <span>
                                          <a href=""><i class="fa fa-eye fa-3x"></i></a>
                                      </span>
                                      <span>
                                          <a href=""><i class="fa fa-bookmark-o fa-3x"></i></a>
                                      </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 movie-info-wrapper">
                                <table class="table table-responsive movie-table">
                                    <tbody>
                                    <tr>
                                        <td><fmt:message bundle="${loc}" key="year"/></td>
                                        <%--<td><fmt:formatDate pattern="yyyy" value="${movie.releaseYear}"/></td>--%>
                                        <td>${movie.releaseYear}</td>
                                    </tr>
                                    <tr>
                                        <td><fmt:message bundle="${loc}" key="country"/></td>
                                        <td>
                                            <c:forEach var="contry" items="${requestScope.countries}" varStatus="rank">
                                                ${contry.name}
                                                ${!rank.last ? ', ' : ''}
                                            </c:forEach>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><fmt:message bundle="${loc}" key="genre"/></td>
                                        <td>
                                            <c:forEach var="genre" items="${requestScope.genres}" varStatus="rank">
                                                ${genre.name}
                                                ${!rank.last ? ', ' : ''}
                                            </c:forEach>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><fmt:message bundle="${loc}" key="slogan"/></td>
                                        <td class="text-muted">«${movie.slogan}»</td>
                                    </tr>
                                    <tr>
                                        <td><fmt:message bundle="${loc}" key="director"/></td>
                                        <td>
                                            <c:forEach var="participant" items="${requestScope.participants}"
                                                       varStatus="rank">
                                                <c:forEach items="${participant.value}" var="role" varStatus="loop">
                                                    <c:if test="${role.name eq 'director'}">
                                                        ${participant.key.name}
                                                        ${participant.key.surname}
                                                    </c:if>
                                                </c:forEach>
                                            </c:forEach>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><fmt:message bundle="${loc}" key="writer"/></td>
                                        <td>
                                            <c:forEach var="participant" items="${requestScope.participants}"
                                                       varStatus="rank">
                                                <c:forEach items="${participant.value}" var="role" varStatus="loop">
                                                    <c:if test="${role.name eq 'writer'}">
                                                        ${participant.key.name}
                                                        ${participant.key.surname}
                                                    </c:if>
                                                </c:forEach>
                                            </c:forEach>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><fmt:message bundle="${loc}" key="age.limit"/></td>
                                        <td>${movie.ageLimit}</td>
                                    </tr>
                                    <tr>
                                        <td><fmt:message bundle="${loc}" key="duration"/></td>
                                        <td>
                                            <fmt:formatDate pattern="hh:mm" value="${movie.duration}"/></td>
                                    </tr>
                                    </tbody>

                                </table>

                                <div class="jumbotron" id="rating-section">
                                    <div id="message"></div>
                                    <div id="movie-id">${movie.id}</div>
                                    <%--display : none--%>
                                    <div class="">
                                        <div class="movie-rating">
                                            <label for="input-movie-rating" class="control-label">
                                                <fmt:message bundle="${loc}" key="rating"/></label>
                                            <div class="movie-input-wrapper">
                                                <input id="input-movie-rating" name="input-movie-rating"
                                                       value="${movie.rating}" class="rating-loading">
                                            </div>
                                        </div>
                                        <div class="users-movie-rating">
                                            <label for="input-user-movie-rating" class="control-label">
                                                <fmt:message bundle="${loc}" key="your.rating"/></label>
                                            <input id="input-user-movie-rating" name="input-user-movie-rating"
                                                   value="${userMovieRating.mark}" class="rating-loading">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-3 main-actors-side">
                                <table class="table table-responsive movie-table table-condensed">
                                    <thead>
                                    <tr>
                                        <th><fmt:message bundle="${loc}" key="actors"/></th>
                                        <th></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="participant" items="${requestScope.participants}"
                                               varStatus="rank">
                                        <c:forEach items="${participant.value}" var="role" varStatus="loop">
                                            <c:if test="${role.name eq 'actor'}">
                                                <tr>
                                                    <td>
                                                        <a href="#">
                                                            <img src="/${participant.key.photoURL}" width="30"
                                                                 height="40">
                                                        </a>
                                                    </td>
                                                    <td>
                                                        <a href="#">
                                                                ${participant.key.name}
                                                                ${participant.key.surname}
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                    </c:forEach>
                                    </tbody>
                                    <tr class="table-footer">
                                        <td></td>
                                        <td class="text-right">
                                            <a href="#"><fmt:message bundle="${loc}" key="view.all"/></a>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Delete Modal -->
<div class="modal fade" id="delete-movie-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title"><fmt:message bundle="${loc}" key="confirmation"/></h4>
            </div>
            <div class="modal-body">

                <fmt:message bundle="${loc}" key="are.you.sure.you.want.to.delete.movie"/>
            </div>
            <div class="modal-footer">
                <button id="delete-movie-btn" type="button" class="btn btn-primary">
                    <fmt:message bundle="${loc}" key="delete"/></button>
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    <fmt:message bundle="${loc}" key="cancel"/>
                </button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
