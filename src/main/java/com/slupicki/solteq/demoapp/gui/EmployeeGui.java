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
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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

    private final Grid<Employee> employeeGrid = new Grid<>();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        employees = iterableToList(repository.findAll());

        final VerticalLayout layout = new VerticalLayout();

        final Label employeesLbl = new Label("Employees");
        Button addBtn = new Button("Add");
        addBtn.addClickListener(e -> {
            Employee employee = new Employee(
                    "",
                    "",
                    BigDecimal.ZERO
            );
            editEmployee(employee);
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout(
                employeesLbl,
                addBtn
        );

        employeeGrid.addColumn(employee -> employees.indexOf(employee) + 1).setExpandRatio(0);
        employeeGrid.addColumn(Employee::getFirstName).setCaption("First name").setExpandRatio(1);
        employeeGrid.addColumn(Employee::getLastName).setCaption("Last name").setExpandRatio(2);
        employeeGrid.addColumn(Employee::getSalary).setCaption("Salary").setExpandRatio(1);
        employeeGrid.addComponentColumn(this::editButton).setExpandRatio(0);
        employeeGrid.addComponentColumn(this::deleteButton).setExpandRatio(0);
        employeeGrid.setFrozenColumnCount(1);

        layout.addComponent(horizontalLayout);
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
        Window window = new Window();
        window.setWidth(300.0f, Unit.PIXELS);
        final FormLayout content = new FormLayout();
        content.setMargin(true);
        final TextField firstNameTf = new TextField();
        firstNameTf.setCaption("First name:");
        firstNameTf.setValue(e.getFirstName());
        final TextField lastNameTf = new TextField();
        lastNameTf.setCaption("Last name:");
        lastNameTf.setValue(e.getLastName());
        final TextField salaryTf = new TextField();
        salaryTf.setCaption("Salary:");
        salaryTf.setValue(String.valueOf(e.getSalary()));
        Button saveBtn = new Button("Save", event -> {
            log.info("Save edit: {}", e);
            e.setFirstName(firstNameTf.getValue());
            e.setLastName(lastNameTf.getValue());
            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setParseBigDecimal(true);
            BigDecimal salary = Try.of(() -> (BigDecimal) decimalFormat.parse(salaryTf.getValue())).getOrElse(BigDecimal.ZERO);
            e.setSalary(salary);
            repository.save(e);
            refreshGrid();
            window.close();
        });
        Button cancelBtn = new Button("Cancel", event -> {
            log.info("Cancel edit: {}", e);
            window.close();
        });
        final HorizontalLayout buttonLayout = new HorizontalLayout(
                cancelBtn,
                saveBtn
        );
        content.addComponent(firstNameTf);
        content.addComponent(lastNameTf);
        content.addComponent(salaryTf);
        content.addComponent(buttonLayout);
        window.setContent(content);
        window.setModal(true);
        window.setClosable(false);
        window.setResizable(false);
        this.addWindow(window);
    }

    private Button deleteButton(Employee e) {
        Button button = new Button(VaadinIcons.DEL);
        button.addStyleName(ValoTheme.BUTTON_SMALL);
        button.addClickListener(event -> deleteEmployee(e));
        return button;
    }

    private void deleteEmployee(Employee e) {
        Window window = new Window("Are you sure?");
        //window.setWidth(300.0f, Unit.PIXELS);
        final HorizontalLayout content = new HorizontalLayout();
        content.setMargin(true);
        Button yesBtn = new Button("Yes", event -> {
            log.info("Delete: {}", e);
            repository.delete(e);
            refreshGrid();
            window.close();
        });
        Button noBtn = new Button("No", event -> {
            log.info("NO delete: {}", e);
            window.close();
        });
        content.addComponent(noBtn);
        content.addComponent(yesBtn);
        window.setContent(content);
        window.setModal(true);
        window.setClosable(false);
        window.setResizable(false);
        this.addWindow(window);
    }

    private void refreshGrid() {
        refreshGrid(employeeGrid);
    }

    private void refreshGrid(Grid<Employee> grid) {
        employees = iterableToList(repository.findAll());
        refreshGrid(grid, employees);
    }

    private void refreshGrid(Grid<Employee> grid, List<Employee> employees) {
        grid.setItems(employees);
        grid.recalculateColumnWidths();
    }

    private <T> List<T> iterableToList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    @WebServlet(urlPatterns = "/*", name = "EmpleyeeGUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = EmployeeGui.class, productionMode = false)
    public static class EmpleyeeGUIServlet extends VaadinServlet {
    }
}
