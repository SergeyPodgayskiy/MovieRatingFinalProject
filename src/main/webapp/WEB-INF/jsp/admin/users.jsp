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
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/bootstrap-switch.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/font-awesome.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/top-navbar.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/sidebar-menu.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/login-modal-style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/datatables.min.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/users-table.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/datatables.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/table.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap-switch.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/users-table.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/loader.js"></script>
    <link href="https://fonts.googleapis.com/css?family=Lato|Nunito|Open+Sans|Oxygen|Poppins|Roboto" rel="stylesheet">
    <title>MovieRating</title>
</head>
<body>
<div class="loader"></div>
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
                        <div id="admin-id">${sessionScope.userId}</div>
                        <div class="col-sm-12">
                            <h1><fmt:message bundle="${loc}" key="users"/></h1>
                        </div>
                    </div>
                    <div class="row row__margin_0">
                        <div class="col-sm-12">
                            <div class="card">
                                <div class="table-responsive  table-wrapper">
                                    <table id="users-table" class="table table-hover top-movie-table">
                                        <thead>
                                        <tr id="header-row">
                                            <th>#</th>
                                            <th class="no-sort "></th>
                                            <th><fmt:message bundle="${loc}" key="login"/></th>
                                            <th><fmt:message bundle="${loc}" key="full.name"/></th>
                                            <th><fmt:message bundle="${loc}" key="register.date"/></th>
                                            <th><fmt:message bundle="${loc}" key="admin"/></th>
                                            <th><fmt:message bundle="${loc}" key="ban.status"/></th>
                                            <th><fmt:message bundle="${loc}" key="delete.date"/></th>
                                            <th class="no-sort"></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="user" items="${requestScope.users}"
                                                   varStatus="loop">
                                            <%--display:none--%>
                                            <%--<div id="ban-status">${user.isBanned()}</div> &lt;%&ndash;display:none&ndash;%&gt;--%>
                                            <%--<div id="admin-status">${user.isAdmin()}</div> &lt;%&ndash;display:none&ndash;%&gt;--%>
                                            <tr id="${loop.count}">
                                                <td id="number-column" class="">
                                                        ${loop.count}
                                                    <div id="user-id">${user.id}</div>
                                                </td>
                                                <td id="avatar-column" class="">
                                                    <a href="">
                                                        <img class="img" src="./${user.photoURL}">
                                                    </a>
                                                </td>
                                                <td id="login-column" class="">
                                                    <a href="Controller?command=show-user&userId=${user.id}">
                                                            ${user.login}
                                                    </a>
                                                </td>
                                                <td id="full-name-column" class="">
                                                    <a href="Controller?command=show-user&userId=${user.id}">
                                                            ${user.fullName}
                                                    </a>
                                                </td>
                                                <td id="register-date-column">
                                                        ${user.registerDate}
                                                </td>
                                                <td id="is-admin-column" class="">
                                                    <div id="admin-status">${user.isAdmin()}</div>
                                                        <%--display:none--%>
                                                    <input id="admin-switch" type="checkbox" name="admin-switch">
                                                </td>
                                                <td id="is-banned-column" class="">
                                                    <div id="ban-status">${user.isBanned()}</div>
                                                        <%--display:none--%>
                                                    <input id="ban-switch" type="checkbox" name="ban-switch">
                                                </td>
                                                <td id="deleted-at-column" class="">
                                                    <c:choose>
                                                        <c:when test="${user.deletedAt != null}">
                                                            ${user.deletedAt}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <fmt:message bundle="${loc}" key="active"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td id="delete-button-column" class="">
                                                    <button  id="btn" name="delete-user-btn" type="button" class="btn btn-primary btn-sm">
                                                        <fmt:message bundle="${loc}" key="delete"/>
                                                    </button>
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
<!-- Admin Status Modal -->
<div class="modal fade" id="admin-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Confirmation</h4>
            </div>
            <div class="modal-body">
                Are you sure you want to change admin status?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary">Confirm</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
            </div>
        </div>
    </div>
</div>
<!-- Ban Status Modal -->
<div class="modal fade" id="ban-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Confirmation</h4>
            </div>
            <div class="modal-body">
                Are you sure you want to change ban status?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary">Confirm</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
            </div>
        </div>
    </div>
    <c:import url="/WEB-INF/jsp/template/footer.jsp"/>
</div>
<!-- Delete Modal -->
<div class="modal fade" id="delete-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Confirmation</h4>
            </div>
            <div class="modal-body">
                Are you sure you want to delete this user?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary">Confirm</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>

