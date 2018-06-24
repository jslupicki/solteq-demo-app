package com.slupicki.solteq.demoapp.model;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

import static com.slupicki.solteq.demoapp.common.Constants.DATE_FORMATTER;
import static com.slupicki.solteq.demoapp.common.Constants.SEARCH_STRING_LENGTH;

@Entity
public class ContactInfo {

    @Id
    private UUID id = UUID.randomUUID();

    private LocalDate fromDate;
    private String phone;
    @ManyToOne
    private Employee employee;

    @Column(length = SEARCH_STRING_LENGTH)
    private String searchString;

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
                        DATE_FORMATTER.format(fromDate),
                        phone
                ),
                " "
        );
        searchString = StringUtils.substring(fullSearchString, 0, SEARCH_STRING_LENGTH - 1);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
