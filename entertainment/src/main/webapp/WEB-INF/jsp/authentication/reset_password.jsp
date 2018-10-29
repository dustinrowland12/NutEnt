<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<body>
	<form id="resetPasswordForm" method="post" action="<c:url value="/resetPassword"/>">
		<fieldset>
			<legend>Reset Password</legend>
			<div class="form-group">
				<label for="username">User ID</label>
				<input type="text" class="form-control" name="username" value="${userForm.username}" readonly>
			</div>
			<div class="form-group">
				<label for="password">Password</label>
				<input type="password" class="form-control" name="password" value="${userForm.password}">
			</div>
			<div class="form-group">
				<label for="newPassword">New Password</label>
				<input type="password" class="form-control" name="newPassword" value="${userForm.newPassword}">
				<input type="password" class="form-control" name="newPasswordConfirmation" style="margin-top: .5rem" placeholder="Confirm new password" value="${userForm.newPasswordConfirmation}">
			</div>
			<input type="submit" class="btn btn-primary" value="Submit"/>
			<input type="submit" class="btn btn-secondary" value="Cancel"/>
		</fieldset>
	</form>
</body>