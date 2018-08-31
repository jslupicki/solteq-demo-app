package com.slupicki.solteq.demoapp.reports;

import com.google.common.collect.Lists;
import com.slupicki.solteq.demoapp.model.Employee;

import java.util.Collection;

public class EmployeeFactory {

    public static Collection<EmployeeDTO> getEmployees() {
        return Lists.newArrayList(
                new EmployeeDTO(new Employee("Jan", "Kowalski")),
                new EmployeeDTO(new Employee("Jan", "Nowak")),
                new EmployeeDTO(new Employee("Zdzisław", "Jeziorański"))
        );
    }
}
