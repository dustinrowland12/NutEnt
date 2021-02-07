package nutent.entertainment.web.form.authentication;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ResetPasswordFormValidator implements Validator {
	
	@Override
    public boolean supports(Class<?> clazz) {
        return ResetPasswordForm.class.equals(clazz);
    }
	
	@Override
	public void validate(Object obj, Errors errors) {
		ResetPasswordForm userForm = (ResetPasswordForm) obj;
		
		if (userForm.getNewPassword() != null && !userForm.getNewPassword().equals(userForm.getNewPasswordConfirmation())) {
			errors.rejectValue("NewPassword", "New Password must match New Password Confirmation.");
		}
		
	}
	
}
