package entertainment.games.form.authentication;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ResetPasswordForm {

	private String username;
	@NotNull
	@Size(min=1, max=256)
	private String currentPassword;
	@NotNull
	@Size(min=1, max=256)
	private String newPassword;
	@NotNull
	@Size(min=1, max=256)
	private String newPasswordConfirmation;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getNewPasswordConfirmation() {
		return newPasswordConfirmation;
	}
	public void setNewPasswordConfirmation(String newPasswordConfirmation) {
		this.newPasswordConfirmation = newPasswordConfirmation;
	}
}
