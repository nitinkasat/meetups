package com.emc.employee.security;

import com.emc.employee.config.SecurityConfig;
import com.emc.employee.model.Employee;
import com.emc.employee.service.EmployeeService;
import java.util.Collection;
import java.util.Objects;
import javax.sql.RowSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class EmcAuthorization {

  private final EmployeeService employeeService;

  @Autowired
  public EmcAuthorization(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  public boolean ifDirectReports(Authentication authentication, Integer employeeId) {
    Employee employee = employeeService.getEmployeeById(employeeId);
    User user = (User) authentication.getPrincipal();
    String userName = user.getUsername();
    Employee loggedInUser = employeeService.getEmployeeByUserName(userName);
    if (loggedInUser != null) {
      Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
      return loggedInUser.getId().equals(employee.getId()) || (loggedInUser.getId()
          .equals(employee.getReportsTo()) && authorities
          .contains(new SimpleGrantedAuthority(SecurityConfig.MGR_ROLE)))
          || authorities.contains(new SimpleGrantedAuthority(SecurityConfig.ADMIN_ROLE));
    }
    return false;
  }

  public boolean checkReportingTo(Authentication authentication, Integer directReportingTo) {
    User loggedInUser = (User) authentication.getPrincipal();
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    boolean isAdmin = authorities.contains(new SimpleGrantedAuthority(SecurityConfig.ADMIN_ROLE));
    Employee loggedInEmployee = employeeService.getEmployeeByUserName(loggedInUser.getUsername());
    if (!isAdmin && Objects.nonNull(loggedInEmployee)) {
      return loggedInEmployee.getId().equals(directReportingTo);
    }
    return isAdmin;
  }

}
