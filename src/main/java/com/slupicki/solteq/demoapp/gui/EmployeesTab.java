package com.slupicki.solteq.demoapp.gui;

import com.slupicki.solteq.demoapp.common.Util;
import com.slupicki.solteq.demoapp.dao.EmplyeeRepository;
import com.slupicki.solteq.demoapp.model.Employee;
import com.slupicki.solteq.demoapp.model.Salary;
import com.slupicki.solteq.demoapp.model.User;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class EmployeesTab extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(EmployeesTab.class);

    private final EmplyeeRepository repository;

    private List<Employee> employees;

    private final Grid<Employee> employeeGrid = new Grid<>();

    public EmployeesTab(EmplyeeRepository repository) {
        super();
        this.repository = repository;
        employees = Util.iterableToList(repository.findAll());
    }

    public void refresh() {
        removeAllComponents();
        employeeGrid.removeAllColumns();

        final HorizontalLayout gridCaptionLayout = Util.captionAndAddButton("Employees", e -> {
            Employee employee = new Employee(
                    "",
                    ""
            );
            editEmployee(employee);
        });

        final HorizontalLayout searchLayout = Util.search(s -> {
            log.info("Search by '{}'",s);
            employees = repository.findBySearchStringIsLike("%" + s.toLowerCase() + "%");
            Util.refreshGrid(employeeGrid, employees);
        });

        employeeGrid.addColumn(employee -> employees.indexOf(employee) + 1).setExpandRatio(0);
        employeeGrid.addColumn(Employee::getFirstName).setCaption("First name").setExpandRatio(1);
        employeeGrid.addColumn(Employee::getLastName).setCaption("Last name").setExpandRatio(2);
        employeeGrid.addColumn(
                employee -> employee.getLatestSalary()
                        .map(Salary::getAmount)
                        .getOrElse(BigDecimal.ZERO)
        ).setCaption("Salary").setExpandRatio(2);
        if (User.Access.ADMIN.equals(Util.currentUserAccess())) {
            employeeGrid.addComponentColumn(employee -> Util.editButton(event -> editEmployee(employee))).setExpandRatio(0);
            employeeGrid.addComponentColumn(employee -> Util.deleteButton(event -> deleteEmployee(employee))).setExpandRatio(0);
        }
        employeeGrid.setFrozenColumnCount(1);

        refreshGrid(employeeGrid);

        addComponent(gridCaptionLayout);
        addComponent(searchLayout);
        addComponent(employeeGrid);
    }

    private void editEmployee(Employee e) {
        new EmployeeEditor(e,
                (employeeToSave, editor) -> {
                    log.info("Save edit: {}", employeeToSave);
                    repository.save(employeeToSave);
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
}
