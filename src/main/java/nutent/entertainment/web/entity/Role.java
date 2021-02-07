package nutent.entertainment.web.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_gen")
	@SequenceGenerator(name = "roles_gen", sequenceName = "role_seq", allocationSize = 1)
	private Integer roleId;
 
    private String roleName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="USER_ROLE", joinColumns = { @JoinColumn(name = "ROLE_ID",nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "USER_ID", nullable = false) })
    private Set<User> users = new HashSet<>();
 
    public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
    
    public String getRoleName() {
        return roleName;
    }
    
	public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

	@JsonIgnore
    public Set<User> getUsers() {
        return users;
    }
 
    public void setUsers(Set<User> users) {
        this.users = users;
    }
 
    public void addUser(User user){
        if(!this.users.contains(user)){
            this.users.add(user);
        }
 
        if(!user.getRoles().contains(this)){
            user.getRoles().add(this);
        }
    }
 
    public void removeUser(User user){
        this.users.remove(user);
        user.getRoles().remove(this);
    }
 
}