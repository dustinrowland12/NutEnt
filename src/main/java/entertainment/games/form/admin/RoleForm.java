package entertainment.games.form.admin;

import javax.validation.constraints.NotBlank;

public class RoleForm {
	private int roleId;
	
	@NotBlank(message="Role Name must not be blank.")
	private String roleName;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
