﻿<%--
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:eval expression="@applicationProps['application.version']" var="applicationVersion" />
<spring:eval expression="@applicationProps['application.revision']" var="applicationRevision" />
<spring:eval expression="@applicationProps['google.analytics.account.web']" var="googleAnalyticsAccount" />

<!doctype html>
<html lang="en" ng-app="shareTaskWeb">
	<!-- Application version: ${applicationVersion} -->
	<!-- Application revision: ${applicationRevision} -->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="description" content="">
		<meta name="keywords" content="">
		<meta name="author" content="">
		<title>ShareTa.sk</title>
		<link rel="shortcut icon" href="<c:url value="/resources-web-${applicationVersion}/favicon.ico" />" />
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-web-${applicationVersion}/css/bootswatch.min.css" />">
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-webapp-${applicationVersion}/css/font-awesome.min.css" />">
		<!--[if IE 7]>
			<link rel="stylesheet" type="text/css" href="<c:url value="/resources-webapp-${applicationVersion}/css/font-awesome-ie7.min.css" />">
		<![endif]-->
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-web-${applicationVersion}/css/sharetask.css" />">
	</head>
	<body ng-cloak>
		<!-- application menu -->
		<div id="app-menu">
			<div class="navbar navbar-inverse">
				<div class="navbar-inner" style="padding-left:0;">
					<div class="container" style="width:auto;">
						<img class="pull-left" src="<c:url value="/resources-web-${applicationVersion}/img/icon-white-small.png" />" width="40" height="40" hspace="5" />
						<span class="brand">ShareTa.sk</span>
						<ul class="nav">
							<li><a href="<c:url value="/" />"><i class="icon-home icon-white"></i> <spring:message code="menu.home" /></a></li>
							<li><a href="<c:url value="/register" />"><i class="icon-user icon-white"></i> <spring:message code="menu.registration" /></a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-full">
			<div class="panel-full-inside">
				<div class="brand-img">
					<div class="row-full">
						<div class="span8">
						</div>
						<div class="span4" style="width:330px;right:0;overflow:visible;position:absolute;">
							<div class="brand-login" ng-controller="AuthCtrl">
								<div ng-show="loaded">
									<div ng-show="loggedUser != null">
										<h4><spring:message code="msg.helloBack" arguments="{{loggedUser.name}}" /></h4><br />
										<a href="<c:url value="/webapp" />" class="btn btn-inverse"><spring:message code="app.button.open" /></a> <a ng-click="logout()" class="btn"><spring:message code="app.button.logout" /></a><br />
									</div>
									<div ng-show="loggedUser == null">
										<h4><spring:message code="account.question" /></h4><br />
										<a href="<c:url value="/register" />" class="btn btn-inverse"><spring:message code="account.create.button" /></a><br />
										<br /><br />
										<h4><spring:message code="login.title" /></h4><br />
										<form name="formLogin" novalidate class="css-form">
											<div class="alert alert-error" ng-class="{'hidden':!loginData || !loginData.result || loginData.result == 1 || loginData.result == 0}">
												<a class="close" ng-click="loginData.result = 0">&times;</a>
												Bad user name or password.
											</div>
											<input type="text" name="username" placeholder="<spring:message code="login.username.placeholder" />" ng-model="user.username" ui-keypress="{enter:'login()'}" required auto-fillable-field /><br />
											<input type="password" name="password" placeholder="<spring:message code="login.password.placeholder" />" ng-model="user.password" ui-keypress="{enter:'login()'}" required auto-fillable-field /><br />
											<button class="btn btn-inverse" ng-click="login()" ng-disabled="formLogin.$invalid || loginData.processing"><spring:message code="login.button.submit" /></button>
										</form>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div style="padding:20px 20px 50px 20px;">
			</div>
		</div>
		<div id="footer">
			<spring:message code="msg.changeLanguage" />: <a href="<c:url value="/?lang=en" />">english</a>, <a href="<c:url value="/?lang=cs" />">česky</a>
		</div>
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/vendor/jquery/jquery-1.9.1.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/vendor/jquery/jquery-ui.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/vendor/bootstrap/bootstrap.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/vendor/angular/angular.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/vendor/angular/angular-resource.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/services/services.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/filters/filters.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/directives/directives.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/controllers/controllers.js" />"></script>
		<script type="text/javascript">
			angular.module('shareTaskWeb', ['shareTaskWeb.filters', 'shareTaskWeb.services', 'shareTaskWeb.directives', 'shareTaskWeb.controllers'])
				.run(['$rootScope', function ($rootScope) {
					$rootScope.appBaseUrl = '<c:url value="/" />';
					$rootScope.appVersion = '${applicationVersion}';
					$rootScope.appLocale = {language: '<c:out value="${pageContext.request.locale.language}" />', country: '<c:out value="${pageContext.request.locale.country}" />'};
				}]);
		</script>
		<script>
			(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
			(i[r].q=i[r].q||[]).push(arguments);},i[r].l=1*new Date();a=s.createElement(o),
			m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m);
			})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
			ga('create', '${googleAnalyticsAccount}', 'shareta.sk');
			ga('send', 'pageview');
		</script>
	</body>
</html>