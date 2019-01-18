package entertainment.games.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import entertainment.games.entity.Role;
import entertainment.games.form.admin.RoleForm;
import entertainment.games.service.AdminService;

@Controller
@RequestMapping("admin")
public class AdminController {
	
	@Autowired
	protected AdminService adminService;
	
	//pages
	private static final String PAGE_ADMIN_CONSOLE = "admin/console";
	private static final String PAGE_ROLE_LIST = "admin/roles";
	private static final String PAGE_USER_LIST = "admin/users";
	private static final String PAGE_GAME_LIST = "admin/games";
	private static final String PAGE_ROLE_MANAGEMENT = "admin/role";
	private static final String PAGE_USER_MANAGEMENT = "admin/user";
	private static final String PAGE_GAME_MANAGEMENT = "admin/game";
	
	@GetMapping("/console")
	public String console(
			Model model,
			HttpServletRequest request) {
		return PAGE_ADMIN_CONSOLE;
	}
	
	@GetMapping("/roles")
	public String roles(
			Model model,
			HttpServletRequest request) {
		
		model.addAttribute("tab_active_role", "true");
		return PAGE_ROLE_LIST;
	}
	
	@ResponseBody
	@GetMapping("/ajax/get/roles")
	public List<Role> getRoles() {
		return adminService.getAllRoles();
	}
	
	@ResponseBody
	@PostMapping("/ajax/add/role") 
	public Role addRole(
			Model model,
			HttpServletRequest request,
			@Valid @RequestBody RoleForm form) {
		
		Role role = new Role();
		role.setRole(form.getRoleName());
		try {
			adminService.saveRole(role);
		}
		catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role " + role.getRole() + " already exists.", e);
		}
		catch (DataAccessException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role " + role.getRole() + " could not be added.", e);
		}
		return role;
	}
	
	@ResponseBody
	@PostMapping("/ajax/edit/role") 
	public Role editRole(
			Model model,
			HttpServletRequest request,
			@Valid @RequestBody RoleForm form) {
		
		Role role = new Role();
		role.setRoleId(form.getRoleId());
		role.setRole(form.getRoleName());
		try {
			role = adminService.saveRole(role);
		}
		catch (DataRetrievalFailureException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role " + role.getRoleId() + " does not exist or could not be found.", e);
		}
		catch (DataAccessException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role " + role.getRoleId() + " could not be updated.", e);
		}
		return role;
	}
	
	@ResponseBody
	@PostMapping("/ajax/delete/role") 
	public void deleteRole(
			Model model,
			HttpServletRequest request,
			@RequestBody RoleForm form) {
		
		Role role = new Role();
		role.setRoleId(form.getRoleId());
		try {
			adminService.deleteRole(role);
		}
		catch (DataRetrievalFailureException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role " + role.getRoleId() + " does not exist or could not be found.", e);
		}
		catch (DataAccessException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role " + role.getRoleId() + " could not be deleted.", e);
		}
	}
}
