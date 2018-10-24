package entertainment.games.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.mkammerer.argon2.*;
import entertainment.games.dao.UserDao;
import entertainment.games.dto.UserDto;
import entertainment.games.entity.User;
import entertainment.games.enums.LoginReturnCode;

@Service
public class AuthenticationService {
	@Autowired
	protected UserDao userDao; 
	
	public UserDto getUser(String username) {
		UserDto userDto = new UserDto();
		User user = userDao.getUserByUsername(username);
		userDto.setUser(user);
		return userDto;
	}
		
	public LoginReturnCode authenticateUser(UserDto userDTO, String password) {
		boolean successful = false;
				
		if (userDTO != null) {
			User user = userDTO.getUser();
			if (user != null) {
				successful = verifyPassword(userDTO.getUser(), password);
			}
		}
		
		if (!successful) {
			
			return LoginReturnCode.incorrectPassword;
		}
		else {
			//user authenticated, verify user account active
		}
		
		return LoginReturnCode.success;
		
	}
	
	public UserDto createNewUser(UserDto userDto) throws Exception {
		
		userDto.setUser(userDao.createNewUser(userDto));
		
		return userDto;
	}
	
	public static String hashPassword(String password) throws Exception {
		String hashedPassword = null;
		//byte[] salt = new byte[16];
		//SecureRandom random = new SecureRandom();
		
		Argon2 argon2 = Argon2Factory.create();
		
		try {
			//random.nextBytes(salt);
			hashedPassword = argon2.hash(3, 65536, 1, password);
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return hashedPassword;
	}
	
	protected boolean verifyPassword(User user, String password) {
		boolean verified = false;
		
		Argon2 argon2 = Argon2Factory.create();
		verified = argon2.verify(user.getPassword(), password);
		
		return verified;
	}
}
