package nutent.entertainment.web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nutent.entertainment.web.entity.Role;
import nutent.entertainment.web.entity.User;
import nutent.entertainment.web.repository.RoleRepository;
import nutent.entertainment.web.repository.UserRepository;

@Service
public class AdminService {

	@Autowired
	protected RoleRepository roleRepository;
	@Autowired
	protected UserRepository userRepository;
	
	public List<Role> getAllRoles() {
		return roleRepository.findAllByOrderByRoleIdAsc();
	}
	
	public Role getRole(int id) {
		return roleRepository.getOne(id);
	}
	
	@Transactional
	public Role saveRole(Role role) {
		return roleRepository.save(role);
	}
	
	@Transactional
	public void deleteRole(Role role) {
		roleRepository.delete(role);
	}
	
	public List<User> getAllUsers() {
		return userRepository.findAllByOrderByUserIdAsc();
	}
	
	public User getUser(int id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			return user.get();
		}
		throw new DataRetrievalFailureException("User not found.");
	}
	
	@Transactional
	public User updateUser(User user) {
		User theUser = getUser(user.getUserId());
		theUser.setUsername(user.getUsername());
		theUser.getName().setFirstName(user.getName().getFirstName());
		theUser.getName().setMiddleName(user.getName().getMiddleName());
		theUser.getName().setLastName(user.getName().getLastName());
		
		return userRepository.save(theUser);
	}
	
	@Transactional
	public void deleteUser(User user) {
		userRepository.delete(user);
	}
}
