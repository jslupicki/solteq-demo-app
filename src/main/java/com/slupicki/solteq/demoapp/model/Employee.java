package com.slupicki.solteq.demoapp.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Employee {

    @Id
    private UUID id = UUID.randomUUID();

    private String firstName;
    private String lastName;
    private BigDecimal salary;

    public Employee() {
    }

    public Employee(String firstName, String lastName, BigDecimal salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Employee{");
        sb.append("id=").append(id);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", salary=").append(salary);
        sb.append('}');
        return sb.toString();
    }
}
