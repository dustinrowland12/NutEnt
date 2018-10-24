package entertainment.games.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.google.gson.Gson;

import entertainment.games.common.ContextConstants;
import entertainment.games.common.MessageUtils;
import entertainment.games.dto.UserDto;
import entertainment.games.dto.UserSessionDto;
import entertainment.games.enums.LoginReturnCode;
import entertainment.games.enums.MessageType;
import entertainment.games.service.AuthenticationService;

@Controller
public class AuthenticationController {
	
	@Autowired
	protected AuthenticationService authService;
	public Gson gson = new Gson();
	
	@PostMapping(value = "/login")
	public String login(
			HttpServletRequest request,
			@RequestParam("username") String username, 
			@RequestParam("password") String password)  {
		
		HttpSession session = request.getSession();
		LoginReturnCode returnCode;
		UserDto userDto = authService.getUser(username);
		
		if (userDto != null && userDto.getUser() != null) {
			returnCode = authService.authenticateUser(userDto, password);
		}
		else {
			returnCode = LoginReturnCode.invalidUser;
		}
		
		switch(returnCode) {
			case success:
				UserSessionDto userSessionData = new UserSessionDto();
				userSessionData.setUserDto(userDto);
				userSessionData.setLoggedIn(true);
				session.setAttribute(ContextConstants.USER_SESSION_DATA, userSessionData);
				MessageUtils.addMessage(request, "Logged in successfully!", MessageType.CONFIRMATION);
				break;
			case incorrectPassword:
				MessageUtils.addMessage(request, "Password is Incorrect", MessageType.ALERT);
				break;
			case passwordExpired:
				MessageUtils.addMessage(request, "Password is Expired", MessageType.ALERT);
				break;
			case accountSuspended:
				MessageUtils.addMessage(request, "Account is Suspended", MessageType.ALERT);
				break;
			case invalidUser:
				MessageUtils.addMessage(request, "Username is Invalid", MessageType.ALERT);
				break;
		}
		
		return "index";
	}
	
	@RequestMapping(value="/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
		return "index";
	}
}
