package entertainment.games.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import entertainment.games.entity.Role;
import entertainment.games.repository.RoleRepository;

@Service
public class AdminService {

	@Autowired
	protected RoleRepository roleRepository;
	
	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}
	
	public Role getRole(int id) {
		return roleRepository.getOne(id);
	}
	
	@Transactional
	public Role addRole(Role role) {
		Role newRole = null;
		newRole = roleRepository.save(role);
		return newRole;
	}
	
	@Transactional
	public void deleteRole(Role role) {
		roleRepository.delete(role);
	}
}
