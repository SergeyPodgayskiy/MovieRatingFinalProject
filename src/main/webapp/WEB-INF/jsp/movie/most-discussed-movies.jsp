<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentLanguage" value="${cookie.userLanguage.value != null ? cookie.userLanguage.value : sessionScope.defaultLanguage}"/>
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
            <c:import url="../template/user-navside-menu.jsp"/>
        </div>
        <div class="col-sm-10 col__padding_0">

            <div class="row row__margin_0 top-nav__wrapper">
                <c:import url="../template/header.jsp"/>
                <div class="col-sm-12 <%--col__padding_0--%> inner-page__wrapper">
                    <div class="row row__margin_0">
                        <div class="col-sm-12">
                            <h1><fmt:message bundle="${loc}" key="movies"/></h1>
                        </div>
                    </div>
                    <div class="row row__margin_0">
                        <div class="col-sm-12">
                            <div class="card">
                                <ul class="nav nav-tabs" role="tablist">
                                    <li>
                                        <a href="Controller?command=show-top-movies"  role="tab">
                                            <fmt:message bundle="${loc}" key="top.50.movies"/>
                                        </a>
                                    </li>
                                    <li class="active">
                                        <a href="Controller?command=show-most-discussed-movies"  role="tab" >
                                            <fmt:message bundle="${loc}" key="most.discussed"/>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="Controller?command=show-movie-navigator" role="tab" >
                                            <fmt:message bundle="${loc}" key="movie.navigator"/>
                                        </a>
                                    </li>
                                </ul>
                                <!-- Tab panes -->
                                <div class="tab-content">
                                    <div role="tabpanel" class="tab-pane active" id="most-discussed">
                                        <div class="table-responsive  table-wrapper">
                                            <table id="movies-table" class="table table-striped top-movie-table">
                                                <thead>
                                                <tr>
                                                    <th class="no-sort"></th>
                                                    <th><fmt:message bundle="${loc}" key="rank.and.title"/></th>
                                                    <th><fmt:message bundle="${loc}" key="rating"/></th>
                                                    <th><fmt:message bundle="${loc}" key="your.rating"/></th>
                                                    <th><fmt:message bundle="${loc}" key="reviews"/></th>
                                                    <th class="no-sort"></th>
                                                    <th class="no-sort"></th>
                                                    <th class="no-sort"></th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var="movie" items="${requestScope.movies}"
                                                           varStatus="rank">
                                                    <tr>
                                                        <td class="poster-column">
                                                            <a href="">
                                                                <img src="./${movie.posterURL}"  alt="${movie.title}">
                                                            </a>
                                                        </td>
                                                        <td class="title-column">
                                                                ${rank.count}.
                                                            <a href="Controller?command=show-movie-page&movieId=${movie.id}">
                                                                    ${movie.title}
                                                            </a>
                                                            (<fmt:formatDate pattern = "yyyy" value = "${movie.releaseYear}"/>)
                                                        </td>
                                                        <td class="rating-column">
                                                            <span>
                                                                <i class="fa fa-star fa-lg"></i>
                                                                   <fmt:formatNumber pattern="0.00" value="${movie.rating}"/>
                                                            </span>
                                                        </td>
                                                        <td class="your-rating-column">
                                                            <span>
                                                                <a href="">
                                                                <i class="fa fa-star-o fa-lg"></i>

                                                                    </a>
                                                            </span>
                                                        </td>
                                                        <td class="reviews-column">
                                                            <span>
                                                                <i class="fa fa-comments fa-lg"></i>
                                                                ${movie.amountOfReviews}
                                                            </span>
                                                        </td>
                                                        <td class="fav-column">
                                                            <span>
                                                                <a href="">
                                                                <i class="fa fa-heart-o fa-lg"></i>
                                                                </a>
                                                            </span>

                                                        </td>
                                                        <td class="watchlist-column">
                                                              <span>
                                                                  <a href="">
                                                                <i class="fa fa-eye fa-lg"></i>
                                                                  </a>
                                                            </span>
                                                        </td>
                                                        <td class="bookmark-column">
                                                            <span>
                                                                <a href="">
                                                                    <i class="fa fa-bookmark-o fa-lg"></i>
                                                                </a>
                                                            </span>
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
    </div>
</div>
</body>
</html>

