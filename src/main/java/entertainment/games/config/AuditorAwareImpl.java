package entertainment.games.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import entertainment.games.dto.UserDetailsImpl;


public class AuditorAwareImpl implements AuditorAware<String> {

	public Optional<String> getCurrentAuditor() {
		return Optional.ofNullable(((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
	}
}