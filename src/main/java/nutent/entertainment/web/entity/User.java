package nutent.entertainment.web.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import nutent.entertainment.web.dto.Auditable;

@Entity
@Table(name="USERS")
public class User extends Auditable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_gen")
	@SequenceGenerator(name = "users_gen", sequenceName = "users_seq", allocationSize = 1)
	private Integer userId;
	private String username;
	@JsonIgnore
	private String password;
	@JsonIgnore
	private String salt;
	@Embedded
	private Name name;
	private Date lastLoginDate;
	private Date passwordUpdateDate;
	private Integer unsuccessfulLoginAttempts;
	private boolean accountLocked;
	@ManyToOne
	@JoinColumn(name = "account_locked_reason_code")
	private LuAccountLockedReasonCode accountLockedReasonCode;
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="USER_ROLE", joinColumns = { @JoinColumn(name = "USER_ID",nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", nullable = false) })
    private Set<Role> roles = new HashSet<>();
	
	public User() {
		super();
		name = new Name();
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer id) {
		this.userId = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public Name getName() {
		return name;
	}
	public void setName(Name name) {
		this.name = name;
	}
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public Date getPasswordUpdateDate() {
		return passwordUpdateDate;
	}
	public void setPasswordUpdateDate(Date passwordUpdateDate) {
		this.passwordUpdateDate = passwordUpdateDate;
	}
	public Integer getUnsuccessfulLoginAttempts() {
		return unsuccessfulLoginAttempts;
	}
	public void setUnsuccessfulLoginAttempts(Integer unsuccessfulLoginAttempts) {
		this.unsuccessfulLoginAttempts = unsuccessfulLoginAttempts;
	}
	public boolean isAccountLocked() {
		return accountLocked;
	}
	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}
	public LuAccountLockedReasonCode getAccountLockedReasonCode() {
		return accountLockedReasonCode;
	}
	public void setAccountLockedReasonCode(LuAccountLockedReasonCode accountLockedReasonCode) {
		this.accountLockedReasonCode = accountLockedReasonCode;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
}