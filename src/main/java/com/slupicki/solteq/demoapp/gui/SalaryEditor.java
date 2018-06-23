package com.slupicki.solteq.demoapp.gui;

import com.slupicki.solteq.demoapp.model.Salary;
import com.vaadin.ui.*;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.function.BiConsumer;

public class SalaryEditor extends Window {

    private static final Logger log = LoggerFactory.getLogger(SalaryEditor.class);

    public SalaryEditor(
            Salary salary,
            BiConsumer<Salary, SalaryEditor> saveListener,
            BiConsumer<Salary, SalaryEditor> cancelListener
    ) {
        super();
        init(salary, saveListener, cancelListener);
    }

    public SalaryEditor(
            String caption,
            Salary salary,
            BiConsumer<Salary, SalaryEditor> saveListener,
            BiConsumer<Salary, SalaryEditor> cancelListener
    ) {
        super(caption);
        init(salary, saveListener, cancelListener);
    }

    public void show() {
        UI.getCurrent().addWindow(this);
    }

    private void init(
            Salary salary,
            BiConsumer<Salary, SalaryEditor> saveListener,
            BiConsumer<Salary, SalaryEditor> cancelListener
    ) {
        setWidth(20, Unit.EM);
        final FormLayout content = new FormLayout();
        content.setMargin(true);
        final TextField amountTf = new TextField();
        amountTf.setCaption("Amount");
        amountTf.setValue(Try.of(() -> salary.getAmount().toString()).getOrElse(BigDecimal.ZERO.toString()));
        final DateField fromDateDf = new DateField();
        fromDateDf.setDateFormat("yyyy-MM-dd");
        fromDateDf.setCaption("From date:");
        fromDateDf.setValue(salary.getFromDate());
        Button saveBtn = new Button("Save", event -> {
            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setParseBigDecimal(true);
            BigDecimal amount = Try.of(() -> (BigDecimal) decimalFormat.parse(amountTf.getValue())).getOrElse(BigDecimal.ZERO);
            salary.setAmount(amount);
            salary.setFromDate(fromDateDf.getValue());
            saveListener.accept(salary, this);
            close();
        });
        Button cancelBtn = new Button("Cancel", event -> {
            cancelListener.accept(salary, this);
            close();
        });
        final HorizontalLayout buttonLayout = new HorizontalLayout(
                cancelBtn,
                saveBtn
        );
        content.addComponent(amountTf);
        content.addComponent(fromDateDf);
        content.addComponent(buttonLayout);
        setContent(content);
        setModal(true);
        setClosable(false);
        setResizable(false);
    }
}
