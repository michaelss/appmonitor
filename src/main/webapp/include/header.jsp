<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html class="no-js" lang="en">
<head>
	<meta charset="utf-8" />
	<meta http-equiv="x-ua-compatible" content="ie=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>Inventory</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/foundation.min.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/foundation-icons.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css" />
</head>
<body>

	<div class="top-bar" id="mainMenu" ng-controller="HeaderController">
		<div class="top-bar-left">
			<ul class="menu" data-responsive-menu="accordion">
				<li class="menu-text">Inventory</li>
				<li><a href="#">Servers and apps</a></li>
			</ul>
		</div>
		<div class="top-bar-right">
			<ul class="menu">
				<shiro:guest><li><a href="<c:url value="/login.jsp"/>" class="hollow button"></i>Login</a></li></shiro:guest>
 			</ul>
		</div>
	</div>