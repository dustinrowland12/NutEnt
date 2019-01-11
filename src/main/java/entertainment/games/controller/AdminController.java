package entertainment.games.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
			@ModelAttribute RoleForm form) {
		
		Role newRole = new Role();
		newRole.setRole(form.getRole());
		newRole = adminService.addRole(newRole);
		return newRole;
	}
	
	@ResponseBody
	@PostMapping("/ajax/delete/role") 
	public void deleteRole(
			Model model,
			HttpServletRequest request,
			@ModelAttribute RoleForm form) {
		
		Role role = new Role();
		role.setRoleId(form.getRole_id());
		role.setRole(form.getRole());
		adminService.deleteRole(role);
	}
}
