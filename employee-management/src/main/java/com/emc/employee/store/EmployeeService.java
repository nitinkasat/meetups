package com.emc.employee.store;

import com.emc.employee.model.Employee;
import java.util.List;

public interface EmployeeService {

  List<Employee> getEmployees();

  Employee getEmployeeById(Integer employeeId);

  Employee getEmployeeByUserName(String username);

  Employee addEmployee(String firstName, String lastName, Integer reportsTo);

  Employee updateEmployeeName(Integer employeeId, String firstName, String lastName);

  Employee updateReporting(Integer employeeId, Integer newReportingTo);

  void delete(Integer id);

}