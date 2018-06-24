package com.slupicki.solteq.demoapp.gui;

import com.slupicki.solteq.demoapp.common.Util;
import com.slupicki.solteq.demoapp.model.ContactInfo;
import com.slupicki.solteq.demoapp.model.Employee;
import com.slupicki.solteq.demoapp.model.Salary;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.BiConsumer;

public class EmployeeEditor extends Window {

    private static final Logger log = LoggerFactory.getLogger(EmployeeEditor.class);

    public EmployeeEditor(
            Employee employee,
            BiConsumer<Employee, EmployeeEditor> saveListener,
            BiConsumer<Employee, EmployeeEditor> cancelListener
    ) {
        super();
        init(employee, saveListener, cancelListener);
    }

    public EmployeeEditor(
            String caption,
            Employee employee,
            BiConsumer<Employee, EmployeeEditor> saveListener,
            BiConsumer<Employee, EmployeeEditor> cancelListener
    ) {
        super(caption);
        init(employee, saveListener, cancelListener);
    }

    public void show() {
        UI.getCurrent().addWindow(this);
    }

    private void init(
            Employee employee,
            BiConsumer<Employee, EmployeeEditor> saveListener,
            BiConsumer<Employee, EmployeeEditor> cancelListener
    ) {
        setWidth(80, Unit.EM);

        final VerticalLayout content = new VerticalLayout();
        content.setMargin(true);

        final TextField firstNameTf = new TextField();
        firstNameTf.setCaption("First name:");
        firstNameTf.setValue(employee.getFirstName());
        final TextField lastNameTf = new TextField();
        lastNameTf.setCaption("Last name:");
        lastNameTf.setValue(employee.getLastName());
        final FormLayout formLayout = new FormLayout(
                firstNameTf,
                lastNameTf
        );

        final HorizontalLayout gridLayout = new HorizontalLayout();

        final VerticalLayout salaryLayout = new VerticalLayout();
        final Grid<Salary> salaryGrid = new Grid<>();
        salaryGrid.addColumn(salary -> employee.getSortedSalaries().indexOf(salary) + 1).setExpandRatio(0);
        salaryGrid.addColumn(Salary::getAmount).setCaption("Amount").setExpandRatio(1);
        salaryGrid.addColumn(Salary::getFromDate).setCaption("From date").setExpandRatio(2);
        salaryGrid.addComponentColumn(salary -> Util.editButton(event -> editSalary(salary, employee, salaryGrid))).setExpandRatio(0);
        salaryGrid.addComponentColumn(salary -> Util.deleteButton(event -> deleteSalary(salary, employee, salaryGrid))).setExpandRatio(0);
        salaryGrid.setFrozenColumnCount(1);
        Util.refreshGrid(salaryGrid, employee.getSortedSalaries());

        final HorizontalLayout salariesCaption = Util.captionAndAddButton("Salaries", event -> {
            Salary salary = new Salary(LocalDate.now(), null, BigDecimal.ZERO);
            editSalary(salary, employee, salaryGrid);
        });
        salaryLayout.addComponentsAndExpand(
                salariesCaption,
                salaryGrid
        );

        final VerticalLayout contactInfoLayout = new VerticalLayout();
        final Grid<ContactInfo> contactInfoGrid = new Grid<>();
        contactInfoGrid.addColumn(contactInfo -> employee.getSortedContactInfos().indexOf(contactInfo) + 1).setExpandRatio(0);
        contactInfoGrid.addColumn(ContactInfo::getPhone).setCaption("Phone").setExpandRatio(1);
        contactInfoGrid.addColumn(ContactInfo::getFromDate).setCaption("From date").setExpandRatio(2);
        contactInfoGrid.addComponentColumn(contactInfo -> Util.editButton(event -> editContactInfo(contactInfo, employee, contactInfoGrid))).setExpandRatio(0);
        contactInfoGrid.addComponentColumn(contactInfo -> Util.deleteButton(event -> deleteContactInfo(contactInfo, employee, contactInfoGrid))).setExpandRatio(0);
        contactInfoGrid.setFrozenColumnCount(1);
        Util.refreshGrid(contactInfoGrid, employee.getSortedContactInfos());

        final HorizontalLayout contactInfoCaption = Util.captionAndAddButton("Contact info", event -> {
            ContactInfo contactInfo = new ContactInfo(LocalDate.now(), null, "");
            editContactInfo(contactInfo, employee, contactInfoGrid);
        });
        contactInfoLayout.addComponentsAndExpand(
                contactInfoCaption,
                contactInfoGrid
        );

        gridLayout.addComponentsAndExpand(
                salaryLayout,
                contactInfoLayout
        );

        Button saveBtn = new Button("Save", event -> {
            employee.setFirstName(firstNameTf.getValue());
            employee.setLastName(lastNameTf.getValue());
            employee.updateSearchString();
            saveListener.accept(employee, this);
            close();
        });
        Button cancelBtn = new Button("Cancel", event -> {
            cancelListener.accept(employee, this);
            close();
        });
        final HorizontalLayout buttonLayout = new HorizontalLayout(
                cancelBtn,
                saveBtn
        );

        content.addComponent(formLayout);
        content.addComponent(gridLayout);
        content.addComponent(buttonLayout);

        setContent(content);
        setModal(true);
        setClosable(false);
        setResizable(false);
    }

    private void deleteContactInfo(ContactInfo contactInfo, Employee employee, Grid<ContactInfo> contactInfoGrid) {
        new YesNoWindow("Are you sure?",
                event -> {
                    log.info("Delete contact info: {}", contactInfo);
                    employee.getContactInfos().remove(contactInfo);
                    Util.refreshGrid(contactInfoGrid, employee.getSortedContactInfos());
                },
                event -> log.info("NO delete contact info: {}", contactInfo)
        ).show();
    }

    private void editContactInfo(ContactInfo contactInfo, Employee employee, Grid<ContactInfo> contactInfoGrid) {
        new ContactInfoEditor(
                contactInfo,
                (editedContactInfo, contactInfoEditor) -> {
                    log.info("Save edited contact info: {}", editedContactInfo);
                    employee.addContactInfo(contactInfo);
                    Util.refreshGrid(contactInfoGrid, employee.getSortedContactInfos());
                },
                (editedContactInfo, contactInfoEditor) -> log.info("Cancel add salary: {}", editedContactInfo)
        ).show();
    }

    private void deleteSalary(Salary salary, Employee employee, Grid<Salary> salaryGrid) {
        new YesNoWindow("Are you sure?",
                event -> {
                    log.info("Delete salary: {}", salary);
                    employee.getSalaries().remove(salary);
                    Util.refreshGrid(salaryGrid, employee.getSortedSalaries());
                },
                event -> log.info("NO delete salary: {}", salary)
        ).show();
    }

    private void editSalary(Salary salary, Employee employee, Grid<Salary> salaryGrid) {
        new SalaryEditor(
                salary,
                (editedSalary, salaryEditor) -> {
                    log.info("Save edited salary: {}", editedSalary);
                    employee.addSalary(editedSalary);
                    Util.refreshGrid(salaryGrid, employee.getSortedSalaries());
                },
                (editedSalary, salaryEditor) -> log.info("Cancel add salary: {}", editedSalary)
        ).show();
    }
}
