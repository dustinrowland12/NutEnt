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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import entertainment.games.entity.Name;
import entertainment.games.entity.Role;
import entertainment.games.entity.User;
import entertainment.games.form.admin.RoleForm;
import entertainment.games.form.admin.UserForm;
import entertainment.games.service.AdminService;
import entertainment.games.service.AuthenticationService;

@Controller
@RequestMapping("admin")
public class AdminController {
	
	@Autowired
	protected AdminService adminService;
	@Autowired
	protected AuthenticationService authenticationService;
	
	//pages
	private static final String PAGE_ADMIN_CONSOLE = "admin/console";
	private static final String PAGE_ROLE_LIST = "admin/roles";
	private static final String PAGE_USER_LIST = "admin/users";
	private static final String PAGE_GAME_LIST = "admin/games";
	private static final String PAGE_ROLE_MANAGEMENT = "admin/role";
	private static final String PAGE_USER_MANAGEMENT = "admin/user";
	private static final String PAGE_GAME_MANAGEMENT = "admin/game";
	private static final String REQUEST_ATTRIBUTE_TAB_ACTIVE = "tab_active";
	
	@GetMapping("/")
	public String console(
			Model model,
			HttpServletRequest request) {
		return PAGE_ADMIN_CONSOLE;
	}
	
	@GetMapping("/roles")
	public String roles(
			Model model,
			HttpServletRequest request) {
		
		model.addAttribute(REQUEST_ATTRIBUTE_TAB_ACTIVE, "roles");
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
	
	@GetMapping("/users")
	public String users(
			Model model,
			HttpServletRequest request) {
		
		model.addAttribute(REQUEST_ATTRIBUTE_TAB_ACTIVE, "users");
		return PAGE_USER_LIST;
	}
	
	@ResponseBody
	@GetMapping("/ajax/get/users")
	public List<User> getUsers() {
		return adminService.getAllUsers();
	}
	
	@ResponseBody
	@PostMapping("/ajax/get/user")
	public User getUser(
			Model model,
			HttpServletRequest request,
			@RequestBody UserForm form) {
		return adminService.getUser(form.getUserId());
	}
	
	@ResponseBody
	@PostMapping("/ajax/add/user") 
	public User addUser(
			Model model,
			HttpServletRequest request,
			@Valid @RequestBody UserForm form) {
		
		User user = convertUserFormToUser(form);		
		try {
			authenticationService.createNewUser(user);
		}
		catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not add User " + user.getUsername() + " due to an integrity constraint.", e);
		}
		catch (DataAccessException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + user.getUsername() + " could not be added.", e);
		}
		return user;
	}
	
	@ResponseBody
	@PostMapping("/ajax/edit/user") 
	public User editUser(
			Model model,
			HttpServletRequest request,
			@Valid @RequestBody UserForm form) {
		
		User user = convertUserFormToUser(form);
		try {
			user = adminService.saveUser(user);
		}
		catch (DataRetrievalFailureException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + user.getUserId() + " does not exist or could not be found.", e);
		}
		catch (DataAccessException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + user.getUserId() + " could not be updated.", e);
		}
		return user;
	}
	
	@ResponseBody
	@PostMapping("/ajax/delete/user") 
	public void deleteUser(
			Model model,
			HttpServletRequest request,
			@RequestBody UserForm form) {
		
		User user = convertUserFormToUser(form);
		try {
			adminService.deleteUser(user);
		}
		catch (DataRetrievalFailureException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + user.getUserId() + " does not exist or could not be found.", e);
		}
		catch (DataAccessException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + user.getUserId() + " could not be deleted.", e);
		}
	}
	
	
	/*
	@GetMapping("/games")
	public String games(
			Model model,
			HttpServletRequest request) {
		
		model.addAttribute(REQUEST_ATTRIBUTE_TAB_ACTIVE, "games");
		return PAGE_GAME_LIST;
	}
	
	@ResponseBody
	@GetMapping("/ajax/get/games")
	public List<Game> getGames() {
		return adminService.getAllGames();
	}
	
	@ResponseBody
	@PostMapping("/ajax/add/game") 
	public Game addGame(
			Model model,
			HttpServletRequest request,
			@Valid @RequestBody GameForm form) {
		
		Game game = new Game();
		game.setGame(form.getGameName());
		try {
			adminService.saveGame(game);
		}
		catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game " + game.getGame() + " already exists.", e);
		}
		catch (DataAccessException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game " + game.getGame() + " could not be added.", e);
		}
		return game;
	}
	
	@ResponseBody
	@PostMapping("/ajax/edit/game") 
	public Game editGame(
			Model model,
			HttpServletRequest request,
			@Valid @RequestBody GameForm form) {
		
		Game game = new Game();
		game.setGameId(form.getGameId());
		game.setGame(form.getGameName());
		try {
			game = adminService.saveGame(game);
		}
		catch (DataRetrievalFailureException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game " + game.getGameId() + " does not exist or could not be found.", e);
		}
		catch (DataAccessException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game " + game.getGameId() + " could not be updated.", e);
		}
		return game;
	}
	
	@ResponseBody
	@PostMapping("/ajax/delete/game") 
	public void deleteGame(
			Model model,
			HttpServletRequest request,
			@RequestBody GameForm form) {
		
		Game game = new Game();
		game.setGameId(form.getGameId());
		try {
			adminService.deleteGame(game);
		}
		catch (DataRetrievalFailureException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game " + game.getGameId() + " does not exist or could not be found.", e);
		}
		catch (DataAccessException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game " + game.getGameId() + " could not be deleted.", e);
		}
	}
	*/
	
	private User convertUserFormToUser(UserForm form) {
		User user = new User();
		user.setUserId(form.getUserId());
		user.setUsername(form.getUsername());
		user.setPassword(form.getPassword());
		
		Name name = new Name();
		name.setFirstName(form.getFirstName());
		name.setMiddleName(form.getMiddleName());
		name.setLastName(form.getLastName());
		user.setName(name);
		
		return user;
	}
}
