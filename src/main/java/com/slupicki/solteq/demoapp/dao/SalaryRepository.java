package com.slupicki.solteq.demoapp.dao;

import com.slupicki.solteq.demoapp.model.Salary;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface SalaryRepository extends CrudRepository<Salary, UUID> {
}
