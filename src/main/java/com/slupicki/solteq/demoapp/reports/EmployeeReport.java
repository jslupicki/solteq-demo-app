package com.slupicki.solteq.demoapp.reports;

import com.slupicki.solteq.demoapp.dao.EmplyeeRepository;
import com.slupicki.solteq.demoapp.model.Employee;
import com.slupicki.solteq.demoapp.model.Salary;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class EmployeeReport {

    private final EmplyeeRepository emplyeeRepository;
    private JasperReport jasperReport;

    @Autowired
    public EmployeeReport(EmplyeeRepository emplyeeRepository) throws JRException {
        this.emplyeeRepository = emplyeeRepository;
        prepareReport();
    }

    private void prepareReport() throws JRException {
        InputStream employeeReportStream = getClass().getResourceAsStream("/Employees.jrxml");
        jasperReport = JasperCompileManager.compileReport(employeeReportStream);
    }

    private JRAbstractBeanDataSource prepareDataSource() {
        List<EmployeeWrapper> employeeWrappers = StreamSupport.stream(emplyeeRepository.findAll().spliterator(), false)
                .map(EmployeeWrapper::new)
                .collect(Collectors.toList());
        return new JRBeanCollectionDataSource(employeeWrappers);
    }

    private JasperPrint printReport() throws JRException {
        JRAbstractBeanDataSource dataSource = prepareDataSource();
        Map<String, Object> parameters = new HashMap<>();
        return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
    }

    public InputStream printPDF() throws JRException {
        JasperPrint jasperPrint = printReport();
        JRPdfExporter pdfExporter = new JRPdfExporter();
        pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
        pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReportStream));
        pdfExporter.exportReport();
        return new ByteArrayInputStream(pdfReportStream.toByteArray());
    }

    public InputStream printHTML() throws JRException {
        JasperPrint jasperPrint = printReport();
        HtmlExporter htmlExporter = new HtmlExporter();
        htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        ByteArrayOutputStream htmlReportStream = new ByteArrayOutputStream();
        htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(htmlReportStream));
        htmlExporter.exportReport();
        return new ByteArrayInputStream(htmlReportStream.toByteArray());
    }

    public class EmployeeWrapper {

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
