package nutent.entertainment.web;

import java.net.URI;
import java.net.URISyntaxException;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import nutent.entertainment.web.config.AuditorAwareImpl;

@SpringBootApplication
@EnableJpaAuditing
public class WebApplication {

public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}
	
	@Profile("test")
    @Bean
    public DataSource dataSource() throws URISyntaxException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        return DataSourceBuilder.create()
        		.url(dbUrl)
        		.username(username)
        		.password(password)
        		.build();
    }
	
	@Bean
	public AuditorAware<String> auditorProvider() {
	    return new AuditorAwareImpl();
	}

}
