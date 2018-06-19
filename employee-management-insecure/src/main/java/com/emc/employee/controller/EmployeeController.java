package com.emc.employee.controller;

import com.emc.employee.model.Employee;
import com.emc.employee.store.EmployeeService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

  private EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeStore) {
    this.employeeService = employeeStore;
  }

  @GetMapping("/all")
  public List<Employee> getAllEmployees() {
    return employeeService.getEmployees();
  }

  @PostMapping
  public Employee addEmployee(@RequestBody Employee employee) {
    return employeeService
        .addEmployee(employee.getFirstName(), employee.getLastName(), employee.getReportsTo());
  }

  @PutMapping("/name/{id}")
  public Employee updateEmployeeName(@PathVariable Integer id, @RequestParam String firstName,
      @RequestParam String lastName) {
    return employeeService.updateEmployeeName(id, firstName, lastName);
  }

  @PutMapping("/reporting/{id}")
  public Employee updateReporting(@PathVariable Integer id, @RequestParam Integer newReportingTo) {
    return employeeService.updateReporting(id, newReportingTo);
  }

}