package nutent.entertainment.web.repository;

import org.springframework.transaction.annotation.Transactional;

public interface UserRepositoryCustom {
	@Transactional
	void incrementUnsuccessfulLoginAttempts(String username);
	@Transactional
	void resetUnsuccessfulLoginAttempts(String username);
}
