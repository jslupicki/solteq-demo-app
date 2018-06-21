package com.slupicki.solteq.demoapp.gui;

import com.slupicki.solteq.demoapp.dao.EmplyeeRepository;
import com.slupicki.solteq.demoapp.model.Employee;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SpringUI
@Push
public class EmployeeGui extends UI {

    private static final Logger log = LoggerFactory.getLogger(EmployeeGui.class);

    @Autowired
    private EmplyeeRepository repository;

    private List<Employee> employees;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        employees = iterableToList(repository.findAll());

        final VerticalLayout layout = new VerticalLayout();
        final Grid<Employee> employeeGrid = new Grid<>();

        final TextField firstNameTf = new TextField();
        firstNameTf.setCaption("First name:");
        final TextField lastNameTf = new TextField();
        lastNameTf.setCaption("Last name:");

        Button addBtn = new Button("Add");
        addBtn.addClickListener(e -> {
            Employee employee = new Employee(
                    firstNameTf.getValue(),
                    lastNameTf.getValue(),
                    BigDecimal.ZERO
            );
            repository.save(employee);
            refreshGrid(employeeGrid);
            firstNameTf.setValue("");
            lastNameTf.setValue("");
            layout.addComponent(new Label("You have " + repository.count() + " employees"));
        });

        employeeGrid.addColumn(employee -> employees.indexOf(employee) + 1).setExpandRatio(0);
        employeeGrid.addColumn(Employee::getFirstName).setCaption("First name").setExpandRatio(1);
        employeeGrid.addColumn(Employee::getLastName).setCaption("Last name").setExpandRatio(2);
        employeeGrid.addColumn(Employee::getSalary).setCaption("Salary").setExpandRatio(1);
        employeeGrid.addComponentColumn(this::editButton).setExpandRatio(0);
        employeeGrid.addComponentColumn(this::deleteButton).setExpandRatio(0);
        employeeGrid.setFrozenColumnCount(1);

        layout.addComponent(firstNameTf);
        layout.addComponent(lastNameTf);
        layout.addComponent(addBtn);
        layout.addComponent(employeeGrid);

        setContent(layout);

        refreshGrid(employeeGrid);
    }

    private Button editButton(Employee e) {
        Button button = new Button(VaadinIcons.EDIT);
        button.addStyleName(ValoTheme.BUTTON_SMALL);
        button.addClickListener(event -> editEmployee(e));
        return button;
    }

    private void editEmployee(Employee e) {
        log.info("Edit: {}", e);
    }

    private Button deleteButton(Employee e) {
        Button button = new Button(VaadinIcons.DEL);
        button.addStyleName(ValoTheme.BUTTON_SMALL);
        button.addClickListener(event -> deleteEmployee(e));
        return button;
    }

    private void deleteEmployee(Employee e) {
        Window window = new Window("Test");
        window.setWidth(300.0f, Unit.PIXELS);
        final FormLayout content = new FormLayout();
        content.setMargin(true);
        window.setContent(content);
        this.addWindow(window);
        log.info("Delete: {}", e);
    }

    private void refreshGrid(Grid<Employee> grid) {
        employees = iterableToList(repository.findAll());
        refreshGrid(grid, employees);
    }

    private void refreshGrid(Grid<Employee> grid, List<Employee> employees) {
        grid.setItems(employees);
    }

    private <T> List<T> iterableToList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    @WebServlet(urlPatterns = "/*", name = "EmpleyeeGUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = EmployeeGui.class, productionMode = false)
    public static class EmpleyeeGUIServlet extends VaadinServlet {
    }
}
