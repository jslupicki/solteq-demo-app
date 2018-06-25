package com.slupicki.solteq.demoapp.dao;

import com.slupicki.solteq.demoapp.model.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface EmplyeeRepository extends PagingAndSortingRepository<Employee, UUID> {

    List<Employee> findBySearchStringIsLike(String searchString);
}
