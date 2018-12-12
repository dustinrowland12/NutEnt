package entertainment.games.dto;

import entertainment.games.entity.User;
import entertainment.games.enums.AccountReturnCode;

public class UserDto {
	private User user;
	private AccountReturnCode returnCode;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public AccountReturnCode getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(AccountReturnCode returnCode) {
		this.returnCode = returnCode;
	}
	
}
