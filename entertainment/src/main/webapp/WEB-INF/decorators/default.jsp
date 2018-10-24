<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width">
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/bootstrap.css"/>">
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/main.css"/>">
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/fontawesome/css/all.min.css"/>">
<script type="text/javascript"
	src="<c:url value="/resources/js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/lodash.js"/>" /></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/moment.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/vue/vue.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/vue/validators.min.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/vue/vuelidate.min.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/bootstrap.bundle.min.js"/>"></script>
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
					<a class="navbar-brand" href="<c:url value="/"/>"> NutEnt </a>
					<button class="navbar-toggler" type="button" data-toggle="collapse"
						data-target="#navbarSupportedContent"
						aria-controls="navbarSupportedContent" aria-expanded="false"
						aria-label="Toggle navigation">
						<span class="navbar-toggler-icon"></span>
					</button>
					<div class="collapse navbar-collapse" id="navbarSupportedContent">
						<ul class="navbar-nav mr-auto">
							<li class="nav-item"><a class="nav-link" href="#">About
									Us</a></li>
							<li class="nav-item dropdown"><a
								class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
								role="button" data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false"> Games </a>
								<div class="dropdown-menu" aria-labelledby="navbarDropdown">
									<a class="dropdown-item" href="<c:url value="/games/maze"/>">Maze</a>
									<a class="dropdown-item"
										href="<c:url value="games/minesweeper"/>">Minesweeper</a> <a
										class="dropdown-item" href="<c:url value="/games/sudoku"/>">Sudoku</a>
								</div></li>
						</ul>
					</div>
					<c:if test="${not empty USER_SESSION_DATA && not empty USER_SESSION_DATA.loggedInUser}">
						<span id="loggedInUser" class="navbar-text">${USER_SESSION_DATA.loggedInUser.username} <a href="logout">(Logout)</a></span>
				    </c:if>
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