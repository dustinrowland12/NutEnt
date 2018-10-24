<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<body>
	<form id="createUserForm" method="post" action="<c:url value="/createUser"/>">
		<fieldset>
			<legend>Create User</legend>
			<div class="form-group">
				<label for="username">User ID</label>
				<input type="text" class="form-control" name="username" value="${userForm.username}">
			</div>
			<div class="form-group">
				<label for="password">Password</label>
				<input type="password" class="form-control" name="password" value="${userForm.password}">
			</div>
			<div class="form-group">
				<label for="firstName">First Name</label>
				<input type="text" class="form-control" name="firstName" value="${userForm.firstName}">
			</div>
			<div class="form-group">
				<label for="middleName">Middle Name</label>
				<input type="text" class="form-control" name="middleName" value="${userForm.middleName}">
			</div>
			<div class="form-group">
				<label for="lastName">Last Name</label>
				<input type="text" class="form-control" name="lastName" value="${userForm.lastName}">
			</div>
			<input type="submit" class="btn btn-primary" value="Submit"/>
		</fieldset>
	</form>
</body>