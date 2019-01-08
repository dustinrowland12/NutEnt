package entertainment.games.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import entertainment.games.common.ContextConstants;
import entertainment.games.common.MessageUtils;
import entertainment.games.dto.UserDto;
import entertainment.games.dto.UserSessionDto;
import entertainment.games.entity.User;
import entertainment.games.enums.AccountReturnCode;
import entertainment.games.enums.MessageType;
import entertainment.games.form.authentication.LoginForm;
import entertainment.games.form.authentication.ResetPasswordForm;
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
	
	@RequestMapping(value = "/login")
	public String login(
			Model model,
			HttpServletRequest request)  {
		
		LoginForm loginForm = new LoginForm();
		
		//perhaps pre-populate cached (cookie-based) username
		
		model.addAttribute(loginForm);
		
		return page_login;
	}
	
	@GetMapping(value = "/createUser")
	public String newUser(
			Model model,
			HttpServletRequest request)  {
		
		model.addAttribute(new UserForm());
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
		ResetPasswordForm resetPasswordForm = new ResetPasswordForm();
		
		if (userSessionData == null || userSessionData.getLoggedInUser() == null) {
			//user is not logged in, can't reset password
			MessageUtils.addMessage(request, "You must login to update your password", MessageType.ALERT);
			return "forward:/login";
		}
		
		User loggedInUser = userSessionData.getLoggedInUser();
		resetPasswordForm.setUsername(loggedInUser.getUsername());
		
		model.addAttribute(resetPasswordForm);
		return page_reset_password;
	}
	
	@PostMapping(value = "/resetPassword")
	public String resetPassword(
			Model model,
			HttpServletRequest request,
			@Valid @ModelAttribute ResetPasswordForm resetPasswordForm,
			BindingResult result)  {
		
		String returnPage = page_reset_password;
		model.addAttribute(resetPasswordForm);
		
		AccountReturnCode returnCode;
		HttpSession session = request.getSession();
				
		UserSessionDto userSessionData = (UserSessionDto) session.getAttribute(ContextConstants.USER_SESSION_DATA);

		//make sure user can be here
		if (userSessionData == null || userSessionData.getLoggedInUser() == null) {
			MessageUtils.addMessage(request, "You must login to update your password", MessageType.ALERT);
			return "forward:/login";
		}
		
		//validation
		if (result.hasErrors()) {
			return returnPage;
		}
		
		User loggedInUser = userSessionData.getLoggedInUser();
		String userName = loggedInUser.getUsername();

		UserDto userDto = authService.updatePassword(loggedInUser.getUsername(), resetPasswordForm.getCurrentPassword(), resetPasswordForm.getNewPassword());
		returnCode = userDto.getReturnCode();
		
		switch(returnCode) {
			case INVALID_USER:
				MessageUtils.addMessage(request, "An account with username '" + userName + "' does not exist", MessageType.ALERT);
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
		
		model.addAttribute(resetPasswordForm);
		
		return returnPage;
	}
}
