package com.slupicki.solteq.demoapp.model;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.opencsv.bean.CsvBindByName;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.slupicki.solteq.demoapp.common.Constants.SEARCH_STRING_LENGTH;

@Entity
public class Employee {

    private static Comparator<Salary> fromLatestSalary = (o1, o2) -> o2.getFromDate().compareTo(o1.getFromDate());
    private static Comparator<ContactInfo> fromLatestContactInfo = (o1, o2) -> o2.getFromDate().compareTo(o1.getFromDate());

    @Id
    private UUID id = UUID.randomUUID();

    @CsvBindByName
    private String firstName;
    @CsvBindByName
    private String lastName;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Salary> salaries = Sets.newHashSet();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ContactInfo> contactInfos = Sets.newHashSet();

    @Column(length = SEARCH_STRING_LENGTH)
    private String searchString;

    public Employee() {
    }

    public Employee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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

    public Set<Salary> getSalaries() {
        return salaries;
    }

    public void setSalaries(Set<Salary> salaries) {
        this.salaries = salaries;
    }

    public Set<ContactInfo> getContactInfos() {
        return contactInfos;
    }

    public void setContactInfos(Set<ContactInfo> contactInfos) {
        this.contactInfos = contactInfos;
    }

    public Option<Salary> getLatestSalary() {
        return Try.of(() -> getSortedSalaries().get(0)).toOption();
    }

    public void addSalary(Salary salary) {
        if (salaries == null)
            salaries = new HashSet<>();
        salaries.add(salary);
    }

    public List<Salary> getSortedSalaries() {
        return salaries.stream().sorted(fromLatestSalary).collect(Collectors.toList());
    }

    public Option<ContactInfo> getLatestContactInfo() {
        return Try.of(() -> getSortedContactInfos().get(0)).toOption();
    }

    public void addContactInfo(ContactInfo contactInfo) {
        if (contactInfos == null)
            contactInfos = new HashSet<>();
        contactInfos.add(contactInfo);
    }

    public List<ContactInfo> getSortedContactInfos() {
        return contactInfos.stream().sorted(fromLatestContactInfo).collect(Collectors.toList());
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
        for (Salary salary : salaries) {
            salary.updateSearchString();
        }
        for (ContactInfo contactInfo : contactInfos) {
            contactInfo.updateSearchString();
        }
        final String fullSearchString = StringUtils.join(
                ImmutableList.of(
                        firstName,
                        lastName,
                        Try.of(() -> getLatestSalary().get().getSearchString()).getOrElse(""),
                        Try.of(() -> getLatestContactInfo().get().getSearchString()).getOrElse("")
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
