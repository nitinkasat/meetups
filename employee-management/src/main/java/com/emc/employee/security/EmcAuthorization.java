package com.emc.employee.security;

import com.emc.employee.config.EmcSecurityConfig;
import com.emc.employee.model.Employee;
import com.emc.employee.store.EmployeeService;
import com.emc.employee.store.EmployeeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    return loggedInUser.getId().equals(employee.getId()) || loggedInUser.getId()
        .equals(employee.getReportsTo()) || authentication.getAuthorities()
        .contains(EmcSecurityConfig.ADMIN);
  }

}
