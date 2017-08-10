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
    <link href="${pageContext.request.contextPath}/css/participant-info-style.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/participant-info.js" type="text/javascript"></script>
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
                        <div id="participant-id">${requestScope.participant.id}</div>
                        <div class="col-sm-12 content-header">
                            <h1>
                                ${requestScope.participant.name} ${requestScope.participant.surname}
                                <c:if test="${userRole eq 'admin'}">
                                    <a href="Controller?command=show-edit-participant-page&participantId=${participant.id}"
                                       class="btn btn-default edit-participant" href="#">
                                        <i class="fa fa-pencil fa-lg"></i>
                                        <fmt:message bundle="${loc}" key="edit"/>
                                    </a>
                                    <a class="btn btn-danger delete-participant">
                                        <i class="fa fa-times fa-lg"></i>
                                        <fmt:message bundle="${loc}" key="delete"/>
                                    </a>
                                </c:if>
                            </h1>
                        </div>
                    </div>
                    <hr class="participant-name-divider">
                    <div class="row row__margin_0 inner-content">
                        <div class="col-sm-12">
                            <div class="col-sm-3 photo-side">
                                <img class="img-responsive participant-photo" src="/${requestScope.participant.photoURL}" width="255"
                                     height="350" alt="${requestScope.participant.name} ${requestScope.participant.surname}">

                            </div>
                            <div class="col-sm-6 participant-info-wrapper">
                                <table class="table table-responsive participant-table">
                                    <tbody>
                                    <tr>
                                        <td><fmt:message bundle="${loc}" key="name"/></td>
                                        <td>
                                            ${requestScope.participant.name}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><fmt:message bundle="${loc}" key="surname"/></td>
                                        <td>
                                            ${requestScope.participant.surname}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><fmt:message bundle="${loc}" key="country"/></td>
                                        <td>
                                            ${requestScope.country.name}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><fmt:message bundle="${loc}" key="birthdate"/></td>
                                        <%--<td><fmt:formatDate pattern="yyyy" value="${movie.releaseYear}"/></td>--%>
                                        <td>${requestScope.participant.birthDate}</td>
                                    </tr>
                                    <tr>
                                        <td><fmt:message bundle="${loc}" key="role"/></td>
                                        <td><c:forEach var="movierole" items="${requestScope.movieRoles}"
                                                       varStatus="rank">
                                            ${movierole.name}
                                            ${!rank.last ? ', ' : ''}
                                        </c:forEach>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>

                                <div class="jumbotron">
                                    <div id="message"></div>
                                    <div class="">
                                        <fmt:message bundle="${loc}" key="amount.of.movies"/>
                                        ${requestScope.participant.amountOfMovies}
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

<!-- Delete Modal -->
<div class="modal fade" id="delete-participant-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title"><fmt:message bundle="${loc}" key="confirmation"/></h4>
            </div>
            <div class="modal-body">

                <fmt:message bundle="${loc}" key="are.you.sure.you.want.to.delete.participant"/>
            </div>
            <div class="modal-footer">
                <button id="delete-participant-btn" type="button" class="btn btn-primary">
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
