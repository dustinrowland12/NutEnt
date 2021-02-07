package nutent.entertainment.web.service;

import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nutent.entertainment.web.dto.UserDetailsImpl;
import nutent.entertainment.web.dto.UserDto;
import nutent.entertainment.web.entity.Role;
import nutent.entertainment.web.entity.User;
import nutent.entertainment.web.enums.AccountReturnCode;
import nutent.entertainment.web.repository.RoleRepository;
import nutent.entertainment.web.repository.UserRepository;

@Service
public class AuthenticationService implements UserDetailsService {
	@Autowired
	protected UserRepository userRepository; 
	@Autowired
	protected RoleRepository roleRepository; 
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
	public User createNewUser(User user) {
		User existingUser = userRepository.findByUsername(user.getUsername());
		
		if (existingUser != null) {
			throw new DataIntegrityViolationException("User " + user.getUsername() + " already exists.");
		}
		
		String hashedPassword = encoder.encode(user.getPassword());
		user.setPassword(hashedPassword);
		user.setPasswordUpdateDate(new Date());
		user.setAccountLocked(false);
		user.setUnsuccessfulLoginAttempts(0);
		userRepository.save(user);	
		
		//add default role(s)
		if (user.getRoles() == null) {
			user.setRoles(new HashSet<>());
		}
		
		//add games role
		Role role = roleRepository.findByRole("games");
		user.getRoles().add(role);
		
		return user;
		
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
	
}
