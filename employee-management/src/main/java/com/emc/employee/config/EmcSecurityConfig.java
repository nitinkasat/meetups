package com.emc.employee.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class EmcSecurityConfig extends WebSecurityConfigurerAdapter {

  public static final String ADMIN_ROLE = "Admin";
  public static final String MGR_ROLE = "Mgr";
  public static final String INDIVIDUAL_CONTRIBUTOR_ROLE = "Ic";
  private List<UserDetails> users = new ArrayList<>();

  public static final GrantedAuthority ADMIN = new SimpleGrantedAuthority(ADMIN_ROLE);
  public static final GrantedAuthority MANAGER = new SimpleGrantedAuthority(MGR_ROLE);
  public static final GrantedAuthority IC = new SimpleGrantedAuthority(INDIVIDUAL_CONTRIBUTOR_ROLE);
  @Getter
  private InMemoryUserDetailsManager userDetailsManager=new InMemoryUserDetailsManager();

  public EmcSecurityConfig() {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    users.add(new User("daniellek", encoder.encode("daniellek"),
        Arrays.asList(new GrantedAuthority[]{ADMIN})));
    users
        .add(new User("johna", encoder.encode("johna"), Arrays.asList(new GrantedAuthority[]{IC})));
    users.add(new User("kellyj", encoder.encode("kellyj"),
        Arrays.asList(new GrantedAuthority[]{IC, MANAGER})));
    users.add(new User("tomh", encoder.encode("tomh"), Arrays.asList(new GrantedAuthority[]{IC})));
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
    users.forEach(userDetails -> userDetailsManager.createUser(userDetails));
    auth.userDetailsService(userDetailsManager).passwordEncoder(getPasswordEncoder());
  }

  @Bean
  public BCryptPasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}