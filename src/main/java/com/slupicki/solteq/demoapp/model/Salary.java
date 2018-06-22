package com.slupicki.solteq.demoapp.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Salary {

    @Id
    private UUID id = UUID.randomUUID();

    private BigDecimal amount;
    private LocalDate fromDate;
    @ManyToOne
    private Employee employee;

    public Salary() {
    }

    public Salary(BigDecimal amount, LocalDate fromDate) {
        this.amount = amount;
        this.fromDate = fromDate;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Salary{");
        sb.append("amount=").append(amount);
        sb.append(", fromDate=").append(fromDate);
        //sb.append(", employee=").append(employee);
        sb.append('}');
        return sb.toString();
    }
}
