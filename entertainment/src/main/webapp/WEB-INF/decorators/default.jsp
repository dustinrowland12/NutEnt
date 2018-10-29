<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
	<meta name="viewport" content="width=device-width">
	<%-- CSS --%>
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/bootstrap.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/main.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/fontawesome/css/all.min.css"/>">
	<%-- JS --%>
	<script type="text/javascript" src="<c:url value="/resources/js/jquery.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/lodash.js"/>" /></script>
	<script type="text/javascript" src="<c:url value="/resources/js/moment.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/vue/vue.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/vue/validators.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/vue/vuelidate.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/bootstrap.bundle.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/main.js"/>"></script>
	<%-- Page specific CSS and JS --%>
	<sitemesh:write property="head"/>
	<title>Nutmeg Entertainment</title>
</head>
<body class="bg-dark">
	<div id="page">
		<div class="main-grid-container">
			<div class="page-header">
				<img class="logo-nutmeg"
					src="<c:url value="/resources/images/nutmeg.jpg"/>">
				<h1>Nutmeg Entertainment</h1>
			</div>
			<div class="main-menu">
				<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
					<button class="navbar-toggler" type="button" data-toggle="collapse"
						data-target="#navbarSupportedContent"
						aria-controls="navbarSupportedContent" aria-expanded="false"
						aria-label="Main Menu">
						<span class="navbar-toggler-icon"></span>
					</button>
					<a class="navbar-brand" href="<c:url value="/"/>"> NutEnt </a>
					<div class="collapse navbar-collapse justify-content-between" id="navbarSupportedContent">
						<ul class="navbar-nav">
							<li class="nav-item"><a class="nav-link" href="#">About Us</a></li>
							<li class="nav-item dropdown">
								<a class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
									role="button" data-toggle="dropdown" aria-haspopup="true"
									aria-expanded="false"> Games </a>
								<div class="dropdown-menu" aria-labelledby="navbarDropdown">
									<a class="dropdown-item" href="<c:url value="/games/maze"/>">Maze</a>
									<a class="dropdown-item" href="<c:url value="/games/minesweeper"/>">Minesweeper</a> 
									<a class="dropdown-item" href="<c:url value="/games/sudoku"/>">Sudoku</a>
								</div></li>
						</ul>
						<ul class="navbar-nav" id="loggedInUser">
							<c:choose>
						    	<c:when test="${not empty USER_SESSION_DATA && not empty USER_SESSION_DATA.loggedInUser}">
							    	<li class="nav-item dropdown">
							    		<a class="nav-link" href="#" id="loginDropdown" role="button" 
							    			data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							    			${USER_SESSION_DATA.loggedInUser.username} 
							    			<i class="fas fa-cog" style="margin-left:0.255em"></i>
						    			</a>
									    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="loginDropdown">
									      <a class="dropdown-item disabled" href="#">Edit Profile</a>
									      <a class="dropdown-item" href="<c:url value="/resetPassword"/>">Reset Password</a>
									      <div class="dropdown-divider"></div>
									      <a class="dropdown-item" href="<c:url value="/logout"/>">Logout</a>
									    </div>
					    			</li>
						    	</c:when>
						    	<c:otherwise>
						    		<li class="nav-item"><a class="nav-link" href="<c:url value="/login"/>">Login</a></li>
						    		<li class="nav-item"><a class="nav-link" href="<c:url value="/createUser"/>">New User</a></li>
						    	</c:otherwise>
						    </c:choose>
						</ul>
					</div>
				</nav>
			</div>
			<div class="content">
				<div id="systemMessages">
					<c:if test="${ not empty MESSAGES }">
						<c:forEach items="${ MESSAGES.informationals }" var="info">
							<div class="alert alert-info" role="alert">
								${info.message}
							</div>
						</c:forEach>
						<c:forEach items="${ MESSAGES.confirmations }" var="confirm">
							<div class="alert alert-success" role="alert">
								${confirm.message}
							</div>
						</c:forEach>
						<c:forEach items="${ MESSAGES.alerts }" var="alert">
							<div class="alert alert-danger" role="alert">
								${alert.message}
							</div>
						</c:forEach>
					</c:if>
				</div>
				<sitemesh:write property="body" />
			</div>
			<div class="footer"></div>
		</div>
	</div>
</body>
</html>