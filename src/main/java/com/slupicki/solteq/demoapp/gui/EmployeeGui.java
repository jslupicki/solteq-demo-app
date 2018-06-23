package com.slupicki.solteq.demoapp.gui;

import com.slupicki.solteq.demoapp.common.Util;
import com.slupicki.solteq.demoapp.dao.EmplyeeRepository;
import com.slupicki.solteq.demoapp.model.Employee;
import com.slupicki.solteq.demoapp.model.Salary;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import java.math.BigDecimal;
import java.util.List;

@SpringUI
@Push
public class EmployeeGui extends UI {

    private static final Logger log = LoggerFactory.getLogger(EmployeeGui.class);

    private final EmplyeeRepository repository;

    private List<Employee> employees;

    private final Grid<Employee> employeeGrid = new Grid<>();

    @Autowired
    public EmployeeGui(EmplyeeRepository repository) {
        this.repository = repository;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        employees = Util.iterableToList(repository.findAll());

        final VerticalLayout layout = new VerticalLayout();

        final HorizontalLayout gridCaptionLayout = Util.captionAndAddButton("Employees", e -> {
            Employee employee = new Employee(
                    "",
                    ""
            );
            editEmployee(employee);
        });

        employeeGrid.addColumn(employee -> employees.indexOf(employee) + 1).setExpandRatio(0);
        employeeGrid.addColumn(Employee::getFirstName).setCaption("First name").setExpandRatio(1);
        employeeGrid.addColumn(Employee::getLastName).setCaption("Last name").setExpandRatio(2);
        employeeGrid.addColumn(
                employee -> employee.getLatestSalary()
                        .map(Salary::getAmount)
                        .getOrElse(BigDecimal.ZERO)
        ).setCaption("Salary").setExpandRatio(2);
        employeeGrid.addComponentColumn(employee -> Util.editButton(event -> editEmployee(employee))).setExpandRatio(0);
        employeeGrid.addComponentColumn(employee -> Util.deleteButton(event -> deleteEmployee(employee))).setExpandRatio(0);
        employeeGrid.setFrozenColumnCount(1);

        layout.addComponent(gridCaptionLayout);
        layout.addComponent(employeeGrid);

        setContent(layout);

        refreshGrid(employeeGrid);
    }

    private void editEmployee(Employee e) {
        new EmployeeEditor(e,
                (employeeToSave, editor) -> {
                    log.info("Save edit: {}", e);
                    repository.save(e);
                    refreshGrid();
                },
                (employeeCanceled, editor) -> log.info("Cancel edit: {}", employeeCanceled)
        ).show();
    }

    private void deleteEmployee(Employee e) {
        new YesNoWindow("Are you sure?",
                event -> {
                    log.info("Delete: {}", e);
                    repository.delete(e);
                    refreshGrid();
                },
                event -> log.info("NO delete: {}", e)
        ).show();
    }

    private void refreshGrid() {
        refreshGrid(employeeGrid);
    }

    private void refreshGrid(Grid<Employee> grid) {
        employees = Util.iterableToList(repository.findAll());
        Util.refreshGrid(grid, employees);
    }

    @WebServlet(urlPatterns = "/*", name = "EmpleyeeGUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = EmployeeGui.class, productionMode = false)
    public static class EmpleyeeGUIServlet extends VaadinServlet {
    }
}
