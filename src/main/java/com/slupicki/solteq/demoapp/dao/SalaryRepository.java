package com.slupicki.solteq.demoapp.dao;

import com.slupicki.solteq.demoapp.model.Salary;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface SalaryRepository extends PagingAndSortingRepository<Salary, UUID> {
}
