<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html class="no-js" lang="en" ng-app="inventory">
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
				<shiro:user><li><a href="#"><i class="fi-torso"></i>{{username}}</a></li></shiro:user>
				<shiro:user><li><a href="<c:url value="/logout"/>" class="hollow button">Logout</a></li></shiro:user>
				
				<shiro:guest><li><a href="<c:url value="/login.jsp"/>" class="hollow button"></i>Login</a></li></shiro:guest>
 			</ul>
		</div>
	</div>
	
	<div class="row column" ng-if="flash.getMessage()">
		<br>
		<div class="{{flash.getMessage().status}} callout" data-closable>
			<p>{{flash.getMessage().text}}</p>
		</div>
	</div>
	
	<div class="row column" ng-if="message">
		<br>
		<div class="{{message.status}} callout" data-closable>
			<p>{{message.text}}</p>
		</div>
	</div>