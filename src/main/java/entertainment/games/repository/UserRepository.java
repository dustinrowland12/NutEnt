package entertainment.games.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import entertainment.games.entity.User;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {
	
	User findByUsername(String username);
	
	List<User> findAllByOrderByUserIdAsc();
	
}
