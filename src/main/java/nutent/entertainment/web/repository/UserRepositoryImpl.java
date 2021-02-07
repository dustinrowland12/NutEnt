package nutent.entertainment.web.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nutent.entertainment.web.common.AccountLockedReasonCode;
import nutent.entertainment.web.entity.LuAccountLockedReasonCode;
import nutent.entertainment.web.entity.User;

@Component
public class UserRepositoryImpl implements UserRepositoryCustom {
	@PersistenceContext
    private EntityManager em;
	
	@Autowired
	private LuAccountLockedReasonCodeRepository luAccountLockedReasonCodeRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private static final int MAX_ATTEMPTS = 3;

	@Override
	public void incrementUnsuccessfulLoginAttempts(String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) return;
		//update login attempts
		user.setUnsuccessfulLoginAttempts(user.getUnsuccessfulLoginAttempts() + 1);
		if (user.getUnsuccessfulLoginAttempts() >= MAX_ATTEMPTS) {
			user.setAccountLocked(true);
			LuAccountLockedReasonCode reasonCode = luAccountLockedReasonCodeRepository.findByCode(AccountLockedReasonCode.EXCEEDED_MAX_LOGIN_ATTEMPTS);
			user.setAccountLockedReasonCode(reasonCode);
		}
		em.persist(user);
	}

	@Override
	public void resetUnsuccessfulLoginAttempts(String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) return;
		user.setUnsuccessfulLoginAttempts(0);
		if (user.isAccountLocked() == true 
				&& user.getAccountLockedReasonCode() != null
				&& user.getAccountLockedReasonCode().getCode().equals(AccountLockedReasonCode.EXCEEDED_MAX_LOGIN_ATTEMPTS)) {
			user.setAccountLocked(false);
			user.setAccountLockedReasonCode(null);
		}
		em.persist(user);
	}
	
}
