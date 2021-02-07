package nutent.entertainment.web.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import nutent.entertainment.web.entity.User;

public class UserDetailsImpl implements UserDetails {
	private Collection<SimpleGrantedAuthority> authorities;
    private String username;
    private String password;
    private Boolean enabled = true;
    private Boolean accountNonLocked = true;
 
    public UserDetailsImpl(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.accountNonLocked = user.isAccountLocked() == false;
        authorities = new ArrayList<>();
        if (user.getRoles() != null) {
        	user.getRoles().forEach(role -> {
        		authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
            });
        }
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.enabled;
	}
	
	public void setAuthorities(Collection<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
 
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

}