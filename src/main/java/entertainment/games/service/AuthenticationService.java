package entertainment.games.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import entertainment.games.dto.UserDetailsImpl;
import entertainment.games.dto.UserDto;
import entertainment.games.entity.User;
import entertainment.games.enums.AccountReturnCode;
import entertainment.games.repository.UserRepository;

@Service
public class AuthenticationService implements UserDetailsService {
	@Autowired
	protected UserRepository userRepository; 
	@Autowired
	protected PasswordEncoder encoder;
	
	@Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImpl(user);
    }
	
	@Transactional
	public UserDto getUser(String username) {
		UserDto userDto = new UserDto();
		User user = userRepository.findByUsername(username);
		userDto.setUser(user);
		return userDto;
	}
	
	/* Now handled with Spring Security
	@Transactional
	public UserDto authenticateUser(String username, String password) {
		boolean successful = false;
				
		UserDto userDto = getUser(username);
		
		if (userDto.getUser() == null) {
			userDto.setReturnCode(AccountReturnCode.INVALID_USER);
			return userDto;
		}
		
		User user = userDto.getUser();
		successful = encoder.matches(password, user.getPassword());
		
		//if password was wrong, update login attempts and potentially lock account
		if (!successful) {
			user = updateUnsuccessfulLoginAttempts(user, user.getUnsuccessfulLoginAttempts() + 1);
			userRepository.save(user);
			if (user.isAccountLocked() == true) {
				userDto.setReturnCode(AccountReturnCode.ACCOUNT_LOCKED);
			}
			else {
				userDto.setReturnCode(AccountReturnCode.INCORRECT_PASSWORD);
			}
			return userDto;
		}
		
		//user authenticated, verify user account active
		if (user.isAccountLocked()) {
			userDto.setReturnCode(AccountReturnCode.ACCOUNT_LOCKED);
			return userDto;
		}
		
		//passed all failure cases, update login attempts
		user.setUnsuccessfulLoginAttempts(0);
		
		userDto.setReturnCode(AccountReturnCode.LOGIN_SUCCESSFUL);
		return userDto;
	}
	*/
	@Transactional
	public UserDto createNewUser(User user) {
		UserDto userDto = new UserDto();
		UserDto existingUserDto = getUser(user.getUsername());
		
		if (existingUserDto != null && existingUserDto.getUser() != null) {
			userDto.setReturnCode(AccountReturnCode.ACCOUNT_ALREADY_EXISTS);
			return userDto;
		}
		
		try {
			String hashedPassword = encoder.encode(user.getPassword());
			user.setPassword(hashedPassword);
			user.setCreateDate(new Date());
			user.setUpdateDate(new Date());
			user.setPasswordUpdateDate(new Date());
			user.setAccountLocked(false);
			user.setUnsuccessfulLoginAttempts(0);
			userRepository.save(user);	
			
			userDto.setUser(user);
			userDto.setReturnCode(AccountReturnCode.ACCOUNT_CREATED);
			return userDto;
		}
		catch(Exception e) {
			e.printStackTrace();
			userDto.setReturnCode(AccountReturnCode.ACCOUNT_CREATION_ERROR);
			return userDto;
		}
		
	}
	
	@Transactional
	public UserDto updatePassword(String username, String originalPassword, String newPassword) {
		UserDto userDto = new UserDto();

		try {
			//get latest user info
			userDto = getUser(username);
			if (userDto == null || userDto.getUser() == null) {
				userDto.setReturnCode(AccountReturnCode.INVALID_USER);
				return userDto;
			}
			User user = userDto.getUser();
			//verify user's original password
			boolean verified = encoder.matches(originalPassword, user.getPassword());
			if (verified == false) {
				userDto.setReturnCode(AccountReturnCode.INCORRECT_PASSWORD);
				return userDto;
			}
			String newHashedPassword = encoder.encode(newPassword);
			user.setPassword(newHashedPassword);
			user.setPasswordUpdateDate(new Date());
			user.setUpdateDate(new Date());
			userRepository.save(user);
			//reset number of login attempts
			userRepository.resetUnsuccessfulLoginAttempts(username);
		}
		catch (Exception e) {
			e.printStackTrace();
			userDto.setReturnCode(AccountReturnCode.UNKNOWN_ERROR);
			return userDto;
		}
		
		userDto.setReturnCode(AccountReturnCode.PASSWORD_UPDATED);
		return userDto;
	}
	
	/* Now done with Repository
	@Transactional
	private User updateUnsuccessfulLoginAttempts(User user, Integer attempts) {
		//update login attempts
		user.setUnsuccessfulLoginAttempts(attempts);
		if (user.getUnsuccessfulLoginAttempts() >= 3) {
			user.setAccountLocked(true);
			LuAccountLockedReasonCode reasonCode = luAccountLockedReasonCodeRepository.findByCode(AccountLockedReasonCode.EXCEEDED_MAX_LOGIN_ATTEMPTS);
			user.setAccountLockedReasonCode(reasonCode);
		}
		else {
			//if account is locked due to too many attempts, unlock it
			if (user.isAccountLocked() == true 
					&& user.getAccountLockedReasonCode() != null
					&& user.getAccountLockedReasonCode().getCode().equals(AccountLockedReasonCode.EXCEEDED_MAX_LOGIN_ATTEMPTS)) {
				user.setAccountLocked(false);
				user.setAccountLockedReasonCode(null);
			}
		}
		return user;
	}
	*/
}
