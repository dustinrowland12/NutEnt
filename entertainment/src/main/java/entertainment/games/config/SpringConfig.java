package entertainment.games.config;

import java.util.Properties;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@EnableJpaRepositories
@ComponentScan(basePackages = { 
		"entertainment.games.controller", 
		"entertainment.games.service",
		"entertainment.games.dao"})
public class SpringConfig implements WebMvcConfigurer {
	@Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
			.addResourceLocations("/resources/");
	}
	
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver bean = new InternalResourceViewResolver();
		bean.setPrefix("/WEB-INF/jsp/");
		bean.setSuffix(".jsp");
		return bean;
	}
	
	@Bean
	public DataSource dataSource() {
		try {
			return (DataSource) new JndiTemplate().lookup("java:comp/env/jdbc/NutmegDB");
		}
		catch (NamingException e) {
			return null;
		}
	}
	
	@Bean EntityManagerFactory entityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(false);
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("entertainment.games.entity");
		factory.setDataSource(dataSource());
		Properties jpaProperties = new Properties();
		// Hibernate Specific Properties
		jpaProperties.setProperty("hibernate.physical_naming_strategy", "entertainment.games.config.PhysicalNamingStrategyImpl");
		jpaProperties.setProperty("hibernate.show_sql", "true");
		factory.setJpaProperties(jpaProperties);
		factory.afterPropertiesSet();
		
		return factory.getObject();
	}
}