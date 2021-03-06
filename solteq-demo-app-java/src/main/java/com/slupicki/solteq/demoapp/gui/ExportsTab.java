package com.slupicki.solteq.demoapp.gui;

import com.slupicki.solteq.demoapp.exports.EmployeeCsvExport;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ExportsTab extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(ReportsTab.class);

    private final EmployeeCsvExport employeeCsvExport;

    @Autowired
    public ExportsTab(EmployeeCsvExport employeeCsvExport) {
        super();
        this.employeeCsvExport = employeeCsvExport;
        refresh();
    }

    public void refresh() {
        log.info("Refresh {}", this.getClass().getSimpleName());
        removeAllComponents();

        Button employeeCsvDownload = new Button("Download");
        StreamResource employeesCsvResource = employeesCsvResource();
        FileDownloader fileDownloader = new FileDownloader(employeesCsvResource);
        fileDownloader.extend(employeeCsvDownload);

        addComponent(new HorizontalLayout(
                new Label("Employees in CSV"),
                employeeCsvDownload
        ));
    }

    private StreamResource employeesCsvResource() {
        return new StreamResource(
                (StreamResource.StreamSource) () -> Try.of(employeeCsvExport::generate).getOrNull(),
                "employees.csv"
        );
    }
}
