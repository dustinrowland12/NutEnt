package entertainment.games.form.authentication;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginForm {
	
	@NotNull
	@Size(min=1, max=256)
	protected String username;
	@NotNull
	@Size(min=1, max=256)
	protected String password;
	
	public LoginForm() {
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
