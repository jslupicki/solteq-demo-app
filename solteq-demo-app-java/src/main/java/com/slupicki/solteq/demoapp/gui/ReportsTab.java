package com.slupicki.solteq.demoapp.gui;

import com.slupicki.solteq.demoapp.reports.EmployeeReport;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ReportsTab extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(ReportsTab.class);
    private final EmployeeReport employeeReport;

    public ReportsTab(EmployeeReport employeeReport) {
        super();
        this.employeeReport = employeeReport;
        refresh();
    }

    public void refresh() {
        log.info("Refresh {}", this.getClass().getSimpleName());
        removeAllComponents();

        Button employeePdfDownload = new Button("Download PDF");
        StreamResource employeesPdfResource = employeesPdfResource();
        FileDownloader filePdfDownloader = new FileDownloader(employeesPdfResource);
        filePdfDownloader.extend(employeePdfDownload);

        Button employeeHtmlDownload = new Button("Download HTML");
        StreamResource employeesHtmlResource = employeesHtmlResource();
        FileDownloader fileHtmlDownloader = new FileDownloader(employeesHtmlResource);
        fileHtmlDownloader.extend(employeeHtmlDownload);

        addComponents(
                new HorizontalLayout(
                        new Label("Employees report in PDF"),
                        employeePdfDownload
                ),
                new HorizontalLayout(
                        new Label("Employees report in HTML"),
                        employeeHtmlDownload
                )
        );
    }

    private StreamResource employeesPdfResource() {
        return new StreamResource(
                (StreamResource.StreamSource) () -> Try.of(employeeReport::printPDF).getOrNull(),
                "employeesReport.pdf"
        );
    }

    private StreamResource employeesHtmlResource() {
        return new StreamResource(
                (StreamResource.StreamSource) () -> Try.of(employeeReport::printHTML).getOrNull(),
                "employeesReport.html"
        );
    }
}
