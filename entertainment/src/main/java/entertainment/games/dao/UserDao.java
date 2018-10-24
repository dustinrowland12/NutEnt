package entertainment.games.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import entertainment.games.dto.UserDto;
import entertainment.games.entity.User;
import entertainment.games.implement.BaseDao;
import entertainment.games.service.AuthenticationService;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
	
	User findByUsername(String username);
	/*
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		
		return users;
	}
	
	public User getUserByUsername(String username) {
		User user = null;
		try {
			TypedQuery<User> query = em.createQuery("SELECT u FROM User u where u.username = :username", User.class);
			query.setParameter("username", username);
			user = query.getSingleResult();
		}
		catch (NoResultException e) {
			
		}
		return user;
	}
	
	public User createNewUser(UserDto userDto) throws Exception {
		User user = new User();
		
		user.setName(userDto.getUser().getName());
		
		user.setPassword(AuthenticationService.hashPassword(userDto.getUser().getPassword()));
		user.setUsername(userDto.getUser().getUsername());
		Date date = new Date();
		user.setCreateDate(date);
		user.setUpdateDate(date);
		
		try {
			em.persist(user);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return user;
	*/
}
