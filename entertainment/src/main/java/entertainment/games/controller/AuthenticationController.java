package entertainment.games.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

import entertainment.games.common.ContextConstants;
import entertainment.games.common.MessageUtils;
import entertainment.games.dto.UserDto;
import entertainment.games.dto.UserSessionDto;
import entertainment.games.entity.User;
import entertainment.games.enums.AccountReturnCode;
import entertainment.games.enums.MessageType;
import entertainment.games.form.authentication.LoginForm;
import entertainment.games.form.authentication.UserForm;
import entertainment.games.service.AuthenticationService;

@Controller
public class AuthenticationController {
	
	@Autowired
	protected AuthenticationService authService;
	public Gson gson = new Gson();
	
	@GetMapping(value = "/login")
	public String login(
			Model model,
			HttpServletRequest request)  {
		
		LoginForm loginForm = new LoginForm();
		
		//perhaps pre-populate cached (cookie-based) username
		
		model.addAttribute(loginForm);
		
		return "authentication/login";
	}
	
	@PostMapping(value = "/login")
	public String login(
			Model model,
			HttpServletRequest request,
			@ModelAttribute LoginForm loginForm)  {
		
		HttpSession session = request.getSession();
		AccountReturnCode returnCode;
		UserDto userDto = authService.getUser(loginForm.getUsername());
		
		if (userDto != null && userDto.getUser() != null) {
			returnCode = authService.authenticateUser(userDto, loginForm.getPassword());
		}
		else {
			returnCode = AccountReturnCode.INVALID_USER;
		}
		
		switch(returnCode) {
			case LOGIN_SUCCESSFUL:
				UserSessionDto userSessionData = new UserSessionDto();
				userSessionData.setUserDto(userDto);
				userSessionData.setLoggedIn(true);
				session.setAttribute(ContextConstants.USER_SESSION_DATA, userSessionData);
				MessageUtils.addMessage(request, "Logged in successfully!", MessageType.CONFIRMATION);
				break;
			case INCORRECT_PASSWORD:
				MessageUtils.addMessage(request, "Password is Incorrect", MessageType.ALERT);
				break;
			case PASSWORD_EXPIRED:
				MessageUtils.addMessage(request, "Password is Expired", MessageType.ALERT);
				break;
			case ACCOUNT_SUSPENDED:
				MessageUtils.addMessage(request, "Account is Suspended", MessageType.ALERT);
				break;
			case INVALID_USER:
				MessageUtils.addMessage(request, "Username is Invalid", MessageType.ALERT);
				break;
			default:
				break;
		}
		
		model.addAttribute(loginForm);
		
		return "authentication/login";
	}
	
	@RequestMapping(value="/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		MessageUtils.addMessage(request, "Logged Out Successfully", MessageType.CONFIRMATION);
		session.removeAttribute(ContextConstants.USER_SESSION_DATA);
		return "index";
	}
	
	@GetMapping(value = "/createUser")
	public String newUser(
			Model model,
			HttpServletRequest request)  {
		
		String returnPage = "authentication/create_user";
		return returnPage;
	}
	
	@PostMapping(value = "/createUser")
	public String createUser(
			Model model,
			HttpServletRequest request,
			@ModelAttribute UserForm userForm)  {
		
		String returnPage = "authentication/create_user";
		AccountReturnCode returnCode;
		HttpSession session = request.getSession();
		
		
		UserDto userDto = new UserDto();
		User user =  new User();
		user.setUsername(userForm.getUsername());
		user.setPassword(userForm.getPassword());
		user.getName().setFirstName(userForm.getFirstName());
		user.getName().setMiddleName(userForm.getMiddleName());
		user.getName().setLastName(userForm.getLastName());
		userDto.setUser(user);
		
		returnCode = authService.createNewUser(userDto);
		
		switch(returnCode) {
			case ACCOUNT_ALREADY_EXISTS:
				MessageUtils.addMessage(request, "An account with username '" + userForm.getUsername() + "' already exists", MessageType.ALERT);
				break;
			case ACCOUNT_CREATION_ERROR:
				MessageUtils.addMessage(request, "Error creating account; please try again", MessageType.ALERT);
				break;
			case ACCOUNT_CREATED:
				UserSessionDto userSessionData = new UserSessionDto();
				userDto = authService.getUser(userForm.getUsername());
				userSessionData.setUserDto(userDto);
				userSessionData.setLoggedIn(true);
				session.setAttribute(ContextConstants.USER_SESSION_DATA, userSessionData);
				MessageUtils.addMessage(request, "Account created successfully!", MessageType.CONFIRMATION);
				returnPage = "index";
				break;
			default:
				break;
		}
		
		model.addAttribute(userForm);
		
		return returnPage;
	}
}
