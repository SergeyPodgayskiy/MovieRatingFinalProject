<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language"
       value="${cookie.userLanguage.value != null ? cookie.userLanguage.value : sessionScope.defaultLanguage}"/>
<c:set var="userRole" value="${cookie.role.value != null ? cookie.role.value : sessionScope.role}"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="localization" var="loc"/>
<div class="nav-side-menu">
    <div class="logo logo-sidebar">
        <c:choose>
            <c:when test="${userRole eq 'admin'}">
                <a href="Controller?command=show-admin-page">
                    MovieRating
                </a>
            </c:when>
            <c:otherwise>
                <a href="Controller?command=show-welcome-page">
                    MovieRating
                </a>
            </c:otherwise>
        </c:choose>
    </div>
    <i class="fa fa-bars fa-2x toggle-btn" data-toggle="collapse" data-target="#menu-content"></i>
    <div class="user-panel">
        <div class="user-panel-head">
            <c:choose>
                <c:when test="${sessionScope.get('user') !=null}">
                    <div class="user-panel-side">
                        <a href="">
                            <i class="fa fa-user fa-lg"></i> ${user.login}
                        </a>
                        <a class="pull-right" data-toggle="collapse" data-target="#user-panel__menu">
                            <i class="fa fa-chevron-down "></i>
                        </a>
                    </div>
                    <div class="user-panel-menu collapse" id="user-panel__menu">
                        <a href="Controller?command=log-out">
                            <i class="fa fa-sign-out fa-lg"></i>
                            <fmt:message bundle="${loc}" key="log.out"/>
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <a href="#" data-toggle="modal" data-target="#log-in-modal">
                        <i class="fa fa-sign-in fa-lg"></i> <fmt:message bundle="${loc}" key="sign.in"/>
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <div class="menu-list">
        <ul id="menu-content" class="menu-content collapse out">
            <li class="target">
                <a href="Controller?command=show-top-movies"><i class="fa fa-film fa-fw"></i>
                    <fmt:message bundle="${loc}" key="movies"/> <%--<span class="arrow"></span>--%>
                </a>
            </li>
            <%--<ul class="sub-menu collapse" id="products">--%>
            <%--<li><a href="#">TOP Movies</a></li>--%>
            <%--<li><a href="#">New Movies</a></li>--%>
            <%--</ul>--%>

            <li data-toggle="collapse" data-target="#service" class="collapsed target">
                <a href="#"><i class="fa fa-language fa-lg"></i>
                    <fmt:message bundle="${loc}" key="genres"/>
                    <%--<span class="arrow"></span>--%>
                </a>
            </li>
            <li data-toggle="collapse" data-target="#new" class="collapsed">
                <a href="#"><i class="fa  fa-male fa-fw"></i>
                    <fmt:message bundle="${loc}" key="actors"/> <%--<span class="arrow"></span>--%></a>
            </li>
        </ul>
        <div class="language">
        </div>
    </div>
</div>
<%--login modal--%>
<div class="modal fade" id="log-in-modal" role="dialog">
    <div class="modal-dialog new-style-modal">
        <!-- Modal content-->
        <div class="modal-content new-style-modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title"><fmt:message bundle="${loc}" key="have.an.account.sign.in"/></h4>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/Controller" method="post">
                    <input type="hidden" name="command" value="log-in">
                    <div class="form-group">
                        <div class="input-group">
                            <span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
                            <input type="text" class="form-control new-input-style" id="username"
                                   placeholder="<fmt:message bundle="${loc}" key="username"/>" name="login" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group">
                            <span class="input-group-addon new-input-style"><i class="fa fa-key fa-fw"></i></span>
                            <input type="password" class="form-control new-input-style" id="password"
                                   placeholder="<fmt:message bundle="${loc}" key="password"/>"
                                   name="password" required>
                        </div>
                        <a class="pull-right forgot-psw" href="#"><fmt:message bundle="${loc}"
                                                                               key="forgot.password"/></a>
                    </div>
                    <input type="hidden" name="previousPageQuery" value="${pageContext.request.queryString}">
                    <button type="submit" class="btn btn-primary btn-block active btn-new-style "><fmt:message
                            bundle="${loc}" key="sign.in"/></button>
                </form>
            </div>
            <div class="modal-footer">
                <div class="text-center"><fmt:message bundle="${loc}" key="new.to.movierating"/>
                    <a href="Controller?command=redirect&redirectPage=registration&previousPageQuery=${pageContext.request.queryString}">
                        <fmt:message bundle="${loc}" key="register"/>
                        <i class="fa fa-sign-in fa-lg" aria-hidden="true"></i>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

