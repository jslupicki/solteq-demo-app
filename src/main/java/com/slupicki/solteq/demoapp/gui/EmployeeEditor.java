package com.slupicki.solteq.demoapp.gui;

import com.slupicki.solteq.demoapp.model.Employee;
import com.vaadin.ui.*;

import java.util.function.BiConsumer;

public class EmployeeEditor extends Window {

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
        setWidth(300.0f, Unit.PIXELS);
        final FormLayout content = new FormLayout();
        content.setMargin(true);
        final TextField firstNameTf = new TextField();
        firstNameTf.setCaption("First name:");
        firstNameTf.setValue(employee.getFirstName());
        final TextField lastNameTf = new TextField();
        lastNameTf.setCaption("Last name:");
        lastNameTf.setValue(employee.getLastName());
        Button saveBtn = new Button("Save", event -> {
            employee.setFirstName(firstNameTf.getValue());
            employee.setLastName(lastNameTf.getValue());
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
        content.addComponent(firstNameTf);
        content.addComponent(lastNameTf);
        content.addComponent(buttonLayout);
        setContent(content);
        setModal(true);
        setClosable(false);
        setResizable(false);
    }
}
