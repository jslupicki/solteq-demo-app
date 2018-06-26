package com.slupicki.solteq.demoapp.exports;

import com.google.common.base.Charsets;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.slupicki.solteq.demoapp.common.Util;
import com.slupicki.solteq.demoapp.dao.EmplyeeRepository;
import com.slupicki.solteq.demoapp.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

@Component
public class EmployeeCsvExport {

    private final EmplyeeRepository emplyeeRepository;

    @Autowired
    public EmployeeCsvExport(EmplyeeRepository emplyeeRepository) {
        this.emplyeeRepository = emplyeeRepository;
    }

    public InputStream generate() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
        StringWriter writer = new StringWriter();
        StatefulBeanToCsv<Employee> beanToCsv = new StatefulBeanToCsvBuilder<Employee>(writer).build();
        beanToCsv.write(Util.iterableToList(emplyeeRepository.findAll()));
        writer.close();
        return new ByteArrayInputStream(Charsets.UTF_8.encode(writer.toString()).array());
    }
}
