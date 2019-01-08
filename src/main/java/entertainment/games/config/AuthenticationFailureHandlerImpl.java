package entertainment.games.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;

import entertainment.games.common.MessageUtils;
import entertainment.games.enums.MessageType;

public class AuthenticationFailureHandlerImpl extends ForwardAuthenticationFailureHandler {

	public AuthenticationFailureHandlerImpl(String forwardUrl) {
		super(forwardUrl);
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		MessageUtils.addMessage(request, exception.getMessage(), MessageType.ALERT);
		super.onAuthenticationFailure(request, response, exception);
	}
}