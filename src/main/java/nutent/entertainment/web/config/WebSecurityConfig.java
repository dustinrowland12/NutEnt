package nutent.entertainment.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import nutent.entertainment.web.service.AuthenticationService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private AuthenticationService userDetailsService;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/", "/home", "/createUser").permitAll()
                .antMatchers("/games/**").hasAuthority("games")
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .failureHandler(authenticationFailureHandlerImpl())
                .and()
            .logout()
                .permitAll()
                .logoutSuccessUrl("/login");
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
          = new DaoAuthenticationProviderImpl();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }
    
    @Bean
    public PasswordEncoder encoder() {
        return new Argon2PasswordEncoder();
    }
    
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandlerImpl() {
    	return new AuthenticationFailureHandlerImpl("/login");
    }
}