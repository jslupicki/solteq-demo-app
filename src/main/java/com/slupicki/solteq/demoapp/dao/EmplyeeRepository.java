package com.slupicki.solteq.demoapp.dao;

import com.slupicki.solteq.demoapp.model.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EmplyeeRepository extends CrudRepository<Employee, UUID> {
}
