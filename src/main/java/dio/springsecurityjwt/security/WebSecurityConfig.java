package dio.springsecurityjwt.security;

import javax.servlet.http.HttpServlet;

import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
  @Bean
  BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  SecurityFilterChain web(HttpSecurity http) throws Exception {
    http.headers().frameOptions().disable();
    http.cors().and().csrf().disable()
      .addFilterAfter(new JWTFilter(), UsernamePasswordAuthenticationFilter.class)
      .authorizeRequests()
      .antMatchers("/h2-console/**").permitAll()
      .antMatchers(HttpMethod.POST, "/login").permitAll()
      .antMatchers(HttpMethod.POST, "/users").permitAll()
      .antMatchers(HttpMethod.GET, "/users").hasAnyRole("USERS", "MANAGERS")
      .antMatchers("/managers").hasAnyRole("MANAGERS")
      .anyRequest().authenticated().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    return http.build();
  }

  @Bean
  ServletRegistrationBean h2servletRegistration() {
    ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new WebServlet());    
    servletRegistrationBean.addUrlMappings("/h2-console/*");
    return servletRegistrationBean;
  }
}
