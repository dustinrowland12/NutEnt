package nutent.entertainment.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import nutent.entertainment.web.common.ContextConstants;
import nutent.entertainment.web.common.MessageUtils;
import nutent.entertainment.web.dto.UserDto;
import nutent.entertainment.web.dto.UserSessionDto;
import nutent.entertainment.web.entity.User;
import nutent.entertainment.web.enums.AccountReturnCode;
import nutent.entertainment.web.enums.MessageType;
import nutent.entertainment.web.form.authentication.LoginForm;
import nutent.entertainment.web.form.authentication.ResetPasswordForm;
import nutent.entertainment.web.form.authentication.UserForm;
import nutent.entertainment.web.service.AuthenticationService;

@Controller
public class AuthenticationController {
	
	@Autowired
	protected AuthenticationService authService;
	
	//pages
	private static final String PAGE_HOME = "index";
	private static final String PAGE_CREATE_USER = "authentication/create_user";
	private static final String PAGE_LOGIN = "authentication/login";
	//private String PAGE_PROFILE = "authentication/profile";
	private static final String PAGE_PROFILE = "index";
	private static final String PAGE_RESET_PASSWORD = "authentication/reset_password";
	
	@RequestMapping(value = "/login")
	public String login(
			Model model,
			HttpServletRequest request)  {
		
		LoginForm loginForm = new LoginForm();
		
		//perhaps pre-populate cached (cookie-based) username
		
		model.addAttribute(loginForm);
		
		return PAGE_LOGIN;
	}
	
	@GetMapping(value = "/createUser")
	public String newUser(
			Model model,
			HttpServletRequest request)  {
		
		model.addAttribute(new UserForm());
		String returnPage = PAGE_CREATE_USER;
		return returnPage;
	}
	
	@PostMapping(value = "/createUser")
	public String createUser(
			Model model,
			HttpServletRequest request,
			@ModelAttribute UserForm userForm)  {
		
		String returnPage = PAGE_CREATE_USER;
		
		User user =  new User();
		user.setUsername(userForm.getUsername());
		user.setPassword(userForm.getPassword());
		user.getName().setFirstName(userForm.getFirstName());
		user.getName().setMiddleName(userForm.getMiddleName());
		user.getName().setLastName(userForm.getLastName());
		
		try {
			user = authService.createNewUser(user);
			MessageUtils.addMessage(request, "Account created successfully!", MessageType.CONFIRMATION);
			returnPage = PAGE_HOME;
		}
		catch(DataIntegrityViolationException e) {
			MessageUtils.addMessage(request, e.getMessage(), MessageType.ALERT);
		}
		catch (Exception e) {
			MessageUtils.addMessage(request, "Error creating account; please try again", MessageType.ALERT);
		}
		
		if (returnPage.equals(PAGE_CREATE_USER)) {
			model.addAttribute(userForm);
		}
		
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
		return PAGE_RESET_PASSWORD;
	}
	
	@PostMapping(value = "/resetPassword")
	public String resetPassword(
			Model model,
			HttpServletRequest request,
			@Valid @ModelAttribute ResetPasswordForm resetPasswordForm,
			BindingResult result)  {
		
		String returnPage = PAGE_RESET_PASSWORD;
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
				returnPage = PAGE_PROFILE;
				break;
			default:
				break;
		}
		
		model.addAttribute(resetPasswordForm);
		
		return returnPage;
	}
}
