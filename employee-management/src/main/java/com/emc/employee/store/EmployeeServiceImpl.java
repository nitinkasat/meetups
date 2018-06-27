package com.emc.employee.store;

import com.emc.employee.config.EmcSecurityConfig;
import com.emc.employee.model.Employee;
import com.emc.employee.security.PostAuthorizeDirectReports;
import com.emc.employee.security.PreAuthorizeDirectReports;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

  public static final Random RANDOM = new Random();
  private final InMemoryUserDetailsManager userDetailsManager;
  private final BCryptPasswordEncoder encoder;
  private List<Employee> employees = new ArrayList<>();
  private AtomicInteger id = new AtomicInteger(1);

  @Autowired
  public EmployeeServiceImpl(EmcSecurityConfig emcSecurityConfig,
      BCryptPasswordEncoder encoder) {
    employees.add(new Employee(null, "Danielle", "Kody", id.getAndIncrement(), "daniellek"));
    int reportsToDanielle = id.get();
    employees
        .add(new Employee(reportsToDanielle, "John", "Anderson", id.getAndIncrement(), "johna"));
    employees
        .add(new Employee(reportsToDanielle, "Kelly", "Jackson", id.getAndIncrement(), "kellyj"));
    employees.add(new Employee(null, "Tom", "Henson", id.getAndIncrement(), "tomh"));
    this.userDetailsManager = emcSecurityConfig.getUserDetailsManager();
    this.encoder = encoder;
  }

  @Override
  public List<Employee> getEmployees() {
    return employees;
  }

  @Override
  @PostAuthorizeDirectReports
  public Employee addEmployee(String firstName, String lastName, Integer reportsTo) {
    String userName = firstName.toLowerCase().concat(("" + lastName.charAt(0)).toLowerCase());
    Employee employee = new Employee(reportsTo, firstName, lastName, id.getAndIncrement(),
        userName);
    if (userDetailsManager.userExists(userName)) {
      userName = userName + RANDOM.nextInt();
    }
    User user = new User(userName, encoder.encode(userName), Arrays.asList(EmcSecurityConfig.IC));
    userDetailsManager.createUser(user);
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
    return employees.stream().filter(emp -> emp.getUserName().equals(username)).findFirst().get();
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