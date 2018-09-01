package com.slupicki.solteq.demoapp.reports;

import com.google.common.collect.Lists;
import com.slupicki.solteq.demoapp.model.Employee;
import com.slupicki.solteq.demoapp.model.Salary;

import java.math.BigDecimal;
import java.util.Collection;

public class EmployeeFactory {

    public static Collection<EmployeeWrapper> getEmployees() {
        return Lists.newArrayList(
                new EmployeeWrapper(new Employee("Jan", "Kowalski")),
                new EmployeeWrapper(new Employee("Jan", "Nowak")),
                new EmployeeWrapper(new Employee("Zdzisław", "Jeziorański"))
        );
    }

    public static class EmployeeWrapper {

        private Employee employee;

        public EmployeeWrapper(Employee employee) {
            this.employee = employee;
        }

        public String getFirstName() {
            return employee.getFirstName();
        }

        public String getLastName() {
            return employee.getLastName();
        }

        public BigDecimal getLatestSalary() {
            return employee.getLatestSalary()
                    .getOrElse(new Salary(null, null, BigDecimal.ZERO))
                    .getAmount();
        }

    }
}