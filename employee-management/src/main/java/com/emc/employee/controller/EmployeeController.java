package com.emc.employee.controller;

import com.emc.employee.model.Employee;
import com.emc.employee.security.PostAuthorizeDirectReports;
import com.emc.employee.service.EmployeeService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600,
    allowedHeaders = {"Access-Control-Allow-Origin", "x-auth-token", "x-requested-with",
        "x-xsrf-token"})
public class EmployeeController {

  private EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeStore) {
    this.employeeService = employeeStore;
  }

  @GetMapping("/admin/all/employees")
  public @ResponseBody List<Employee> getAllEmployees() {
    return employeeService.getEmployees();
  }

  @GetMapping("/employee/{id}")
  @PostAuthorizeDirectReports
  public @ResponseBody Employee getAllEmployee(Integer employeeId) {
    return employeeService.getEmployeeById(employeeId);
  }

  @PostMapping("/employee")
  @PreAuthorize(value = "hasAnyAuthority('Admin','Mgr')")
  public @ResponseBody
  Employee addEmployee(@RequestBody Employee employee) {
    return employeeService
        .addEmployee(employee.getFirstName(), employee.getLastName(), employee.getReportsTo());
  }

  @PutMapping("/employee/name/{id}")
  public Employee updateEmployeeName(@PathVariable Integer id, @RequestParam String firstName,
      @RequestParam String lastName) {
    return employeeService.updateEmployeeName(id, firstName, lastName);
  }

  @PutMapping("/employee/reporting/{id}")
  public @ResponseBody
  Employee updateReporting(@PathVariable Integer id, @RequestParam Integer newReportingTo) {
    return employeeService.updateReporting(id, newReportingTo);
  }

}