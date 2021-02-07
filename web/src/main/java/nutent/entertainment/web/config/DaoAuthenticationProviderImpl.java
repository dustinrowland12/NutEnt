package nutent.entertainment.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import nutent.entertainment.web.repository.UserRepository;

public class DaoAuthenticationProviderImpl extends DaoAuthenticationProvider {
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		try {
			Authentication auth = super.authenticate(authentication);
			// if reach here, means login success, else an exception will be thrown
			// reset the user_attempts
			userRepository.resetUnsuccessfulLoginAttempts(authentication.getName());
			return auth;
		} catch (BadCredentialsException e) {
			// invalid login, update to user_attempts
			userRepository.incrementUnsuccessfulLoginAttempts(authentication.getName());
			throw e;
		}
		
	}
}
