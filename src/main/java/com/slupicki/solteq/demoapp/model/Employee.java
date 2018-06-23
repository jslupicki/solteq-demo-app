package com.slupicki.solteq.demoapp.model;


import io.vavr.Lazy;
import io.vavr.control.Option;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Employee {

    private static Comparator<Salary> fromLatestSalary = (o1, o2) -> o2.getFromDate().compareTo(o1.getFromDate());
    private static Comparator<ContactInfo> fromLatestContactInfo = (o1, o2) -> o2.getFromDate().compareTo(o1.getFromDate());

    @Id
    private UUID id = UUID.randomUUID();

    private String firstName;
    private String lastName;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Salary> salaries;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ContactInfo> contactInfos;

    @Transient
    private Lazy<Option<Salary>> latestSalary = Lazy.of(() ->
            Option.ofOptional(salaries.stream()
                    .filter(salary -> LocalDate.now().isAfter(salary.getFromDate())) // Only salaries from past
                    .sorted(fromLatestSalary)
                    .findFirst())
    );
    @Transient
    private Lazy<Option<ContactInfo>> latestContactInfo = Lazy.of(() ->
            Option.ofOptional(contactInfos.stream()
                    .filter(contactInfo -> LocalDate.now().isAfter(contactInfo.getFromDate())) // Only contact info from past
                    .sorted(fromLatestContactInfo)
                    .findFirst())
    );

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
        return latestSalary.get();
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
        return latestContactInfo.get();
    }

    public void addContactInfo(ContactInfo contactInfo) {
        if (contactInfos == null)
            contactInfos = new HashSet<>();
        contactInfos.add(contactInfo);
    }

    public List<ContactInfo> getSortedContactInfos() {
        return contactInfos.stream().sorted(fromLatestContactInfo).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Employee{");
        sb.append("id=").append(id);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", salaries=").append(salaries);
        sb.append(", contactInfos=").append(contactInfos);
        sb.append('}');
        return sb.toString();
    }
}
