package com.emc.employee.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

  private Integer reportsTo;
  private String firstName;
  private String lastName;
  private Integer id;
  private String userName;
  private List<String> roles = new ArrayList<>();

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Employee)) {
      return false;
    }
    final Employee other = (Employee) o;
    if (!other.canEqual((Object) this)) {
      return false;
    }
    final Object this$id = this.getId();
    final Object other$id = other.getId();
    if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
      return false;
    }
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $id = this.getId();
    result = result * PRIME + ($id == null ? 43 : $id.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Employee;
  }
}
