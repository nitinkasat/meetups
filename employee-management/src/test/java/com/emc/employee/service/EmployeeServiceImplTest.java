package com.emc.employee.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EmployeeServiceImplTest {

  @Autowired
  private EmployeeService employeeService;

  @Test(expected = AccessDeniedException.class)
  @WithMockUser(username = "nitin", authorities = {"Mgr"})
  public void addEmployeeWithMgrNotReportingTo() {
    employeeService.addEmployee("nitin", "kasat", 1);
  }

  @Test
  @WithMockUser(username = "admina", authorities = {"Admin"})
  public void addEmployeeWithAdminUser() {
    employeeService.addEmployee("nitin", "kasat", 3);
  }

  @Test
  @WithMockUser(username = "kellyj", authorities = {"Mgr"})
  public void addEmployeeWithMgrReportingTo() {
    employeeService.addEmployee("nitin", "kasat", 3);
  }

  @Test
  @WithMockUser(username = "kellyj", authorities = {"User"})
  public void updateNameWithEmployeeSelfLogin() {
    employeeService.updateEmployeeName(3, "Kelly", "Henson");
  }
}