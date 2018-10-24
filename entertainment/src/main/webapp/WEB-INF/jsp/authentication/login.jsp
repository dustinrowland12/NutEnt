<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<body>
	<form id="loginForm" method="post" action="<c:url value="/login"/>">
		<fieldset>
			<legend>Login</legend>
			<div class="form-group">
				<label for="username">User ID</label>
				<input type="text" class="form-control" name="username" value="${loginForm.username}">
			</div>
			<div class="form-group">
				<label for="password">Password</label>
				<input type="password" class="form-control" name="password" value="${loginForm.password}">
			</div>
			<input type="submit" class="btn btn-primary" value="Login"/>
		</fieldset>
	</form>
</body>