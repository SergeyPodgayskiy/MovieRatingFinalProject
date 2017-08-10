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
    <link href="${pageContext.request.contextPath}/css/select2.min.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/admin-add-edit-movie-page.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/select2.full.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/add-and-edit-movie-form.js"></script>
    <script src="${pageContext.request.contextPath}/js/loader.js"></script>
    <script src="http://malsup.github.com/jquery.form.js"></script>
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
                            <h1><fmt:message bundle="${loc}" key="movies"/></h1>
                        </div>
                    </div>
                    <div class="row row__margin_0">
                        <div class="col-sm-12">
                            <div class="card">
                                <ul class="nav nav-tabs" role="tablist">
                                    <li class="active">
                                        <a href="Controller?command=redirect&redirectPage=addMoviePage"
                                           role="tab">
                                            <fmt:message bundle="${loc}" key="add.movie.page"/>
                                        </a>
                                    </li>
                                    <li class="">
                                        <a href="Controller?command=show-top-movies" role="tab">
                                            <fmt:message bundle="${loc}" key="top.50.movies"/>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="Controller?command=show-most-discussed-movies" role="tab">
                                            <fmt:message bundle="${loc}" key="most.discussed"/>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="Controller?command=show-movie-navigator" role="tab">
                                            <fmt:message bundle="${loc}" key="movie.navigator"/>
                                        </a>
                                    </li>
                                </ul>
                                <!-- Tab panes -->
                                <div class="tab-content">
                                    <div role="tabpanel" class="tab-pane active" id="add-movie-tab">
                                        <div class="row__margin_0">
                                            <div class="col-md-7">
                                                <div class="panel panel-default">
                                                    <div id="movie-id">${requestScope.movieId}</div>
                                                    <div class="panel-heading"><fmt:message bundle="${loc}"
                                                                                            key="movie.information"/></div>
                                                    <div class="panel-body">
                                                        <div class="frame-nf-step1">
                                                            <div class="form-group">
                                                                <label for="release-year"><fmt:message bundle="${loc}"
                                                                                                       key="Year"/></label>
                                                                <input type="text" class="form-control"
                                                                       id="release-year" required>
                                                            </div>

                                                            <div class="form-group">
                                                                <label for="age-limit" class=""><fmt:message
                                                                        bundle="${loc}" key="Age.limit"/></label>
                                                                <select id="age-limit" class="age-limit"
                                                                        style="width: 100%" required>
                                                                </select>
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="duration"><fmt:message bundle="${loc}"
                                                                                                   key="Duration"/></label>
                                                                <input type="text" class="form-control" id="duration"
                                                                       required>
                                                            </div>
                                                            <div class="row row__margin_0">
                                                                <div class="col-sm-5 col__padding_0">
                                                                    <button id="accept-fields-nf" type="button"
                                                                            class="btn btn-primary">
                                                                        <fmt:message bundle="${loc}" key="save"/>
                                                                    </button>
                                                                    <button id="edit-fields-nf" type="button"
                                                                            class="btn btn-info">
                                                                        <fmt:message bundle="${loc}" key="edit"/>
                                                                    </button>
                                                                    <button id="accept-editing-fields-nf" type="button"
                                                                            class="btn btn-primary">
                                                                        <fmt:message bundle="${loc}"
                                                                                     key="save.editing"/>
                                                                    </button>
                                                                    <button id="clear-fields-nf" type="button"
                                                                            class="btn btn-default">
                                                                        <fmt:message bundle="${loc}" key="clear"/>
                                                                    </button>
                                                                </div>
                                                                <div class="col-sm-7 col__padding_0">
                                                                    <div class="message-nf"></div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="frame-step2">
                                                            <hr>
                                                            <div class="form-group">
                                                                <label for="country" class=""><fmt:message
                                                                        bundle="${loc}" key="Country"/></label>
                                                                <select id="country" class="country"
                                                                        multiple="multiple" style="width: 100%">
                                                                </select>
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="genre" class=""><fmt:message bundle="${loc}"
                                                                                                         key="Genres"/></label>
                                                                <select id="genre" class="genre"
                                                                        multiple="multiple" style="width: 100%">
                                                                </select>
                                                            </div>
                                                            <hr>
                                                        </div>
                                                        <div class="panel panel-default">
                                                            <div class="panel-heading frame-step-3">
                                                                <div class="form-group">
                                                                    <label for="language" class=""><fmt:message
                                                                            bundle="${loc}"
                                                                            key="content.language"/></label>
                                                                    <select id="language" class="language"
                                                                            style="width: 100%">
                                                                    </select>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="name-movie"><fmt:message bundle="${loc}"
                                                                                                         key="Title"/></label>
                                                                    <input id="name-movie" type="text"
                                                                           class="form-control" required>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="slogan"><fmt:message bundle="${loc}"
                                                                                                     key="Slogan"/></label>
                                                            <textarea id="slogan" class="form-control"
                                                                      rows="1"></textarea>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="description"><fmt:message
                                                                            bundle="${loc}" key="Description"/></label>
                                                            <textarea id="description" class="form-control"
                                                                      rows="4"></textarea>
                                                                </div>
                                                                <div class="row row__margin_0">
                                                                    <div class="col-sm-5 col__padding_0">
                                                                        <button id="accept-fields-lf" type="button"
                                                                                class="btn btn-primary">
                                                                            <fmt:message bundle="${loc}" key="save"/>
                                                                        </button>
                                                                        <button id="edit-fields-lf" type="button"
                                                                                class="btn btn-info">
                                                                            <fmt:message bundle="${loc}" key="edit"/>
                                                                        </button>
                                                                        <button id="accept-editing-fields-lf"
                                                                                type="button"
                                                                                class="btn btn-primary">
                                                                            <fmt:message bundle="${loc}"
                                                                                         key="save.editing"/>
                                                                        </button>
                                                                        <button id="clear-fields-lf" type="button"
                                                                                class="btn btn-default">
                                                                            <fmt:message bundle="${loc}" key="clear"/>
                                                                        </button>
                                                                    </div>
                                                                    <div class="col-sm-7 col__padding_0">
                                                                        <div class="message-lf"></div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                    </div>
                                                </div>
                                            </div>
                                            <div id="poster-adding-wrapper" class="col-md-5">
                                                <div id="add-poster-movie" class="panel panel-default">
                                                    <div class="panel-heading"><fmt:message bundle="${loc}"
                                                                                            key="Poster"/></div>
                                                    <div class="panel-body">
                                                        <div>
                                                            <img class="img-responsive movie-poster">
                                                        </div>
                                                    </div>
                                                    <div class="panel-footer">
                                                        <div id="poster-form-wrapper" class="row row__margin_0">
                                                        </div>
                                                        <button id="upload-poster" type="button"
                                                                class="btn btn-primary">
                                                            <fmt:message bundle="${loc}" key="add.poster"/>
                                                        </button>
                                                    </div>
                                                </div>
                                                <div class="panel panel-default">
                                                    <div class="panel-heading"><fmt:message bundle="${loc}"
                                                                                            key="Participants"/></div>
                                                    <div class="panel-body">
                                                        <div class="form-group">
                                                            <label for="actors" class=""> <fmt:message bundle="${loc}"
                                                                                                       key="actors"/></label>
                                                            <select id="actors" class="actors "
                                                                    multiple="multiple" style="width: 100%">
                                                            </select>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="directors" class=""> <fmt:message
                                                                    bundle="${loc}" key="Director"/></label>
                                                            <select id="directors" class="directors "
                                                                    multiple="multiple" style="width: 100%">
                                                            </select>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="writers" class=""> <fmt:message bundle="${loc}"
                                                                                                        key="Writer"/></label>
                                                            <select id="writers" class="writers "
                                                                    multiple="multiple" style="width: 100%">
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <%-- <div class="panel-footer">
                                                         <button id="accept-fields-participant" type="button"
                                                                 class="btn btn-primary">
                                                             Accept
                                                         </button>
                                                         <button id="edit-fields-participant" type="button"
                                                                 class="btn btn-info">
                                                             Edit
                                                         </button>
                                                         <button id="clear-fields-participant" type="button"
                                                                 class="btn btn-default">
                                                             Clear
                                                         </button>
                                                     </div>--%>
                                                </div>
                                                <div class="main-buttons-wrapper row__margin_0">
                                                    <div class="col-sm-5 col__padding_0">
                                                        <button id="save-movie-btn" class="btn btn-success btn-lg"
                                                                type="button">
                                                            <fmt:message bundle="${loc}" key="save.all"/>
                                                        </button>
                                                    </div>
                                                    <div class="col-sm-5 col-sm-offset-2 col__padding_0">
                                                        <button id="decline-add-movie-btn" class="btn btn-danger btn-lg"
                                                                type="button">
                                                            <fmt:message bundle="${loc}" key="delete.movie"/>
                                                        </button>
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
        </div>
    </div>
    <c:import url="/WEB-INF/jsp/template/footer.jsp"/>
</div>
<!-- Save movie Modal -->
<div class="modal fade" id="save-movie-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title"><fmt:message bundle="${loc}" key="confirmation"/></h4>
            </div>
            <div class="modal-body">
                <fmt:message bundle="${loc}" key="information.saved.select.following.action"/>
            </div>
            <div class="modal-footer">
                <button id="go-to-the-movie-page" type="button" class="btn btn-primary btn-block">
                    <fmt:message bundle="${loc}" key="go.to.the.movie.page"/></button>
                <a href="Controller?command=redirect&redirectPage=addMoviePage"
                   class="btn btn-block">
                    <fmt:message bundle="${loc}" key="add.another.movie"/>
                </a>
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

