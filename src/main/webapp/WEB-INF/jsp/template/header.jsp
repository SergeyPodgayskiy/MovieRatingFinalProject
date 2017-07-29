<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${cookie.userLanguage.value != null ? cookie.userLanguage.value : sessionScope.defaultLanguage}"/>
<c:set var="userRole" value="${cookie.role.value != null ? cookie.role.value : sessionScope.role}"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="localization" var="loc"/>

<nav class="<%--navbar navbar-inverse--%> new-style-navbar">
    <div class="row row__margin_0 valign">
        <div class="col-md-7 col-md-offset-2 col-xs-8 col-xs-offset-0">
            <div id="searching_container">
                <div class="input-group stylish-input-group">
                    <input type="text" class="form-control" placeholder="<fmt:message bundle="${loc}" key="search"/>">
                    <span class="input-group-addon" id="search-input-style">
                        <button type="submit">
                            <span class="glyphicon glyphicon-search"></span>
                        </button>
                    </span>
                </div>
            </div>
        </div>
        <div class="col-md-1 col-md-offset-2 col-xs-4 col-xs-offset-0 text-center">
            <div id="language-wrapper">
                <c:if test="${language eq 'ru_RU'}">
                    <a id="selected-language" href="Controller?command=change-language&userLanguage=ru_RU
                &previousPageQuery=${pageContext.request.queryString}">
                        <fmt:message bundle="${loc}" key="rus"/>
                    </a>
                    |
                    <a href="Controller?command=change-language&userLanguage=en_EN
                &previousPageQuery=${pageContext.request.queryString}">
                        <fmt:message bundle="${loc}" key="eng"/>
                    </a>
                </c:if>

                <c:if test="${language eq 'en_EN' or language == null}">
                    <a href="Controller?command=change-language&userLanguage=ru_RU&previousPageQuery=${pageContext.request.queryString}">
                        <fmt:message bundle="${loc}" key="rus"/></a>
                    |
                    <a id="selected-language" href="Controller?command=change-language&userLanguage=en_EN&previousPageQuery=${pageContext.request.queryString}">
                        <fmt:message bundle="${loc}" key="eng"/></a>
                </c:if>
            </div>
        </div>
    </div>
</nav>



