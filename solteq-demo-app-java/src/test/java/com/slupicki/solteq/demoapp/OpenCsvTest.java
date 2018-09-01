package com.slupicki.solteq.demoapp;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.slupicki.solteq.demoapp.common.Util;
import com.slupicki.solteq.demoapp.dao.EmplyeeRepository;
import com.slupicki.solteq.demoapp.model.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.StringWriter;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OpenCsvTest {

    @Autowired
    private EmplyeeRepository emplyeeRepository;

    @Before
    public void setUp() {
        emplyeeRepository.deleteAll();
        Employee employee1 = new Employee("John", "Smith");
        Employee employee2 = new Employee("Donald", "Trump");
        Employee employee3 = new Employee("Michael", "Crichton");
        emplyeeRepository.save(employee1);
        emplyeeRepository.save(employee2);
        emplyeeRepository.save(employee3);
    }

    @Test
    public void employeesReport() throws Exception {
        StringWriter writer = new StringWriter();
        StatefulBeanToCsv<Employee> beanToCsv = new StatefulBeanToCsvBuilder<Employee>(writer).build();
        beanToCsv.write(Util.iterableToList(emplyeeRepository.findAll()));
        writer.close();
        System.out.println(writer.toString());
    }
}
