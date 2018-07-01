package com.emc.employee.service;

import com.emc.employee.config.SecurityConfig;
import com.emc.employee.model.Employee;
import com.emc.employee.security.PostAuthorizeDirectReports;
import com.emc.employee.security.PreAuthorizeDirectReports;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service("employeeService")
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

  public static final Random RANDOM = new Random();
  private final InMemoryUserDetailsManager userDetailsManager;
  private final BCryptPasswordEncoder encoder;
  private List<Employee> employees = new ArrayList<>();
  private AtomicInteger id = new AtomicInteger(1);

  @Autowired
  public EmployeeServiceImpl(SecurityConfig securityConfig,
      BCryptPasswordEncoder encoder) {
    this.userDetailsManager = securityConfig.getUserDetailsManager();
    this.encoder = encoder;
  }


  @Override
  public List<Employee> getEmployees() {
    return employees;
  }

  @Override
  @PreAuthorize("@emcAuthorization.checkReportingTo(authentication,#reportsTo)")
  public Employee addEmployee(String firstName, String lastName, Integer reportsTo) {
    return addEmployee(firstName, lastName, reportsTo,
        Collections.singletonList(SecurityConfig.USER));
  }

  private void addUserToSecurityContext(String userName, List<String> roles) {
    List authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role))
        .collect(Collectors.toList());
    User user = new User(userName, encoder.encode(userName), authorities);
    userDetailsManager.createUser(user);
  }

  @Override
  public Employee addEmployee(String firstName, String lastName, Integer reportsTo,
      List<String> roles) {
    String userName = firstName.toLowerCase().concat(("" + lastName.charAt(0)).toLowerCase());
    if (!roles.contains(SecurityConfig.USER)) {
      roles.add(SecurityConfig.USER);
    }
    Employee employee = new Employee(reportsTo, firstName, lastName, id.getAndIncrement(),
        userName, roles);
    if (userDetailsManager.userExists(userName)) {
      userName = userName + RANDOM.nextInt();
    }
    addUserToSecurityContext(userName, roles);
    employee.setUserName(userName);
    employees.add(employee);
    return employee;
  }

  @Override
  @PreAuthorizeDirectReports
  public Employee updateEmployeeName(Integer id, String firstName, String lastName) {
    Employee employee = getEmployeeById(id);
    employee.setFirstName(firstName);
    employee.setLastName(lastName);
    return employee;
  }

  public Employee getEmployeeById(Integer employeeId) {
    return employees.stream().filter(emp -> emp.getId().equals(employeeId)).findFirst().get();
  }

  @Override
  public Employee getEmployeeByUserName(String username) {
    log.info(Arrays.toString(employees.toArray()));
    Optional<Employee> first = employees.stream().filter(emp -> emp.getUserName().equals(username))
        .findFirst();
    return first.isPresent() ? first.get() : null;
  }

  @Override
  @PreAuthorizeDirectReports
  public Employee updateReporting(Integer employeeId, Integer newReportingTo) {
    Employee employee = getEmployeeById(employeeId);
    employee.setReportsTo(newReportingTo);
    return employee;
  }

  @Override
  public void delete(Integer id) {
    employees.remove(getEmployeeById(id));
  }

}