package nutent.entertainment.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import nutent.entertainment.web.entity.Role;

@Repository
@Transactional(readOnly = true)
public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	Role findByRoleName(String roleName);
	
	List<Role> findAllByOrderByRoleIdAsc();	
	
}
