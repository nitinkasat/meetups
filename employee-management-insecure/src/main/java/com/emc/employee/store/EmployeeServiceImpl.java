package com.emc.employee.store;

import com.emc.employee.model.Employee;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private List<Employee> employees = new ArrayList<>();

  private AtomicInteger id = new AtomicInteger(1);

  public EmployeeServiceImpl() {
    employees.add(new Employee(null, "Danielle", "Kody", id.getAndIncrement()));
    int reportsToDanielle = id.get();
    employees.add(new Employee(reportsToDanielle, "John", "Anderson", id.getAndIncrement()));
    employees.add(new Employee(reportsToDanielle, "Kelly", "Jackson", id.getAndIncrement()));
    employees.add(new Employee(null, "Steve", "Smith", id.getAndIncrement()));
  }

  @Override
  public List<Employee> getEmployees() {
    return employees;
  }

  @Override
  public Employee addEmployee(String firstName, String lastName, Integer reportsTo) {
    Employee employee = new Employee(reportsTo, firstName, lastName, id.getAndIncrement());
    employees.add(employee);
    return employee;
  }

  @Override
  public Employee updateEmployeeName(Integer employeeId, String firstName, String lastName) {
    Employee employee = getEmployeeById(employeeId);
    employee.setFirstName(firstName);
    employee.setLastName(lastName);
    return employee;
  }

  private Employee getEmployeeById(Integer employeeId) {
    return employees.stream().filter(emp -> emp.getId().equals(employeeId)).findFirst().get();
  }

  @Override
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