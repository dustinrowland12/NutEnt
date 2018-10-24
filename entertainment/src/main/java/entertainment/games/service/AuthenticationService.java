package entertainment.games.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.mkammerer.argon2.*;
import entertainment.games.dao.UserDao;
import entertainment.games.dto.UserDto;
import entertainment.games.entity.User;
import entertainment.games.enums.AccountReturnCode;

@Service
public class AuthenticationService {
	@Autowired
	protected UserDao userDao; 
	
	public UserDto getUser(String username) {
		UserDto userDto = new UserDto();
		User user = userDao.findByUsername(username);
		userDto.setUser(user);
		return userDto;
	}
		
	public AccountReturnCode authenticateUser(UserDto userDTO, String password) {
		boolean successful = false;
				
		if (userDTO != null) {
			User user = userDTO.getUser();
			if (user != null) {
				successful = verifyPassword(userDTO.getUser(), password);
			}
		}
		
		if (!successful) {
			
			return AccountReturnCode.INCORRECT_PASSWORD;
		}
		else {
			//user authenticated, verify user account active
		}
		
		return AccountReturnCode.LOGIN_SUCCESSFUL;
		
	}
	
	public AccountReturnCode createNewUser(UserDto userDto) {
		UserDto existingUserDto = getUser(userDto.getUser().getUsername());
		
		if (existingUserDto != null && existingUserDto.getUser() != null) {
			return AccountReturnCode.ACCOUNT_ALREADY_EXISTS;
		}
		
		try {
			User user = userDto.getUser();
			String hashedPassword = hashPassword(user.getPassword());
			user.setPassword(hashedPassword);
			user.setCreateDate(new Date());
			user.setUpdateDate(new Date());
			userDao.save(user);	
			return AccountReturnCode.ACCOUNT_CREATED;
		}
		catch(Exception e) {
			e.printStackTrace();
			return AccountReturnCode.ACCOUNT_CREATION_ERROR;
		}
		
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
