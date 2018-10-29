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
	
	//jsp pages
	private String page_home = "index";
	private String page_create_user = "authentication/create_user";
	private String page_login = "authentication/login";
	//private String page_profile = "authentication/profile";
	private String page_profile = "index";
	private String page_reset_password = "authentication/reset_password";
	
	@GetMapping(value = "/login")
	public String login(
			Model model,
			HttpServletRequest request)  {
		
		LoginForm loginForm = new LoginForm();
		
		//perhaps pre-populate cached (cookie-based) username
		
		model.addAttribute(loginForm);
		
		return page_login;
	}
	
	@PostMapping(value = "/login")
	public String login(
			Model model,
			HttpServletRequest request,
			@ModelAttribute LoginForm loginForm)  {
		
		HttpSession session = request.getSession();
		
		UserDto userDto = authService.authenticateUser(loginForm.getUsername(), loginForm.getPassword());
		AccountReturnCode returnCode = userDto.getReturnCode();
		
		switch(returnCode) {
			case LOGIN_SUCCESSFUL:
				UserSessionDto userSessionData = new UserSessionDto();
				userSessionData.setUserDto(userDto);
				userSessionData.setLoggedIn(true);
				session.setAttribute(ContextConstants.USER_SESSION_DATA, userSessionData);
				MessageUtils.addMessage(request, "Logged in successfully!", MessageType.CONFIRMATION);
				return page_home;
			case INCORRECT_PASSWORD:
				MessageUtils.addMessage(request, "Password is Incorrect", MessageType.ALERT);
				break;
			case PASSWORD_EXPIRED:
				MessageUtils.addMessage(request, "Password is Expired", MessageType.ALERT);
				break;
			case ACCOUNT_LOCKED:
				MessageUtils.addMessage(request, "Account is Locked: " + userDto.getUser().getAccountLockedReasonCode().getReason(), MessageType.ALERT);
				break;
			case INVALID_USER:
				MessageUtils.addMessage(request, "Username is Invalid", MessageType.ALERT);
				break;
			default:
				MessageUtils.addMessage(request, "Unknown Error Occurred", MessageType.ALERT);
				break;
		}
		
		model.addAttribute(loginForm);
		
		return page_login;
	}
	
	@RequestMapping(value="/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		MessageUtils.addMessage(request, "Logged Out Successfully", MessageType.CONFIRMATION);
		session.removeAttribute(ContextConstants.USER_SESSION_DATA);
		return page_home;
	}
	
	@GetMapping(value = "/createUser")
	public String newUser(
			Model model,
			HttpServletRequest request)  {
		
		String returnPage = page_create_user;
		return returnPage;
	}
	
	@PostMapping(value = "/createUser")
	public String createUser(
			Model model,
			HttpServletRequest request,
			@ModelAttribute UserForm userForm)  {
		
		String returnPage = page_create_user;
		AccountReturnCode returnCode;
		HttpSession session = request.getSession();
		
		User user =  new User();
		user.setUsername(userForm.getUsername());
		user.setPassword(userForm.getPassword());
		user.getName().setFirstName(userForm.getFirstName());
		user.getName().setMiddleName(userForm.getMiddleName());
		user.getName().setLastName(userForm.getLastName());
		
		UserDto userDto = authService.createNewUser(user);
		returnCode = userDto.getReturnCode();
		
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
				returnPage = page_home;
				break;
			default:
				break;
		}
		
		model.addAttribute(userForm);
		
		return returnPage;
	}
	
	@GetMapping(value = "/resetPassword")
	public String resetPassword(
			Model model,
			HttpServletRequest request)  {
		
		HttpSession session = request.getSession();
		UserSessionDto userSessionData = (UserSessionDto) session.getAttribute(ContextConstants.USER_SESSION_DATA);
		UserForm userForm = new UserForm();
		
		if (userSessionData != null && userSessionData.getLoggedInUser() != null) {
			User loggedInUser = userSessionData.getLoggedInUser();
			userForm.setUsername(loggedInUser.getUsername());
		}
		
		model.addAttribute(userForm);
		String returnPage = page_reset_password;
		return returnPage;
	}
	
	@PostMapping(value = "/resetPassword")
	public String resetPassword(
			Model model,
			HttpServletRequest request,
			@ModelAttribute UserForm userForm)  {
		
		String returnPage = page_reset_password;
		model.addAttribute(userForm);
		
		if (!userForm.getNewPassword().equals(userForm.getNewPasswordConfirmation())) {
			MessageUtils.addMessage(request, "New Password does not match new password confirmation field", MessageType.ALERT);
			return returnPage;
		}
		
		AccountReturnCode returnCode;
		HttpSession session = request.getSession();
				
		UserSessionDto userSessionData = (UserSessionDto) session.getAttribute(ContextConstants.USER_SESSION_DATA);

		if (userSessionData == null || userSessionData.getLoggedInUser() == null) {
			MessageUtils.addMessage(request, "You must login to update your password", MessageType.ALERT);
		}
		
		User loggedInUser = userSessionData.getLoggedInUser();

		UserDto userDto = authService.updatePassword(loggedInUser.getUsername(), userForm.getPassword(), userForm.getNewPassword());
		returnCode = userDto.getReturnCode();
		
		switch(returnCode) {
			case INVALID_USER:
				MessageUtils.addMessage(request, "An account with username '" + userForm.getUsername() + "' does not exist", MessageType.ALERT);
				break;
			case INCORRECT_PASSWORD:
				MessageUtils.addMessage(request, "The password you entered is incorrect", MessageType.ALERT);
				break;
			case PASSWORD_UPDATED:
				userDto = authService.getUser(loggedInUser.getUsername());
				userSessionData.setUserDto(userDto);
				userSessionData.setLoggedIn(true);
				session.setAttribute(ContextConstants.USER_SESSION_DATA, userSessionData);
				MessageUtils.addMessage(request, "Password updated successfully!", MessageType.CONFIRMATION);
				returnPage = page_profile;
				break;
			default:
				break;
		}
		
		model.addAttribute(userForm);
		
		return returnPage;
	}
}
