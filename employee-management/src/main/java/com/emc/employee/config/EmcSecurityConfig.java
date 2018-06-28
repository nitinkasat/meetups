package com.emc.employee.config;

import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class EmcSecurityConfig extends WebSecurityConfigurerAdapter {

  public static final String ADMIN_ROLE = "Admin";
  public static final String MGR_ROLE = "Mgr";
  public static final String USER = "User";
  @Getter
  private InMemoryUserDetailsManager userDetailsManager=new InMemoryUserDetailsManager();

  public EmcSecurityConfig() {
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .httpBasic()
        .and()
        .authorizeRequests()
        .mvcMatchers("/employee/admin/**").hasAuthority(ADMIN_ROLE)
        .anyRequest().authenticated().and().csrf().disable();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsManager).passwordEncoder(getPasswordEncoder());
  }

  @Bean
  public BCryptPasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}