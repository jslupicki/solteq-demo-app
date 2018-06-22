package com.slupicki.solteq.demoapp.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class ContactInfo {

    @Id
    private UUID id = UUID.randomUUID();

    private LocalDate fromDate;
    private String phone;
    @ManyToOne
    private Employee employee;

    public ContactInfo() {
    }

    public ContactInfo(LocalDate fromDate, String phone) {
        this.fromDate = fromDate;
        this.phone = phone;
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ContactInfo{");
        sb.append("id=").append(id);
        sb.append(", fromDate=").append(fromDate);
        sb.append(", phone='").append(phone).append('\'');
        //sb.append(", employee=").append(employee);
        sb.append('}');
        return sb.toString();
    }
}
