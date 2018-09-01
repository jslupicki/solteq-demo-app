package com.slupicki.solteq.demoapp.model;

import com.google.common.collect.ImmutableList;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static com.slupicki.solteq.demoapp.common.Constants.DATE_FORMATTER;
import static com.slupicki.solteq.demoapp.common.Constants.SEARCH_STRING_LENGTH;

@Entity
public class Salary {

    @Id
    private UUID id = UUID.randomUUID();

    private LocalDate fromDate;
    private LocalDate toDate;
    private BigDecimal amount;
    @ManyToOne
    private Employee employee;

    @Column(length = SEARCH_STRING_LENGTH)
    private String searchString;

    public Salary() {
    }

    public Salary(LocalDate fromDate, LocalDate toDate, BigDecimal amount) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.amount = amount;
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

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    @PreUpdate
    @PrePersist
    public void updateSearchString() {
        final String fullSearchString = StringUtils.join(
                ImmutableList.of(
                        Try.of(() -> DATE_FORMATTER.format(fromDate)).getOrElse(""),
                        Try.of(() -> DATE_FORMATTER.format(toDate)).getOrElse(""),
                        String.valueOf(amount)
                ),
                " "
        );
        searchString = StringUtils.substring(fullSearchString, 0, SEARCH_STRING_LENGTH - 1).toLowerCase();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
