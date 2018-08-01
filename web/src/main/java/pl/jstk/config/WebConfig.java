package pl.jstk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@EnableWebSecurity
@EnableGlobalMethodSecurity(
prePostEnabled = true,
securedEnabled = true,
jsr250Enabled = true)
public class WebConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests()
		.antMatchers("/*", "/books/book", "/login*","/books/search*","/delete*", "/css/*", "/img/*", "/webjars/**")
		.permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.formLogin()
		.loginPage("/login")
		.defaultSuccessUrl("/")
		.failureUrl("/login?error=true")
		.and()
		.logout()
		.permitAll()
		.logoutUrl("/logout")
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.and()
		.exceptionHandling().accessDeniedPage("/403");

		//httpSecurity.csrf().disable();
		// httpSecurity.headers().frameOptions().disable();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN")
		.and()
		.withUser("user").password(passwordEncoder().encode("user")).roles("USER")
		.and()
		.withUser("sroka").password(passwordEncoder().encode("sroka")).roles("ADMIN");
	}
	
@Override
public void addViewControllers(ViewControllerRegistry registry) {
	registry.addViewController("/403").setViewName("403");
}

}
