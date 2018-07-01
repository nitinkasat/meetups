package com.emc;

import com.emc.employee.service.EmployeeService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import static com.emc.employee.config.SecurityConfig.*;

@SpringBootApplication
public class EmcApplication {

  @Autowired
  private EmployeeService employeeService;

  public static void main(String[] args) {
    SpringApplication.run(EmcApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void loadDefaultUsers() {
    employeeService
        .addEmployee("danielle", "kody", null, new ArrayList<>());
    employeeService.addEmployee("john", "david", 1, new ArrayList<>());
    List<String> managerRoleList = new ArrayList<>();
    managerRoleList.add(MGR_ROLE);
    List<String> adminRoleList = new ArrayList<>();
    adminRoleList.add(ADMIN_ROLE);
    employeeService.addEmployee("kelly", "jackson", 2, managerRoleList);
    employeeService.addEmployee("jimmy", "harless", 2, managerRoleList);
    employeeService.addEmployee("admin", "admin", null, adminRoleList);
  }
}
