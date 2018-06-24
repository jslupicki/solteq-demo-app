package com.slupicki.solteq.demoapp.dao;

import com.slupicki.solteq.demoapp.model.Salary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SalaryRepository extends PagingAndSortingRepository<Salary, UUID> {
    List<Salary> findByFromDateIsLessThanEqualAndToDateIsGreaterThan(LocalDate to, LocalDate from);

    @Query("select min(s.fromDate) from Salary s")
    LocalDate findFirstDate();
    @Query("select max(s.toDate) from Salary s")
    LocalDate findLatestDate();
}
