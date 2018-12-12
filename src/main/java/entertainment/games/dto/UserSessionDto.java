package entertainment.games.dto;

import entertainment.games.entity.User;

public class UserSessionDto {
	private UserDto userDto;
	
	private boolean isLoggedIn;

	public UserDto getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	
	//convenience method for accessing user in user DTO
	public User getLoggedInUser() {
		User user = null;
		if (userDto != null && userDto.getUser() != null) {
			user = userDto.getUser();
		}
		return user;
	}
}
